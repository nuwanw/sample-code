package org.sample;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

import java.io.File;
import java.nio.file.Files;

public class HttpsServerChunkedResponse {
    private static final int PORT = 8263;
    private static final String CERTIFICATE_PATH = "public.crt";
    private static final String PRIVATE_KEY_PATH = "private.key";
    private static final String RESPONSE_FILE_PATH = "large-payload.json";

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
                                ch.pipeline().addLast(sslContext.newHandler(ch.alloc()));
                                ch.pipeline().addLast(new HttpRequestDecoder());
                                ch.pipeline().addLast(new HttpResponseEncoder());
                                ch.pipeline().addLast(new SimpleChannelInboundHandler<HttpObject>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
                                        if (msg instanceof HttpRequest) {
                                            HttpRequest request = (HttpRequest) msg;
                                            if (request.method() == HttpMethod.GET) {
                                                // Read response message from file
                                                byte[] responseBytes = Files.readAllBytes(new File(RESPONSE_FILE_PATH).toPath());

                                                // Create and send response with chunked encoding
                                                DefaultHttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                                                response.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
                                                response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
                                                ctx.write(response);

                                                // Write response chunks
                                                DefaultHttpContent content = new DefaultHttpContent(ctx.alloc().buffer().writeBytes(responseBytes));
                                                ctx.write(content);

                                                // Send last chunk and close connection
                                                DefaultLastHttpContent lastContent = new DefaultLastHttpContent();
                                                ctx.write(lastContent);

                                                ctx.flush(); // Flush the response
                                                ctx.channel().close(); // Close the connection
                                            }
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
