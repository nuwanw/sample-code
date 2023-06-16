package org.demo;


public class RequestFactory {

    public <T extends RequestReader> RequestReader createReader(Class<T> c) {

        try {
            return c.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
