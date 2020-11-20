package com.examples.test.enums;

/**
 * @Author: cz
 * @Date: 2020/9/30
 * @Description:
 */
public class EnumTest {

    public static void main(String[] args) {
        String type = AnimalEnum.CAT.name();
        String catEat = AnimalEnum.valueOf(type).eat();
        System.out.println(catEat);

        int value = AnimalEnum.PANDA.getValue();
        String pandaEat = AnimalEnum.getName(value).eat();
        System.out.println(pandaEat);
    }

}
