package com.examples.test.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: cz
 * @Date: 2020/9/30
 * @Description: 用枚举的方式去掉 if else
 */
@Getter
@AllArgsConstructor
public enum AnimalEnum implements EnumCommon {

    //熊猫
    PANDA(1) {
        @Override
        public String eat() {
            return "吃竹子";
        }
    },
    //猫
    CAT(2) {
        @Override
        public String eat() {
            return "吃鱼";
        }
    },
    //猴子
    MONKEY(3) {
        @Override
        public String eat() {
            return "吃香蕉";
        }
    };

    private int value;

    public static AnimalEnum getEnumByValue(int value){
        for (AnimalEnum item : AnimalEnum.values()) {
            if (item.value == value){
                return item;
            }
        }
        return null;
    }

}