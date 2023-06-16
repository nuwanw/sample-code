package org.demo.response;

import org.demo.ResponseWriter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import static org.demo.Configuration.RESOURCE_DIR;

public class MultiTCPSegmentResponse implements ResponseWriter {
    @Override
    public void write(PrintWriter out) {
        out.write("HTTP/1.1 200 OK\r\n");
        out.write("Content-Type: application/json\r\n");
        out.write("Content-Length: 413306\r\n");
        out.write("Server: tcp-server/1.0\r\n");
        out.write("Connection: Close\r\n");
        out.write("\r\n");
        out.flush();

        FileInputStream in1 = null;
        try {
            in1 = new FileInputStream(RESOURCE_DIR + "custom-payload-2.json");


            int count;
            int chunkSize = 16384/2;
            byte[] buffer = new byte[chunkSize];
            while ((count = in1.read(buffer)) > 0) {

                if(count < chunkSize) {
                    byte[] tempBuffer = new byte[count];
                    for(int i=0; i < count; i++) {
                        tempBuffer[i] = buffer[i];
                    }
                    out.write(new String(tempBuffer), 0, count);
                } else {
                    out.write(new String(buffer), 0, count);
                }
                out.flush();
            }
           // Thread.sleep(20000);

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
