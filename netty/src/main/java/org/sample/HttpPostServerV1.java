package org.sample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.*;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpPostServerV1 {
    private static final int PORT = 8080;

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(PORT))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new HttpServerCodec());
                            ch.pipeline().addLast(new HttpObjectAggregator(65536));
                            ch.pipeline().addLast(new SimpleChannelInboundHandler<FullHttpRequest>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
                                    if (request.method() == HttpMethod.POST) {
                                        // Extract request body
                                        ByteBuf content = request.content();
                                        String requestBody = content.toString(CharsetUtil.UTF_8);
                                        System.out.println("Received POST request body: " + requestBody);

                                        // Extract request headers
                                        HttpHeaders headers = request.headers();
                                        for (Map.Entry<String, String> entry : headers.entries()) {
                                            System.out.println("Header: " + entry.getKey() + " = " + entry.getValue());
                                        }

                                        // Extract request parameters
                                        Map<String, String> params = null;
                                        try {
                                            params = getParametersFromRequest(request);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                        for (Map.Entry<String, String> entry : params.entrySet()) {
                                            System.out.println("Parameter: " + entry.getKey() + " = " + entry.getValue());
                                        }
//                                        try {
//                                            Thread.sleep(5000);
//                                        } catch (InterruptedException e) {
//                                            throw new RuntimeException(e);
//                                        }
                                        // Prepare response
//                                        String responseBody = "POST request received";
                                        String responseBody = "{\"msg\": \"Hello\"}";
                                        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK
                                                , Unpooled.wrappedBuffer(responseBody.getBytes()));
//                                        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                                        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
                                        response.headers().set("Connection", HttpHeaderValues.KEEP_ALIVE);
                                        response.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);

                                        // Send response
                                        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
                                    } else {
                                        // Invalid request method, send error response
                                        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.METHOD_NOT_ALLOWED);
                                        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
                                        response.content().writeBytes("Invalid request method".getBytes(StandardCharsets.UTF_8));

                                        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
                                    }
                                }
                            });
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture future = serverBootstrap.bind().sync();
            System.out.println("HTTP server started on port " + future.channel().localAddress());

            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    private static Map<String, String> getParametersFromRequest(FullHttpRequest request) throws IOException {
        Map<String, String> params = new HashMap<>();
        InterfaceHttpPostRequestDecoder decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false), request);
        List<InterfaceHttpData> postData = decoder.getBodyHttpDatas();
        for (InterfaceHttpData data : postData) {
            if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                Attribute attribute = (Attribute) data;
                params.put(attribute.getName(), attribute.getValue());
            }
        }
        decoder.destroy();
        return params;
    }
}