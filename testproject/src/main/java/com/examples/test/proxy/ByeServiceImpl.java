package com.examples.test.proxy;

public class ByeServiceImpl implements ByeService {
    @Override
    public void sayBye(String s) {
        System.out.println(String.format("Bye %s!", s));
    }
}
