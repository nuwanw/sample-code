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

public class TCPClient {

    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("localhost", 8767);
            PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            outToServer.write("hello\n");
            outToServer.flush();
            System.out.println("reading response");
            String response = inFromServer.readLine();
            System.out.println("Server " + response);

            outToServer.write("exit" + "\n");
            outToServer.flush();
            response = inFromServer.readLine();
            System.out.println("Server " + response);

            outToServer.close();
            inFromServer.close();
            clientSocket.close();
            System.out.println("close");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
