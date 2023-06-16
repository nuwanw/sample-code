package org.demo.response;

import org.demo.ResponseWriter;

import java.io.PrintWriter;

public class SimpleJsonResponse implements ResponseWriter {
    @Override
    public void write(PrintWriter out) {
        out.write("HTTP/1.1 200 OK\r\n");
        out.write("Content-Type: application/json\r\n");
//        out.write("Content-Length: 23\r\n");
        out.write("Content-Length: 15\r\n");
        out.write("Server: tcp-server/1.0\r\n");
//        out.write("Connection: Close\r\n");
        out.write("Connection: Keep-Alive\r\n");
        out.write("\r\n");


        out.write("{\"msg\":\"hello\"}");
        out.flush();
    }
}
