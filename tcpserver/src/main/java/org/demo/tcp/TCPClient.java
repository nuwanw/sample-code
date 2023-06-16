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
package org.demo.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class TCPClient {
    public static AtomicInteger atomicInteger = new AtomicInteger(10000000);
    public static AtomicInteger responseCount = new AtomicInteger(0);

    public static void main(String[] args) {
        int counter = 10000000;
        for (int x = 0; x < 1; x++) {
            Thread worker = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Socket clientSocket = new Socket("localhost", 8767);
                        PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream());
                        BufferedReader inFromServer = new BufferedReader(
                                new InputStreamReader(clientSocket.getInputStream()));
                        // int counter = 10000000;

                        int requestCount = 100;
                        for (int i = 0; i < requestCount; i++) {
                            //counter = counter + 1;
                            System.out.println("sending request " + i);
                            outToServer.write("Hello Server "+ i);
                            outToServer.write("\n");
                            //Thread.sleep(100);
                           // outToServer.write("");
                            outToServer.flush();
                        }
                        for (int i = 0; i < requestCount; i++) {
                            System.out.println("reading response");
                            String response = inFromServer.readLine();
                            System.out.println("Server :" + response);
                            // responseCount.incrementAndGet();
                        }
                        //
                        //            outToServer.write("exit" + "\n");
                        //            outToServer.flush();
                        //            response = inFromServer.readLine();
                        //            System.out.println("Server " + response);

                        outToServer.close();
                        inFromServer.close();
                        clientSocket.close();
                        System.out.println("close");
                    } catch (IOException e) {
                        e.printStackTrace();
                   } //catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
                }
            });

            worker.start();
        }
        //System.out.println("Response Count " + responseCount.get());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
