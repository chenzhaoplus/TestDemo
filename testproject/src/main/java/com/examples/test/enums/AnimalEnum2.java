package com.examples.test.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Supplier;

/**
 * @Author: cz
 * @Date: 2020/9/30
 * @Description: 用枚举的方式去掉 if else
 */
@Getter
public enum AnimalEnum2 {

    //熊猫
    PANDA(1, new PandaServiceImpl()::eat),
    //猫
    CAT(2, new CatServiceImpl()::eat),
    //猴子
    MONKEY(3, new MonkeyServiceImpl()::eat);

    private int value;
    private Supplier<String> service;

    AnimalEnum2(int value, Supplier<String> service){
        this.value = value;
        this.service = service;
    }

    public static AnimalEnum2 getEnumByValue(int value) {
        for (AnimalEnum2 item : AnimalEnum2.values()) {
            if (item.value == value) {
                return item;
            }
        }
        return null;
    }

}