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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPOutputStream;

/**
 * Writes a large payload in chunked response. Also server close the connection after sending the response.
 */

public class HttpsServerChunkedResponseV10ForcefulShutdownGzip {
    private static final Logger logger = LogManager.getLogger(HttpsServerChunkedResponseV10ForcefulShutdownGzip.class);
    private static final int PORT = 8263;
    private static final String CERTIFICATE_PATH = "public.crt";
    private static final String PRIVATE_KEY_PATH = "private.key";

    private static final String RESPONSE_FILE_PATH = System.getProperty("response.file.path");

    public static void main(String[] args) throws Exception {
        if(RESPONSE_FILE_PATH == null) {
            System.out.println("Please provide the response file path as a system property response.file.path");
            logger.info("Please provide the response file path as a system property response.file.path");
            System.exit(1);
        }
        // Load SSL context from the certificate and private key files
        SslContext sslContext = SslContextBuilder
                .forServer(new File(CERTIFICATE_PATH), new File(PRIVATE_KEY_PATH))
                .sslProvider(SslProvider.JDK)
                .build();

        // Configure server
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        NumberSequenceGenerator numberSequenceGenerator = new NumberSequenceGenerator();
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
                                            byte[] compressedBytes = compressGzip(responseBytes);

                                            // Create and send response with gzip compression and chunked encoding
                                            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                                            response.headers().set("Content-Type", "application/json");
                                            response.headers().set(HttpHeaderNames.TRANSFER_ENCODING, HttpHeaderValues.CHUNKED);
                                            response.headers().set(HttpHeaderNames.CONTENT_ENCODING, HttpHeaderValues.GZIP);
                                            response.headers().set("Connection", "Close");
                                            ctx.write(response);
                                            int chunkSize = numberSequenceGenerator.getNextNumber();
                                            logger.info("Chunk Size for activity id " + request.headers().get("activityid") + " is " + chunkSize);
                                            for (int i = 0; i < compressedBytes.length; i += chunkSize) {
                                                int length = Math.min(chunkSize, compressedBytes.length - i);
                                                HttpContent chunk = new DefaultHttpContent(Unpooled.wrappedBuffer(compressedBytes, i, length));
                                                ctx.writeAndFlush(chunk);
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

    private static class CustomSslHandler extends SslHandler {

        public CustomSslHandler(SSLEngine engine) {
            super(engine);
        }

        @Override
        public void close(ChannelHandlerContext ctx, ChannelPromise promise) {
            ctx.close(promise);
        }
    }

    private static byte[] compressGzip(byte[] input) throws Exception {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (GZIPOutputStream gzip = new GZIPOutputStream(output)) {
            gzip.write(input);
        }
        return output.toByteArray();
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