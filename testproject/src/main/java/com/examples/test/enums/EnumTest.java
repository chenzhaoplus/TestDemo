package com.examples.test.enums;

/**
 * @Author: cz
 * @Date: 2020/9/30
 * @Description:
 */
public class EnumTest {

    public static void main(String[] args) {
        String type = AnimalEnum.CAT.name();
        String eat = AnimalEnum.valueOf(type).eat();
        System.out.println(eat);
    }

}
