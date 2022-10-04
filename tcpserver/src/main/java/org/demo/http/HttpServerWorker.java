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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HttpServerWorker implements Runnable {
    private Socket connection;

    public HttpServerWorker(Socket connection) {
        this.connection = connection;
    }

    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            out = new PrintWriter(connection.getOutputStream(), true);
do{


            System.out.println("Message received " + in.readLine());
            System.out.println("Message received " + in.readLine());
            System.out.println("Message received " + in.readLine());
            System.out.println("Message received " + in.readLine());
            System.out.println("Message received " + in.readLine());


//            while (in.readLine() != null) {
//                System.out.println("readline");
//            }

            //            out.write("HTTP/1.1 204 NO Content\r\n");
            out.write("HTTP/1.1 200 Accepted\r\n");
            out.write("Content-Type: application/json\r\n");
            out.write("Content-Length: 23\r\n");
            out.write("Server: tcp-server/1.0\r\n");
            //            out.write("Connection: Keep-Alive\r\n");
            out.write("Connection: keep-alive\r\n");
            out.write("\r\n");
            out.flush();

            out.write("[\"Ford\", \"BMW\", \"Fiat\"]");
//            for(int i=0; i<9000; i++) {
//                out.write("a");
//            }
//            out.write("<x>hello</x>");
//            out.write("</msg>");
            out.flush();

}while (true);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                if (out != null) {
                    out.close();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (in != null) {
                    in.close();
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

