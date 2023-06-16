package org.demo.response;

import org.demo.ResponseWriter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import static org.demo.Configuration.RESOURCE_DIR;

public class ChunkedResponse implements ResponseWriter {
    @Override
    public void write(PrintWriter out) {
        out.write("HTTP/1.1 200 OK\r\n");
        out.write("Content-Type: application/json\r\n");
        out.write("Transfer-Encoding: chunked\r\n");
        out.write("Server: tcp-server/1.0\r\n");
        out.write("Connection: Close\r\n");
        out.write("\r\n");
        // out.flush();

        FileInputStream in1 = null;
        try {
            in1 = new FileInputStream(RESOURCE_DIR + "50kb-payload.json");


            int count;
            int chunkSize = 10;
            byte[] buffer = new byte[chunkSize];
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
