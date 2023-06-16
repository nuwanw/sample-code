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
package org.demo.https;

import org.demo.HttpServerWorker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;

import static org.demo.Configuration.KEYSTORE_PASS;
import static org.demo.Configuration.RESOURCE_DIR;

public class HttpsServer {
    private ExecutorService executor = Executors.newFixedThreadPool(25);
    private ServerSocketFactory serverSocketFactory;
    private ServerSocket providerSocket;
    private boolean runServer = true;

    public void startServer(int port) {

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                stopServer();
            }
        });

        try {
            System.setProperty("javax.net.ssl.keyStore", RESOURCE_DIR + "wso2carbon.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", KEYSTORE_PASS);
            serverSocketFactory = SSLServerSocketFactory.getDefault();
            providerSocket = serverSocketFactory.createServerSocket(port);
            System.out.println("HTTPS Server Started on " + port);
            while (runServer) {
                Socket connection = providerSocket.accept();
                System.out.println("Client connected");
                //connection.setSoTimeout(15000);
                Runnable worker = new HttpServerWorker(connection);
                executor.execute(worker);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                providerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            executor.shutdown();
            while (!executor.isTerminated()) {
            }
        }
    }

    public static void main(String[] args) {
        HttpsServer myServer = new HttpsServer();
        myServer.startServer(8273);
//        myServer.startServer(9090);
    }

    private void stopServer() {
        runServer = false;
        System.out.println("Server Shutting down ");
        try {
            providerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
    }
}
