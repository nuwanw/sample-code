package org.sample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.ReferenceCountUtil;

import javax.net.ssl.SSLEngine;
import java.io.File;
import java.nio.file.Files;

public class HttpsServerMutipleChunkedResponseV4 {

    private static final int PORT = 8263;
    private static final String CERTIFICATE_PATH = "public.crt";
    private static final String PRIVATE_KEY_PATH = "private.key";
    private static final String RESPONSE_FILE_PATH = "100kb-payload.json";

    public static void main(String[] args) throws Exception {
        // Configure SSL
        SslContext sslContext = SslContextBuilder.forServer(new File(CERTIFICATE_PATH), new File(PRIVATE_KEY_PATH)).build();

        // Configure server
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            SSLEngine sslEngine = sslContext.newEngine(ch.alloc());
                            sslEngine.setUseClientMode(false);
                            sslEngine.setNeedClientAuth(false);
                            SslHandler sslHandler = new SslHandler(sslEngine);

                            // Custom ChannelDuplexHandler to handle SSL close behavior
                            ChannelDuplexHandler sslCloseHandler = new ChannelDuplexHandler() {
                                @Override
                                public void channelInactive(ChannelHandlerContext ctx) {
                                    ctx.channel().close();
                                }

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                    ctx.close();
                                    ReferenceCountUtil.release(cause);
                                }
                            };

                            ch.pipeline().addLast(sslHandler);
                            ch.pipeline().addLast(sslCloseHandler);
                            ch.pipeline().addLast(new HttpServerCodec());
                            ch.pipeline().addLast(new HttpObjectAggregator(65536)); // Aggregate HTTP requests
                            ch.pipeline().addLast(new SimpleChannelInboundHandler<FullHttpRequest>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
                                    if (request.method() == HttpMethod.GET || request.method() == HttpMethod.POST) {
                                        // Read response message from file
                                        byte[] responseBytes = Files.readAllBytes(new File(RESPONSE_FILE_PATH).toPath());

                                        // Create and send response with chunked encoding
//                                        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(responseBytes));
//                                        response.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
//                                        response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
//                                        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

                                        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                                        response.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
                                        response.headers().set("Connection", "Close");
                                        response.headers().set("Content-Type", "application/json");
                                        ctx.write(response);

                                        int chunkSize = 9446;

                                        for (int i = 0; i < responseBytes.length; i += chunkSize) {
                                            int length = Math.min(chunkSize, responseBytes.length - i);
                                            HttpContent chunk = new DefaultHttpContent(Unpooled.wrappedBuffer(responseBytes, i, length));
                                            ctx.write(chunk);
                                            ctx.flush();
                                        }

                                        LastHttpContent lastChunk = LastHttpContent.EMPTY_LAST_CONTENT;
                                        ctx.write(lastChunk).addListener(ChannelFutureListener.CLOSE);
                                        ctx.flush();

                                    }
                                }
                            });
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, false);

            // Start server
            serverBootstrap.bind(PORT).sync().channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}