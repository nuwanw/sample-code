package org.wso2.demo;

import org.apache.axis2.Constants;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.synapse.MessageContext;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.AbstractMediator;
import org.apache.synapse.transport.nhttp.NhttpConstants;
import org.apache.synapse.transport.passthru.PassThroughConstants;
import org.apache.synapse.transport.passthru.Pipe;
import org.apache.synapse.transport.passthru.util.RelayUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RawPayloadLoggerMediator extends AbstractMediator {

    @Override
    public boolean mediate(MessageContext synapseMessageContext) {
        org.apache.axis2.context.MessageContext messageContext = ((Axis2MessageContext) synapseMessageContext)
                .getAxis2MessageContext();

        try {
            final Pipe pipe = (Pipe) messageContext.getProperty(PassThroughConstants.PASS_THROUGH_PIPE);

            if (messageContext.getProperty(Constants.Configuration.CONTENT_TYPE) != null) {
                if (log.isDebugEnabled()) {
                    log.debug("Content Type is " + messageContext.getProperty(Constants.Configuration.CONTENT_TYPE));
                }

                if (pipe != null && !Boolean.TRUE
                        .equals(messageContext.getProperty(PassThroughConstants.MESSAGE_BUILDER_INVOKED))) {

                    InputStream in = pipe.getInputStream();

                    Object http_sc = messageContext.getProperty(NhttpConstants.HTTP_SC);
                    if (http_sc != null && http_sc instanceof Integer && http_sc.equals(202)) {
                        if (in != null) {
                            InputStream bis = new ReadOnlyBIS(in);
                            int c = bis.read();
                            if (c == -1) {
                                messageContext.setProperty(PassThroughConstants.MESSAGE_BUILDER_INVOKED, Boolean.TRUE);
                                messageContext.setProperty(PassThroughConstants.NO_ENTITY_BODY, Boolean.TRUE);
                                return true;
                            }
                            bis.reset();
                            in = bis;
                        }
                    }
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    IOUtils.copy(in, byteArrayOutputStream);
                    byteArrayOutputStream.flush();
                    // Open new InputStreams using the recorded bytes and assign to in
                    in = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                    RelayUtils.buildMessage(messageContext, false, in);
                    log.info("Content : " + byteArrayOutputStream.toString());
                    return true;
                } else {
                    log.info("Message already built. Cannot log the raw content");
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Content Type is null and the message is not build");
                }
                messageContext.setProperty(PassThroughConstants.MESSAGE_BUILDER_INVOKED, Boolean.TRUE);
                return true;
            }

        } catch (Exception e) {
            handleException("Error while logging the message content", e, synapseMessageContext);
        }
        return true;
    }

    @Override
    public boolean isContentAware() {
        return false;
    }

    /**
     * An Un-closable, Read-Only, Reusable, BufferedInputStream
     */
    private static class ReadOnlyBIS extends BufferedInputStream {
        private static final String LOG_STREAM = "org.apache.synapse.transport.passthru.util.ReadOnlyStream";
        private static final Log logger = LogFactory.getLog(LOG_STREAM);

        public ReadOnlyBIS(InputStream inputStream) {
            super(inputStream);
            super.mark(Integer.MAX_VALUE);
            if (logger.isDebugEnabled()) {
                logger.debug("<init>");
            }
        }

        @Override
        public void close() throws IOException {
            super.reset();
            //super.mark(Integer.MAX_VALUE);
            if (logger.isDebugEnabled()) {
                logger.debug("#close");
            }
        }

        @Override
        public void mark(int readlimit) {
            if (logger.isDebugEnabled()) {
                logger.debug("#mark");
            }
        }

        @Override
        public boolean markSupported() {
            return true; //but we don't mark.
        }

        @Override
        public long skip(long n) {
            if (logger.isDebugEnabled()) {
                logger.debug("#skip");
            }
            return 0;
        }
    }
}
