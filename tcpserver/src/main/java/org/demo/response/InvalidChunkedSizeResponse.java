package org.demo.response;

import org.demo.ResponseWriter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import static org.demo.Configuration.RESOURCE_DIR;

public class InvalidChunkedSizeResponse implements ResponseWriter {
    @Override
    public void write(PrintWriter out) {
        out.write("HTTP/1.1 200 OK\r\n");
        out.write("Content-Type: application/json\r\n");
        out.write("Transfer-Encoding: chunked\r\n");
        out.write("Server: tcp-server/1.0\r\n");
        out.write("Connection: Close\r\n");
        out.write("\r\n");
        out.flush();

        FileInputStream in1 = null;
        try {
            in1 = new FileInputStream(RESOURCE_DIR + "custom-payload-2.json");

            int count;
            int chunkSize = 8186;
            byte[] buffer = new byte[chunkSize];

            count = in1.read(buffer);
            out.print(Integer.toHexString(8187) + "\r\n");

            out.write(new String(buffer), 0, count);
            out.print("\r\n");
            out.flush();

//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }

            out.print(Integer.toHexString(10000) + "\r\n");
            buffer = new byte[9000];
            count = in1.read(buffer);
            out.write(new String(buffer), 0, count);
            out.flush();

//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }

            buffer = new byte[1000];
            count = in1.read(buffer);
            out.write(new String(buffer), 0, count);
            out.print("\r\n");
            out.flush();

            chunkSize = 8180;
            while ((count = in1.read(buffer)) > 0) {

                out.print(Integer.toHexString(count) + "\r\n");

                if(count < chunkSize) {
                    byte[] tempBuffer = new byte[count];
                    for(int i=0; i < count; i++) {
                        tempBuffer[i] = buffer[i];
                    }
                    out.write(new String(tempBuffer), 0, count);
                } else {
                    out.write(new String(buffer), 0, count);
                }
                out.print("\r\n");
                out.flush();

            }
            out.print("0\r\n");
            out.print("\r\n");
            out.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                in1.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
