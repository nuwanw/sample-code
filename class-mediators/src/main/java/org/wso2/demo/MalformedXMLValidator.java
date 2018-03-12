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
package org.wso2.demo;

import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.mediators.AbstractMediator;

public class MalformedXMLValidator extends AbstractMediator implements ManagedLifecycle {
    @Override
    public void init(SynapseEnvironment synapseEnvironment) {

    }

    @Override
    public boolean mediate(MessageContext synCtx) {
        try {
            synCtx.getEnvelope().buildWithAttachments();
            synCtx.getEnvelope().getBody().getFirstElement().buildNext();
        } catch (Exception e) {
            synCtx.setProperty(SynapseConstants.ERROR_CODE, 00010);
            synCtx.setProperty(SynapseConstants.ERROR_MESSAGE, e.getMessage());
            handleException(e.getMessage(), e, synCtx);
        }
        return true;
    }

    @Override
    public void destroy() {

    }
}
