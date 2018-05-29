package org.demo;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class MyPooledObjectFactory implements PooledObjectFactory<MyObject> {

    public PooledObject makeObject() throws Exception {
        System.out.println("Making object");
        return new DefaultPooledObject<MyObject>(new MyObject());
    }

    public void destroyObject(PooledObject pooledObject) throws Exception {
        System.out.println("destroyObject");
    }

    public boolean validateObject(PooledObject pooledObject) {
        System.out.println("validateObject");
        return true;
    }

    public void activateObject(PooledObject pooledObject) throws Exception {
        System.out.println("activateObject");
    }

    public void passivateObject(PooledObject pooledObject) throws Exception {
        System.out.println("passivateObject");
    }
}
