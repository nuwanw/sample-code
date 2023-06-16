package org.demo.request;

import org.demo.RequestReader;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestReaderByChunking implements RequestReader {
    @Override
    public void read(BufferedReader in) throws IOException {
        String line;
        //reading headers
        while ((line = in.readLine()) != null) {
            System.out.println(line);
            if (line.isEmpty()) {
                break;
            }
        }

        char[] buffer = new char[4096 * 2];
        int bytesRead = 0;
        int chunkSize = 0;
        while ((chunkSize = readChunkSize(in)) != 0) {
            while (bytesRead < chunkSize) {
                int count = in.read(buffer, bytesRead, chunkSize - bytesRead);
                if (count == -1) {
                    throw new IOException("Unexpected end of stream");
                }
                bytesRead += count;
            }
            // do something with the chunk
           // System.out.println(new String(buffer, 0, bytesRead));
            bytesRead = 0;
            // consume the chunk delimiter
            readLine(in);
        }
    }

    private int readChunkSize(BufferedReader in) throws IOException {
        String line = readLine(in);
        if (line == null) {
            throw new IOException("Unexpected end of stream");
        }
        int semicolonIndex = line.indexOf(";");
        if (semicolonIndex != -1) {
            line = line.substring(0, semicolonIndex);
        }
        try {
            return Integer.parseInt(line, 16);
        } catch (NumberFormatException e) {
            throw new IOException("Invalid chunk size: " + line);
        }
    }

    private String readLine(BufferedReader in) throws IOException {
        return in.readLine();
    }
}
