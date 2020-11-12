package com.examples.test.objectpool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class DbConnectionFactory implements PooledObjectFactory<DbConnection> {

    @Override
    public PooledObject<DbConnection> makeObject() throws Exception {
        //用于创建对象
        DbConnection dbConnection = new DbConnection();
        return new DefaultPooledObject<>(dbConnection);
    }

    @Override
    public void destroyObject(PooledObject<DbConnection> p) throws Exception {
        //用于销毁对象
        p.getObject().setActive(false);
    }

    @Override
    public boolean validateObject(PooledObject<DbConnection> p) {
        //用于在每次创建对象放入到pool里或者从pool里取对象出来的时候验证是否对象合法
        return p.getObject().getActive();
    }

    @Override
    public void activateObject(PooledObject<DbConnection> p) throws Exception {
        //激活这个对象，让它从idle状态激活
        p.getObject().setActive(true);
    }

    @Override
    public void passivateObject(PooledObject<DbConnection> p) throws Exception {
        //对象要返回给pool的时候则要用passivateObject方法将它钝化
//        p.getObject().setActive(false);
    }
}