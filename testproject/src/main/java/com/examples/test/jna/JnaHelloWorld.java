package com.examples.test.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

/**
 * @Author: cz
 * @Date: 2020/7/3
 * @Description: jna原理：  https://www.jianshu.com/p/ead89497c403
 */
public class JnaHelloWorld {

    public interface CLibrary extends Library {
        //msvcrt.dll是微软在windows操作系统中提供的C语言运行库执行文件
        CLibrary INSTANCE = (CLibrary) Native.loadLibrary((Platform.isWindows() ? "msvcrt" : "c"), CLibrary.class);

        void printf(String format, Object... args);
    }

    public static void main(String[] args) {
        CLibrary.INSTANCE.printf("Hello, World\n");
    }

}
