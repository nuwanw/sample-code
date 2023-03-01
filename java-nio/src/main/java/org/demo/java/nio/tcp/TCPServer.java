package org.demo.java.nio.tcp;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.Selector;
import java.nio.channels.SelectionKey;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.util.Set;
import java.util.Iterator;
import java.net.InetSocketAddress;

public class TCPServer {
    public static void main(String[] args) throws IOException {
        // Get the selector
        Selector selector = Selector.open();
        System.out.println("Selector is open for making connection: " + selector.isOpen());
        // Get the server socket channel and register using selector
        ServerSocketChannel SS = ServerSocketChannel.open();
        SS.socket().setSoTimeout(60000);
        InetSocketAddress hostAddress = new InetSocketAddress("localhost", 8080);
        SS.bind(hostAddress);
        SS.configureBlocking(false);
        int ops = SS.validOps();
        SelectionKey selectKy = SS.register(selector, ops, null);
        for (; ; ) {
            System.out.println("Waiting for the select operation...");
            int noOfKeys = selector.select();
            System.out.println("The Number of selected keys are: " + noOfKeys);
            Set selectedKeys = selector.selectedKeys();
            Iterator itr = selectedKeys.iterator();
            while (itr.hasNext()) {
                SelectionKey ky = (SelectionKey) itr.next();
                if (ky.isAcceptable()) {
                    // The new client connection is accepted
                    SocketChannel client = SS.accept();
                    client.configureBlocking(false);
                    // The new connection is added to a selector
                    client.register(selector, SelectionKey.OP_READ, new StringBuffer());
                    System.out.println("The new connection is accepted from the client: " + client);
                } else if (ky.isReadable()) {
                    // Data is read from the client
                    SocketChannel client = (SocketChannel) ky.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(256);
                    client.read(buffer);
                    String input = new String(buffer.array());
                    System.out.println("Message read from client: " + input.trim());
                    StringBuffer requestBuffer = ((StringBuffer) ky.attachment()).append(input.trim());
                    if (requestBuffer.toString().contains("#")) {
                        client.register(selector, SelectionKey.OP_WRITE, requestBuffer);
                    } else {
                        client.register(selector, SelectionKey.OP_READ, requestBuffer);
                    }

                } else if (ky.isWritable()) {
                    //writing data
                    System.out.println("writing data");
                    SocketChannel client = (SocketChannel) ky.channel();
                    StringBuffer requestBuffer = ((StringBuffer) ky.attachment());
                    String newData = "Ack from Server " + System.currentTimeMillis();
                    if (requestBuffer.toString().contains("Bye Bye")) {
                        newData = newData + " Bye Bye";
                    }

                    ByteBuffer bb = ByteBuffer.allocate(48);
                    bb.clear();
                    bb.put(newData.getBytes());
                    bb.flip();
                    while (bb.hasRemaining()) {
                        client.write(bb);
                    }

                    //closing connection
                    if (requestBuffer.toString().contains("Bye Bye")) {
                        requestBuffer.setLength(0);
                        //closing connection
                        client.close();
                        System.out.println("The Client messages are complete; close the session.");
                    } else {
                        requestBuffer.setLength(0);
                        client.register(selector, SelectionKey.OP_READ, requestBuffer);
                    }
                }
                itr.remove();
            } // end of while loop
        } // end of for loop
    }
}
