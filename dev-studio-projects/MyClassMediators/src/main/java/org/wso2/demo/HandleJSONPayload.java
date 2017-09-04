package org.wso2.demo;

import org.apache.synapse.MessageContext;
import org.apache.synapse.commons.json.JsonUtil;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.json.JSONObject;

/**
 * This mediator will replace the element fName to name
 * {"fName" : "Nuwan"} --> {"name" : "Nuwan"}
 */

public class HandleJSONPayload extends AbstractMediator {

    public boolean mediate(MessageContext context) {
        try {
            org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) context)
                    .getAxis2MessageContext();
            JSONObject jsonPayload = new JSONObject(JsonUtil.jsonPayloadToString(axis2MessageContext));

            String name = jsonPayload.getString("fName");
            jsonPayload.remove("fName");
            jsonPayload.put("name", name);

            //setting the payload as the message payload
            JsonUtil.getNewJsonPayload(axis2MessageContext, jsonPayload.toString(), true, true);
        } catch (Exception e) {
            handleException("Error while mediating the message", e, context);
        }
        return true;
    }
}
