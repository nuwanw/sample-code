package org.demo;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class MyBasePooledObjectFactory extends BasePooledObjectFactory<MyObject> {
    public MyObject create() throws Exception {
        return new MyObject();
    }

    public PooledObject<MyObject> wrap(MyObject myObject) {
        return new DefaultPooledObject<MyObject>(myObject);
    }

    public PooledObject makeObject() throws Exception {
        System.out.println("makeObject");
        return super.makeObject();
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
