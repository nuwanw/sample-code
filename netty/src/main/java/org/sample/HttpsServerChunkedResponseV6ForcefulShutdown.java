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
import io.netty.handler.ssl.SslProvider;

import javax.net.ssl.SSLEngine;
import java.io.File;
import java.nio.file.Files;

/**
 * Writes a large payload in single chunked.
 */

public class HttpsServerChunkedResponseV6ForcefulShutdown {

    private static final int PORT = 8263;
    private static final String CERTIFICATE_PATH = "public.crt";
    private static final String PRIVATE_KEY_PATH = "private.key";
    private static final String RESPONSE_FILE_PATH = "100kb-payload.json";

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
                                CustomSslHandler sslHandler = new CustomSslHandler(sslEngine, ch.pipeline().context(this));
                                ch.pipeline().addLast(sslHandler);
                                ch.pipeline().addLast(new HttpServerCodec());
                                ch.pipeline().addLast(new HttpObjectAggregator(65536)); // Aggregate HTTP requests
                                ch.pipeline().addLast(new SimpleChannelInboundHandler<FullHttpRequest>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
                                        if (request.method() == HttpMethod.GET || request.method() == HttpMethod.POST) {
                                            // Read response message from file
                                            byte[] responseBytes = Files.readAllBytes(new File(RESPONSE_FILE_PATH).toPath());

                                            // Create and send response with chunked encoding
                                            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(responseBytes));
                                            response.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
                                            response.headers().set("Connection", "Close");
                                            response.headers().set("Content-Type", "application/json");
                                            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
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
            ChannelFuture channelFuture = serverBootstrap.bind(PORT).sync();

            // Close SSL connection forcefully without sending "Close notify" alert
            channelFuture.channel().closeFuture().addListener(future -> {
                CustomSslHandler sslHandler = channelFuture.channel().pipeline().get(CustomSslHandler.class);
                if (sslHandler != null) {
                    sslHandler.closeChannel();
                }
            }).sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private static class CustomSslHandler extends SslHandler {

        private final ChannelHandlerContext context;

        public CustomSslHandler(SSLEngine engine, ChannelHandlerContext context) {
            super(engine);
            this.context = context;
        }

        @Override
        public void close(ChannelHandlerContext ctx, ChannelPromise promise) {
            System.out.println("Closing SSL connection forcefully");
            context.close(promise);
        }

        public void closeChannel() {
            System.out.println("Closing channel forcefully");
            context.channel().close();
        }
    }
}