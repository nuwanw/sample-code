/*
 *  Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.demo;

import org.demo.request.RequestReaderByContentLength;
import org.demo.response.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HttpServerWorker implements Runnable {
    private Socket connection;
    private ResponseWriter writer;
    private RequestReader reader;

    public HttpServerWorker(Socket connection) {
        this.connection = connection;
        reader = new RequestFactory().createReader(RequestReaderByContentLength.class);
        writer = new ResponseFactory().createWriter(ChunkedResponse.class);
        System.out.println(writer.getClass().getName());
    }

    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            out = new PrintWriter(connection.getOutputStream(), true);

//            connection.setSoTimeout(15000);

            //reading the request content
            reader.read(in);
//            connection.setSoTimeout(15000);
            //writing the request content
            writer.write(out);


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {

                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                System.out.println("Client disconnected");
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}

