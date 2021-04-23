package com.examples.test.enums;

/**
 * @Author: cz
 * @Date: 2020/9/30
 * @Description:
 */
public class EnumTest {

    public static void main(String[] args) {
//        test();
        test2();
    }

    public static void test(){
        String type = AnimalEnum.CAT.name();
        String catEat = AnimalEnum.valueOf(type).eat();
        System.out.println(catEat);

        int value = AnimalEnum.PANDA.getValue();
        String pandaEat = AnimalEnum.getEnumByValue(value).eat();
        System.out.println(pandaEat);
    }

    public static void test2(){
        String type = AnimalEnum2.CAT.name();
        String catEat = AnimalEnum2.valueOf(type).getService().get();
        System.out.println(catEat);

        int value = AnimalEnum2.PANDA.getValue();
        String pandaEat = AnimalEnum2.getEnumByValue(value).getService().get();
        System.out.println(pandaEat);
    }

}
