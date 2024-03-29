package org.sample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.SslProvider;


import javax.net.ssl.SSLEngine;
import java.io.File;
import java.nio.file.Files;

public class HttpsServerChunkedResponseV7ForcefulShutdown {

    private static final int PORT = 8263;
    private static final String CERTIFICATE_PATH = "public.crt";
    private static final String PRIVATE_KEY_PATH = "private.key";
    private static final String RESPONSE_FILE_PATH = "large-payload.json";

    public static void main(String[] args) throws Exception {
        // Load SSL context from the certificate and private key files
        SslContext sslContext = SslContextBuilder
                .forServer(new File(CERTIFICATE_PATH), new File(PRIVATE_KEY_PATH))
                .sslProvider(SslProvider.JDK)
                .build();

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
                            try {
                                SSLEngine sslEngine = sslContext.newEngine(ch.alloc());
                                sslEngine.setUseClientMode(false);
                                sslEngine.setNeedClientAuth(false);
                                SslHandler sslHandler = new CustomSslHandler(sslEngine);
                                ch.pipeline().addLast(sslHandler);
                                ch.pipeline().addLast(new HttpServerCodec());
                                ch.pipeline().addLast(new HttpObjectAggregator(65536)); // Aggregate HTTP requests
                                ch.pipeline().addLast(new HttpServerExpectContinueHandler());
                                ch.pipeline().addLast(new SimpleChannelInboundHandler<FullHttpRequest>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
                                        if (request.method() == HttpMethod.GET || request.method() == HttpMethod.POST) {
                                            // Read response message from file
                                            byte[] responseBytes = Files.readAllBytes(new File(RESPONSE_FILE_PATH).toPath());

                                            // Create and send response with chunked encoding
                                            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                                            response.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
                                            response.headers().set("Connection", "Close");
                                            response.headers().set("Content-Type", "application/json");
                                            ctx.write(response);

                                            for (int i = 0; i < responseBytes.length; i += 8048) {
                                                int length = Math.min(8048, responseBytes.length - i);
                                                ByteBuf content = Unpooled.wrappedBuffer(responseBytes, i, length);
                                                HttpContent chunk = new DefaultHttpContent(content);
                                                ctx.writeAndFlush(chunk);
                                            }

                                            LastHttpContent lastChunk = LastHttpContent.EMPTY_LAST_CONTENT;
                                            ctx.writeAndFlush(lastChunk).addListener(ChannelFutureListener.CLOSE);

                                        }
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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

    private static class CustomSslHandler extends SslHandler {

        public CustomSslHandler(SSLEngine engine) {
            super(engine);
        }

        @Override
        public void close(ChannelHandlerContext ctx, ChannelPromise promise) {
            System.out.println("SSL close called");
            ctx.close(promise);
        }
    }
}