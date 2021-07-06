package com.examples.test.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * @Author: cz
 * 参考： https://www.cnblogs.com/windpoplar/p/10908634.html
 * @Date: 2021/7/6
 * @Description:
 */
public class ProxyTest {

    public static void main(String[] args) {
        System.getProperties().setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        HelloService hello = new HelloServiceImpl();
        InvocationHandler handler = new ProxyHandler(hello);
        HelloService proxyHello = (HelloService) Proxy.newProxyInstance(
                hello.getClass().getClassLoader(), hello.getClass().getInterfaces(), handler);
        proxyHello.sayHello("cz");
        proxyHello.sayHi("cz");

        ByeService bye = new ByeServiceImpl();
        InvocationHandler handler1 = new ProxyHandler(bye);
        ByeService proxyBye = (ByeService) Proxy.newProxyInstance(
                bye.getClass().getClassLoader(), bye.getClass().getInterfaces(), handler1);
        proxyBye.sayBye("cz");
    }

}
