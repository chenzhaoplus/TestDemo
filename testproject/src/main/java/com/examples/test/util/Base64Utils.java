package com.examples.test.util;

import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: cz
 * @Date: 2020/7/14
 * @Description:
 */
public class Base64Utils {

    /**
     * 正则判断是否为base64编码1
     *
     * @param str
     * @return
     */
    public static boolean isBase64(String str) {
        String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }

    /**
     * 图片转字符串
     *
     * @param image
     * @return
     */
    public static String encode(byte[] image) {
        BASE64Encoder decoder = new BASE64Encoder();
        return replaceEnter(decoder.encode(image));
    }

    public static String encode(String uri) {
        if (StringUtils.isBlank(uri)) {
            return null;
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return replaceEnter(encoder.encode(uri.getBytes()));
    }

//    public static String encode(String str){
//        if(StringUtils.isBlank(str)){
//            return null;
//        }
//        byte[] bytes = str.getBytes();
//        String encoded = Base64.getEncoder().encodeToString(bytes);
//        return encoded;
//    }

//    public static String decode(String str){
//        if(StringUtils.isBlank(str)){
//            return null;
//        }
//        byte[] decoded = Base64.getDecoder().decode(str);
//        String decodeStr = new String(decoded);
//        return decodeStr;
//    }

    /**
     * 字符串转图片
     *
     * @param base64Str
     * @return
     */
    public static byte[] decode(String base64Str) {
        if (StringUtils.isBlank(base64Str)) {
            return null;
        }
        byte[] b = null;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            b = decoder.decodeBuffer(replaceEnter(base64Str));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return b;
    }

    /**
     * @return
     * @path 图片路径
     */
    public static byte[] fileTobyte(String path) {
        byte[] data = null;
        FileInputStream input = null;
        try {
            input = new FileInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while ((numBytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, numBytesRead);
            }
            data = output.toByteArray();
            output.close();
            input.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public static String replaceEnter(String str) {
        String reg = "[\n-\r]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

    /**
     * base64 视频base64字符串
     * videoFilePath  输出视频文件路径带文件名
     */
    public static void base64ToFile(String base64, String videoFilePath) {
        try {
            byte[] bypes = decode(base64);
            File file = new File(videoFilePath);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bypes, 0, bypes.length);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            System.out.println("base64转换为视频异常: "+e.getMessage());
        }
    }

}
