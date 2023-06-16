package org.demo;



public class ResponseFactory {
    public <T extends ResponseWriter> ResponseWriter createWriter(Class<T> c) {

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
