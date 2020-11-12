package com.examples.test.encode;

import com.examples.test.util.MD5Utils;

import java.util.UUID;

/**
 * @Author: cz
 * @Date: 2020/7/15
 * @Description:
 */
public class MD5Test {

    public static void main(String[] args) {
        String random = UUID.randomUUID().toString();
        long timestamp = System.currentTimeMillis()/1000;
        String key = "a4d041e8-99bf-11ea-91ad-3a047cc651a1";
        String signRaw = "random"+random+"timestamp"+String.valueOf(timestamp)+"key"+key;
        System.out.println("signRaw = "+signRaw);
        String sign = MD5Utils.stringToMD5(signRaw);
        System.out.println("sign = "+sign);
    }

}
