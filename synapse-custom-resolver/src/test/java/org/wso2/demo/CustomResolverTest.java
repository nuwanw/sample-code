package org.wso2.demo;

import org.apache.synapse.commons.resolvers.Resolver;
import org.junit.Assert;
import org.junit.Test;

import java.util.ServiceLoader;

public class CustomResolverTest {
    public static void main(String[] args) {
        ServiceLoader<Resolver> loaders = ServiceLoader.load(Resolver.class);
        for (Resolver resolver : loaders) {
            String className = resolver.getClass().getName();
            resolver.setVariable("x");
            //this should print a
            System.out.println("Value after resolving >" + resolver.resolve());
        }
    }

    @Test
    public void testResolver() {
        ServiceLoader<Resolver> loaders = ServiceLoader.load(Resolver.class);
        Assert.assertEquals("Resolver not found", "org.wso2.demo.CustomResolver", loaders.findFirst().get().getClass().getName());
        for (Resolver resolver : loaders) {
            String className = resolver.getClass().getName();
            resolver.setVariable("x");
            String value = resolver.resolve();
            Assert.assertEquals("Resolver failed", "a", value);
        }
    }
}
