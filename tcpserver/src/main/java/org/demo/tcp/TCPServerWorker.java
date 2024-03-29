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

public class TCPServerWorker implements Runnable {
    private Socket connection = null;

    public TCPServerWorker(Socket connection) {
        this.connection = connection;
    }

    public void run() {
        try {
            Thread.sleep(100);
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            PrintWriter out = new PrintWriter(connection.getOutputStream(), true);

            //            System.out.println("Message received " + in.readLine());
            //            out.write("hello Client");
            //            out.write("\n");
            //            out.flush();
            //            System.out.println("ack sent");
            //
            //            System.out.println("Message received " + in.readLine());
            //            out.write("Accepted\n");
            //            out.flush();
            //            System.out.println("ack sent");
            String line = null;

            System.out.println("waiting for request ");
            while ((line = in.readLine()) != null) {
                System.out.println("Message received " + line);
               out.write("hello Client");
               out.write("\n");
               out.flush();
               System.out.println("ack sent");
            }

            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                System.out.println("Closing connection..");
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

