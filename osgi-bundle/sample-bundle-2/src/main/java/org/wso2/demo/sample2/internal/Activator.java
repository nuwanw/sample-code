package org.wso2.demo.sample2.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
public class Activator implements BundleActivator {
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("Start the sample-2 bundle");
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        System.out.println("Stop the sample-2 bundle");
    }
}