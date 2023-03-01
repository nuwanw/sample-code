package org.demo.java.nio.tcp;


import java.nio.channels.SocketChannel;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class TCPClient {
    public static void main(String[] args)
            throws IOException, InterruptedException {
        InetSocketAddress hA = new InetSocketAddress("localhost", 8080);
        SocketChannel client = SocketChannel.open(hA);
        System.out.println("The Client is sending messages to server...");
        // Sending messages to the server
        String[] msg = new String[]{"Time goes fast#", "What next?#", "Bye Bye#"};
        for (int j = 0; j < msg.length; j++) {
            byte[] message = new String(msg[j]).getBytes();
            ByteBuffer buffer = ByteBuffer.wrap(message);
            client.write(buffer);
            System.out.println(msg[j]);
            buffer.clear();
            Thread.sleep(1000);
        }

        ByteBuffer buffer = ByteBuffer.allocate(256);
        client.read(buffer);
        String output = new String(buffer.array()).trim();
        System.out.println("Message read from Server: " + output);

        Thread.sleep(1000);
        client.close();
    }
}
