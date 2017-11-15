/*
*  Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

import org.apache.axis2.Constants;
import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.MessageContext;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;

public class JSONFormatter extends AbstractMediator implements ManagedLifecycle {
    public static final String JSON_STRING = "JSON_STRING";
    public static final String JSON_MESSAGE_TYPE = "application/json";
    private static final String JSON_INPUT_STREAM = "org.apache.synapse.commons.json.JsonInputStream";

    @Override
    public void init(SynapseEnvironment synapseEnvironment) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public boolean mediate(MessageContext messageContext) {

        try {
            org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) messageContext)
                    .getAxis2MessageContext();
            StringBuilder soapJsonObject = JsonUtil.toJsonString(messageContext.getEnvelope().getBody().getFirstElement());
            String jsonString = soapJsonObject.toString().replace("null", "\"\"");
            messageContext.getEnvelope().getBody().getFirstElement().detach();
            messageContext.setProperty(JSON_INPUT_STREAM, null);
            axis2MessageContext.setProperty(JSON_STRING, jsonString);
            axis2MessageContext.setProperty(Constants.Configuration.MESSAGE_TYPE, JSON_MESSAGE_TYPE);
            axis2MessageContext.setProperty(Constants.Configuration.CONTENT_TYPE, JSON_MESSAGE_TYPE);

        } catch (Exception fault) {
            handleException("Error while converting to JSON", fault, messageContext);
        }
        return true;
    }

    public boolean isContentAware() {
        return true;
    }
}
