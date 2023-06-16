package org.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public interface RequestReader {

    public void read(BufferedReader in) throws IOException;
}
