/*
*  Copyright (c) $today.year, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.demo;

import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

public class ClassMediatorCallingAdminService extends AbstractMediator {
    public boolean mediate(MessageContext messageContext) {
        try {
            System.setProperty("javax.net.ssl.trustStore", "/Users/nuwanw/wso2/align/wso2am-1.7.0/repository/resources/security/client-truststore.jks");
//            System.setProperty("javax.net.ssl.trustStore", "/Users/nuwanw/wso2/align/wso2is-5.0.0/repository/resources/security/client-truststore.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
            System.setProperty("javax.net.ssl.trustStoreType", "JKS");
            //String backEndUrl = "https://scitrvemali-ds1:9443";
//        String backEndUrl = "https://ppauth.aligntech.com:443";
            String backEndUrl = "https://localhost:9445";

            LoginAdminServiceClient login = new LoginAdminServiceClient(backEndUrl);
//        String session = login.authenticate("adminserviceclient", "Admin@123");
//        String session = login.authenticate("spsdevadmin", "$harepoint123");
//        String session = login.authenticate("spsdevadmin", "$harepoint123");
            String session = login.authenticate("admin", "admin");

        } catch (Exception e) {
    e.printStackTrace();
        }
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
