package com.examples.test.util;

import java.util.UUID;

/**
 * @Author: cz
 * @Date: 2020/7/15
 * @Description:
 */
public class UUIDUtils {

    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            String uuid = getUUID();
            System.out.println(uuid);
        }
    }

}
