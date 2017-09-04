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

import org.apache.axiom.om.OMElement;
import org.apache.synapse.MessageContext;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.json.JSONObject;

public class ConvertXmlToJSON extends AbstractMediator {

    public boolean mediate(MessageContext context) {
        try {
            org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) context)
                    .getAxis2MessageContext();
            OMElement xmlResponse = context.getEnvelope().getBody().getFirstElement();
            xmlResponse.build();
            JSONObject jsonObject = new JSONObject(JsonUtil.toJsonString(xmlResponse).toString());

            //setting the payload as the message payload
            JsonUtil.getNewJsonPayload(axis2MessageContext, jsonObject.toString(), true, true);
        } catch (Exception e) {
            handleException("Error while converting the message", e, context);
        }
        return true;
    }
}
