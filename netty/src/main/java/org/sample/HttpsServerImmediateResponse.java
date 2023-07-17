package org.sample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.CharsetUtil;

import javax.net.ssl.SSLEngine;
import java.io.File;

public class HttpsServerImmediateResponse {

    private static final int PORT = 8263;
    private static final String CERTIFICATE_PATH = "public.crt";
    private static final String PRIVATE_KEY_PATH = "private.key";
    private static final String RESPONSE_FILE_PATH = "100kb-payload.json";

    public static void main(String[] args) throws Exception {
        // Load SSL context from the certificate and private key files
        SslContext sslContext = SslContextBuilder
                .forServer(new File(CERTIFICATE_PATH), new File(PRIVATE_KEY_PATH))
                .build();

        // Configure server
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            SSLEngine sslEngine = sslContext.newEngine(ch.alloc());
                            sslEngine.setUseClientMode(false);
                            pipeline.addLast(new SslHandler(sslEngine));
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new SimpleChannelInboundHandler<HttpObject>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
                                    if (msg instanceof HttpRequest) {
                                        HttpRequest request = (HttpRequest) msg;
                                        HttpHeaders headers = request.headers();
                                        System.out.println("Request Headers:");
                                        headers.forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));

                                        FullHttpResponse response = new DefaultFullHttpResponse(
                                                HttpVersion.HTTP_1_1,
                                                HttpResponseStatus.INTERNAL_SERVER_ERROR,
                                                Unpooled.copiedBuffer("Response", CharsetUtil.UTF_8));
                                        response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
                                        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                                    }
                                }
                            });
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Start server
            ChannelFuture future = serverBootstrap.bind(PORT).sync();
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}