package com.examples.test.util;

import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.stream.FileImageInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: cz
 * @Date: 2020/7/14
 * @Description:
 */
public class Base64Utils {

    public static void main(String[] args) {

    }

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
     * @param image
     * @return
     */
    public static String encode(byte[] image){
        BASE64Encoder decoder = new BASE64Encoder();
        return replaceEnter(decoder.encode(image));
    }

    public static String encode(String uri){
        if(StringUtils.isBlank(uri)){
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
     * @param base64Str
     * @return
     */
    public static byte[] decode(String base64Str){
        if(StringUtils.isBlank(base64Str)){
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
     *
     * @path    图片路径
     * @return
     */
    public static byte[] imageTobyte(String path){
        byte[] data = null;
        FileImageInputStream input = null;
        try {
            input = new FileImageInputStream(new File(path));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int numBytesRead = 0;
            while((numBytesRead = input.read(buf)) != -1){
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

    public static String replaceEnter(String str){
        String reg ="[\n-\r]";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(str);
        return m.replaceAll("");
    }

}
