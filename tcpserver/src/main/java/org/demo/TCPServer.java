package org.demo;/*
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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer {
    private ServerSocket providerSocket;
//    private Socket connection = null;
    ExecutorService executor = Executors.newFixedThreadPool(5);

    public void startServer(int port) {
        try {
            providerSocket = new ServerSocket(port, 10);
            System.out.println("Server Started");

            int i = 0;
            while (i < 5) {
                i++;
                Socket connection = providerSocket.accept();
                System.out.println("Client " + i + " connected");
                Runnable worker = new ServerWorker(connection);
                executor.execute(worker);
            }
            System.out.println("Server Shutting down");
            providerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
            while (!executor.isTerminated()) {
            }
        }
    }

    public static void main(String[] args) {
        TCPServer myServer = new TCPServer();
        myServer.startServer(8767);
    }
}
