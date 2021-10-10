package org.wso2.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.AbstractSynapseHandler;
import org.apache.synapse.MessageContext;

public class MatrixHandler extends AbstractSynapseHandler {
    private static final Log log = LogFactory.getLog(MatrixHandler.class);
    private final static String REQUEST_IN = "RequestINTimeV1";
    private final static String REQUEST_OUT = "RequestOUTTimeV1";
    private final static String APIM_PROCESSING = "APIMProcessing";
    private final static String BACKEND_PROCESSING = "BackProcessing";

    @Override
    public boolean handleRequestInFlow(MessageContext messageContext) {
        log.info("Request IN");
        messageContext.setProperty(REQUEST_IN, System.currentTimeMillis());
        return true;
    }

    @Override
    public boolean handleRequestOutFlow(MessageContext messageContext) {
        messageContext.setProperty(APIM_PROCESSING, System.currentTimeMillis() - (Long)(messageContext.getProperty(REQUEST_IN)));
        messageContext.setProperty(REQUEST_OUT, System.currentTimeMillis());
        return true;
    }

    @Override
    public boolean handleResponseInFlow(MessageContext messageContext) {
        messageContext.setProperty(BACKEND_PROCESSING, System.currentTimeMillis() - (Long)(messageContext.getProperty(REQUEST_OUT)));
        return true;
    }

    @Override
    public boolean handleResponseOutFlow(MessageContext messageContext) {
        log.info("APIM " + messageContext.getProperty(APIM_PROCESSING) + " Backend " + messageContext.getProperty(BACKEND_PROCESSING));
        return true;
    }
}
