package org.demo.request;

import org.demo.RequestReader;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestReaderByContentLength implements RequestReader {
    @Override
    public void read(BufferedReader in) throws IOException {
        // Read the HTTP request headers
        int contentLength = getContentLength(in);
//        System.out.println(contentLength);
        // Read the HTTP request content

        if (contentLength > 0) {
            char[] content = new char[contentLength];
            int bytesRead = 0;
            while (bytesRead < contentLength) {
                int count = in.read(content, bytesRead, contentLength - bytesRead);
                if (count == -1) {
                    throw new IOException("Unexpected end of stream");
                }
                bytesRead += count;
            }
           // System.out.println(new String(content));
        }
    }

    private int getContentLength(BufferedReader in) throws IOException {
        String contentLengthHeader = null;
        String line;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
            if (line.isEmpty()) {
                break;
            }
            if (line.startsWith("Content-Length:")) {
                contentLengthHeader = line.substring("Content-Length:".length()).trim();
            }
        }

        return contentLengthHeader != null ? Integer.parseInt(contentLengthHeader) : 0;
    }

    //    @Override
//    public void read(BufferedReader in) {
//        String headerLine = "";
//        int contentLength = 0;
//        try {
//            while (!(headerLine = in.readLine()).isEmpty()) {
//               // System.out.println(headerLine);
//                if ("Content-Length".equalsIgnoreCase(headerLine.split(":")[0])) {
//                    contentLength = Integer.parseInt(headerLine.split(":")[1].trim());
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
