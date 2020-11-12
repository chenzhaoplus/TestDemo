package com.examples.test.enums;

/**
 * @Author: cz
 * @Date: 2020/9/30
 * @Description: 用枚举的方式去掉 if else
 */
public enum AnimalEnum implements EnumCommon {
    PANDA {
        @Override
        public String eat() {
            return "吃竹子";
        }
    }, CAT {
        @Override
        public String eat() {
            return "吃鱼";
        }
    }, MONKEY {
        @Override
        public String eat() {
            return "吃香蕉";
        }
    }
}