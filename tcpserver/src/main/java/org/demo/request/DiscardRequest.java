package org.demo.request;

import org.demo.RequestReader;

import java.io.BufferedReader;
import java.io.IOException;

public class DiscardRequest implements RequestReader {

    @Override
    public void read(BufferedReader in) throws IOException {
        //not reading any request content
        System.out.println("Discarding request content");
    }
}
