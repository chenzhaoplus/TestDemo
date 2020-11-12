package com.examples.test.objectpool;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @Author: cz
 * @Date: 2020/10/20
 * @Description:
 */
public class TestObjectPool {

    public static void main(String[] args) {
        DbConnectionFactory factory = new DbConnectionFactory();
        //设置对象池的相关参数
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        //最大空闲
        poolConfig.setMaxIdle(20);
        //最大总数
        poolConfig.setMaxTotal(100);
        //最小空闲
        poolConfig.setMinIdle(5);
        //新建一个对象池,传入对象工厂和配置
        GenericObjectPool<DbConnection> objectPool = new GenericObjectPool<>(factory, poolConfig);
        DbConnection dbConnection = null;
        try {
            //从对象池获取对象，如果
            dbConnection = objectPool.borrowObject();
            System.out.println(dbConnection.getActive());
            //使用改对象
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dbConnection != null) {
                //返还对象
                objectPool.returnObject(dbConnection);
                System.out.println(dbConnection.getActive());
            }
        }

//        for(int i=0;i<1000000;i++){
//
//        }
    }

}
