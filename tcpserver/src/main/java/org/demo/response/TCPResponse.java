package org.demo.response;

import org.demo.ResponseWriter;

import java.io.PrintWriter;

public class TCPResponse implements ResponseWriter {
    @Override
    public void write(PrintWriter out) {
//        System.out.println("waiting for request ");
//        String line = null;
//        while ((line = in.readLine()) != null) {
//            System.out.println("Message received " + line);
//            out.write("hello Client");
//            out.write("\n");
//            out.flush();
//            System.out.println("ack sent");
//        }
    }
}
