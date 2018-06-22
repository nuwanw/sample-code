package org.wso2.demo.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.wso2.carbon.core.ServerStartupObserver;
import org.wso2.demo.ServerStartupHandler;

public class StartupHandlerActivator implements BundleActivator {
    private static Log log = LogFactory.getLog(StartupHandlerActivator.class);

    public void start(BundleContext bundleContext) throws Exception {
        log.info("Activating Server Observer " + ServerStartupHandler.class.getName());
        bundleContext.registerService(ServerStartupObserver.class.getName(), new ServerStartupHandler(), null);
    }

    public void stop(BundleContext bundleContext) throws Exception {

    }
}
