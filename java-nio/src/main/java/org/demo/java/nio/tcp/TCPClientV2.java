package org.demo.java.nio.tcp;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class TCPClientV2 {
    public static void main(String[] args) throws IOException {

        Selector selector = Selector.open();

        InetSocketAddress hA = new InetSocketAddress("localhost", 8080);
        SocketChannel client = SocketChannel.open();
        client.socket().setSoTimeout(60000);
        client.connect(hA);
        client.configureBlocking(false);

        client.register(selector, SelectionKey.OP_WRITE, null);
        int ops = client.validOps();

        //SelectionKey selectKy = client.register(selector, ops, null);


        for (; ; ) {
            System.out.println("Waiting for the select operation...");
            int noOfKeys = selector.select();
            System.out.println("The Number of selected keys are: " + noOfKeys);
            Set selectedKeys = selector.selectedKeys();
            Iterator itr = selectedKeys.iterator();
            while (itr.hasNext()) {
                SelectionKey ky = (SelectionKey) itr.next();
                SocketChannel channel = (SocketChannel) ky.channel();
                if (ky.isConnectable()) {
                    // The new client connection is accepted
                    System.out.println("isConnectable");
                    channel.register(selector, SelectionKey.OP_WRITE);
                } else if (ky.isReadable()) {

                    System.out.println("Client is reading response");
                    ByteBuffer buffer = ByteBuffer.allocate(256);
                    channel.read(buffer);
                    String output = new String(buffer.array()).trim();
                    System.out.println("Message read from Server: " + output);

                    if (output.contains("Bye Bye")) {
                        System.out.println("Client is closing connection");
                        channel.close();
                    } else {
                        channel.register(selector, SelectionKey.OP_WRITE);
                    }

                } else if (ky.isWritable()) {
                    System.out.println("The Client is sending messages to server...");
                    // Sending messages to the server
                    String[] msg = new String[]{"Time goes fast.#", "What next?#", "Bye Bye#"};
                    for (int j = 0; j < msg.length; j++) {
                        byte[] message = new String(msg[j]).getBytes();
                        ByteBuffer buffer = ByteBuffer.wrap(message);
                        channel.write(buffer);
                        System.out.println(msg[j]);
                        buffer.clear();
                        // Thread.sleep(1000);
                    }
                    channel.register(selector, SelectionKey.OP_READ);
                }
                itr.remove();
            }

        } // end of while loop
    } // end of for loop
}

