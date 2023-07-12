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
import io.netty.handler.stream.ChunkedWriteHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLEngine;
import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Writes a large payload in chunked response. Also server close the connection after sending the response.
 */

public class HttpServerChunkedResponseV9ForcefulShutdown {

    private static final Logger logger = LogManager.getLogger(HttpServerChunkedResponseV9ForcefulShutdown.class);
    private static final int PORT = 9090;

    private static final String RESPONSE_FILE_PATH = "100kb-payload.json";


    public static void main(String[] args) throws Exception {

        // Configure server
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        NumberSequenceGenerator  numberSequenceGenerator = new NumberSequenceGenerator();
        System.out.println("Starting server on port " + PORT);
        logger.info("Starting server on port " + PORT);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            try {
                                ch.pipeline().addLast(new HttpServerCodec());
                                ch.pipeline().addLast(new HttpObjectAggregator(65536)); // Aggregate HTTP requests
                                ch.pipeline().addLast(new ChunkedWriteHandler()); // Enable chunked writing
                                ch.pipeline().addLast(new SimpleChannelInboundHandler<FullHttpRequest>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
                                        if (request.method() == HttpMethod.GET || request.method() == HttpMethod.POST ) {
                                            // Read response message from file
                                            byte[] responseBytes = Files.readAllBytes(new File(RESPONSE_FILE_PATH).toPath());

                                            // Create and send response with chunked encoding
                                            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                                            response.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
                                            response.headers().set("Connection", "Close");
                                            response.headers().set("Content-Type", "application/json");
                                            ctx.write(response);
                                           // Random random = new Random();
                                            //int randomNumber = random.nextInt(9901) + 100; // Generates a random number between 0 and 9900, then adds 100

//                                            int chunkSize = numberSequenceGenerator.getNextNumber();
                                            int chunkSize = 9446;
                                            logger.info("Chunk Size for activity id " + request.headers().get("activityid") + " is " + chunkSize);

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


    private static class NumberSequenceGenerator {
        private AtomicInteger currentNumber;
        private static int start = 6000;

        public NumberSequenceGenerator() {
            this.currentNumber = new AtomicInteger(start);
        }

        public int getNextNumber() {

            int number =  currentNumber.getAndIncrement();
            if (number > 9999) {
                currentNumber.set(start);
            }
            return number;
        }
    }
}