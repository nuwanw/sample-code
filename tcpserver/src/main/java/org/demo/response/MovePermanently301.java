package org.demo.response;

import org.demo.ResponseWriter;

import java.io.PrintWriter;

public class MovePermanently301 implements ResponseWriter {
    @Override
    public void write(PrintWriter out) {
        out.write("HTTP/1.1 301 Moved Permanently\r\n");
        out.write("Server: tcp-server/1.0\r\n");
        out.write("Connection: Close\r\n");
        out.write("Location: https://localhost:8263/demo\r\n");
//        out.write("Content-Length: 0\r\n");
        out.write("Transfer-Encoding: chunked\r\n");
        out.write("\r\n");

        out.print("0\r\n");
       //out.write("\r\n");
        out.flush();
    }
}
