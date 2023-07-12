package org.sample;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.util.CharsetUtil;

import javax.net.ssl.SSLEngine;
import java.net.URI;

public class HttpsClientReadDataByChunks {
    private static final String HOST = "localhost";
    private static final int PORT = 8263;
    private static final String REQUEST_PATH = "/api/endpoint";
    private static final String REQUEST_BODY = "Hello, server!";

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // Configure SSL/TLS context
            SslContext sslContext = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE)
                    .build();

            // Configure client
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<>() {
                        @Override
                        protected void initChannel(Channel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            SSLEngine sslEngine = sslContext.newEngine(ch.alloc());
                            sslEngine.setUseClientMode(true);
                            pipeline.addLast(new SslHandler(sslEngine));
                            pipeline.addLast(new HttpClientCodec());
                            pipeline.addLast(new HttpContentDecompressor());
                            pipeline.addLast(new SimpleChannelInboundHandler<HttpObject>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
                                    if (msg instanceof HttpResponse) {
                                        HttpResponse response = (HttpResponse) msg;
                                        System.out.println("Response received:");
                                        System.out.println("Status: " + response.status());
                                        System.out.println("Headers: " + response.headers());
                                    }
                                    if (msg instanceof HttpContent) {
                                        HttpContent content = (HttpContent) msg;
                                        ByteBuf buf = content.content();
                                        String response = buf.toString(CharsetUtil.UTF_8);
                                        System.out.println("Content received: " + response);
                                    }
                                }
                            });
                        }
                    });

            // Start the client and establish connection
            ChannelFuture future = bootstrap.connect(HOST, PORT).sync();
            Channel channel = future.channel();

            // Prepare the HTTP request
            URI uri = new URI(REQUEST_PATH);
            FullHttpRequest request = new DefaultFullHttpRequest(
                    HttpVersion.HTTP_1_1, HttpMethod.POST, uri.getRawPath(), Unpooled.copiedBuffer(REQUEST_BODY, CharsetUtil.UTF_8));
            request.headers().set(HttpHeaderNames.HOST, HOST);
            request.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
            request.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
            request.headers().set(HttpHeaderNames.CONTENT_LENGTH, request.content().readableBytes());

            // Send the HTTP request
            channel.writeAndFlush(request);

            // Wait for the response and close the connection
            channel.closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}