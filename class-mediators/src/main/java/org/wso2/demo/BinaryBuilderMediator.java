/*
*  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.demo;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMText;
import org.apache.axiom.om.impl.llom.OMTextImpl;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.wso2.carbon.relay.StreamingOnRequestDataSource;

import java.io.IOException;
import java.io.InputStream;
import javax.activation.DataHandler;

/**
 * Covert data to binary content so that ExpandingMessageFormatter can write.
 */
public class BinaryBuilderMediator extends AbstractMediator {
    private SOAPFactory soapFactory = OMAbstractFactory.getSOAP11Factory();

    public boolean mediate(MessageContext mc) {

        OMElement binary = mc.getEnvelope().getBody().getFirstElement();
        OMTextImpl data = (OMTextImpl) binary.getFirstOMChild();
        data.setOptimize(true);
        data.setBinary(true);
        try {
            InputStream in = ((DataHandler) data.getDataHandler()).getInputStream();
            StreamingOnRequestDataSource ds = new StreamingOnRequestDataSource(in);
            DataHandler dataHandler = new DataHandler(ds);
            OMText binaryData = soapFactory.createOMText(dataHandler, true);
            data.detach();
            binary.addChild(binaryData);
        } catch (IOException e) {
            handleException("Error while mediating message", e, mc);
        }
        return true;
    }


//    public boolean mediate(MessageContext mc) {
//
//        OMElement binary = mc.getEnvelope().getBody().getFirstElement();
//        OMTextImpl data = (OMTextImpl) binary.getFirstOMChild();
//        //        data.setOptimize(true);
//        //        data.setBinary(true);
//        //            InputStream in = ((DataHandler) data.getDataHandler()).getInputStream();
//        InputStream in = IOUtils.toInputStream(data.getText());
//
//        StreamingOnRequestDataSource ds = new StreamingOnRequestDataSource(in);
//        DataHandler dataHandler = new DataHandler(ds);
//        OMText binaryData = soapFactory.createOMText(dataHandler, true);
//        data.detach();
//        binary.addChild(binaryData);
//        return true;
//    }

}
