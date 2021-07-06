package com.examples.test.proxy;

public class HelloServiceImpl implements HelloService {
    @Override
    public void sayHello(String s) {
        System.out.println(String.format("Hello %s!", s));
    }

    @Override
    public void sayHi(String s) {
        System.out.println(String.format("Hi %s!", s));
    }
}
