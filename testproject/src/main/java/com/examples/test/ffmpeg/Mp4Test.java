package com.examples.test.ffmpeg;

import com.examples.test.util.Base64Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Author: cz
 * @Date: 2021/2/3
 * @Description:
 */
public class Mp4Test {

    public static void main(String[] args) {
        long beginTime = System.currentTimeMillis();

        String sourcePath = "D:\\linux\\ffmpeg\\input\\200.mp4";
        String targetPath = "D:\\linux\\ffmpeg\\output\\200.mov";

        byte[] bytes = Base64Utils.fileTobyte(sourcePath);
        String base64 = Base64Utils.encode(bytes);
        Base64Utils.base64ToFile(base64, targetPath);

        long endTime = System.currentTimeMillis();
        System.out.println("done. time = " + (endTime - beginTime) + " ms");
    }

}
