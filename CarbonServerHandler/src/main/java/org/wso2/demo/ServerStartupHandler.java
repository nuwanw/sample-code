package org.wso2.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.core.ServerStartupObserver;

public class ServerStartupHandler implements ServerStartupObserver {
    private static Log log = LogFactory.getLog(ServerStartupHandler.class);

    /**
     * This will execute just before the transport initialized.
     */
    public void completingServerStartup() {
        log.info("***** Executing completingServerStartup *********");
    }

    /**
     * Tis will execute just after transport initialized.
     */
    public void completedServerStartup() {
        log.info("***** Executing completedServerStartup *********");
    }
}
