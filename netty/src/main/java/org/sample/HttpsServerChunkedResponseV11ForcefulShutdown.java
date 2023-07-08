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
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Writes a large payload in chunked response. Also server close the connection after sending the response.
 * -Dresponse.file.path=100kb-payload.json -Dchunk.length=6000
 * chunk.length is optional
 */

public class HttpsServerChunkedResponseV11ForcefulShutdown {

    private static final Logger logger = LogManager.getLogger(HttpsServerChunkedResponseV11ForcefulShutdown.class);
    private static final int PORT = 8263;
    private static final String CERTIFICATE_PATH = "public.crt";
    private static final String PRIVATE_KEY_PATH = "private.key";

    private static final String RESPONSE_FILE_PATH = System.getProperty("response.file.path");
    private static final String CHUNK_LENGTH = System.getProperty("chunk.length");
    private static int chunkLength = -1;


    public static void main(String[] args) throws Exception {
        if(RESPONSE_FILE_PATH == null) {
            System.out.println("Please provide the response file path as a system property response.file.path");
            logger.info("Please provide the response file path as a system property response.file.path");
            System.exit(1);
        }
        if(CHUNK_LENGTH == null) {
            System.out.println("System property chunk.length not define,  chunk length will vary from 6000 to 9999");
            logger.info("System chunk.length not define, chunk length will vary from 6000 to 9999)");
        } else {
            chunkLength = Integer.parseInt(CHUNK_LENGTH);
            System.out.println("Chunk length property define,  chunk length will be " + CHUNK_LENGTH);
            logger.info("Chunk length property define,  chunk length will be " + CHUNK_LENGTH);
        }
        // Load SSL context from the certificate and private key files
        SslContext sslContext = SslContextBuilder
                .forServer(new File(CERTIFICATE_PATH), new File(PRIVATE_KEY_PATH))
                .sslProvider(SslProvider.JDK)
                .build();

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
                                SSLEngine sslEngine = sslContext.newEngine(ch.alloc());
                                sslEngine.setUseClientMode(false);
                                sslEngine.setNeedClientAuth(false);
                                SslHandler sslHandler = new CustomSslHandler(sslEngine);
                                ch.pipeline().addLast(sslHandler);
                                ch.pipeline().addLast(new HttpServerCodec());
                                ch.pipeline().addLast(new HttpObjectAggregator(65536)); // Aggregate HTTP requests
                                ch.pipeline().addLast(new ChunkedWriteHandler()); // Enable chunked writing
                                ch.pipeline().addLast(new SimpleChannelInboundHandler<FullHttpRequest>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
                                        if (request.method() == HttpMethod.GET) {
                                            // Read response message from file
                                            byte[] responseBytes = Files.readAllBytes(new File(RESPONSE_FILE_PATH).toPath());

                                            // Create and send response with chunked encoding
                                            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                                            response.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
                                            response.headers().set("Connection", "Close");
                                            response.headers().set("Content-Type", "application/json");
                                            ctx.write(response);
//                                            Random random = new Random();
//                                            int randomNumber = random.nextInt(9901) + 100; // Generates a random number between 0 and 9900, then adds 100

                                            int chunkSize = 6000;
                                            if(CHUNK_LENGTH != null) {
                                                chunkSize = chunkLength;
                                            } else {
                                                chunkSize = numberSequenceGenerator.getNextNumber();
                                            }
//                                            int chunkSize = 7888;
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
                                logger.error((e));
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
            ctx.close(promise);
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