package com.examples.test.util;

import cn.hutool.core.io.IoUtil;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.util.Base64;
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

    public static void main(String[] args) throws FileNotFoundException {
        byte[] bytes = IoUtil.readBytes(new FileInputStream("C:\\Users\\chenz\\Desktop\\人脸比对图片\\123.png"));
        String base64 = Base64.getEncoder().encodeToString(bytes);
        System.out.println(base64);

        //String base64 = "AID6RAAAnEIAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAyE1IPK04OL011Os94GQiPfXqsz26mYO90q/8PEF5sD2Wpxw8XhI7PaDCmz12RsK9qHW6PZPpdj2Z95k8g5nqvFKYXLyLvlo7l7xAPYd9M7zFP3o9zxysPPEXyz3XnoI8CImUPCvoOz2X/uC9CKKnvVPktb2NttI8ItVgPdclpD0L9BS8R6WxPIDJp71f01892zvmPJOlobx3WIa7TZiivX8vNjwvT5E9G0fWPYO7yzuFxsQ8OTsdPZ0cf7s35B09PsM8undv871gq/u8fdJvPc6krL1oIKA9LCULvQNHMLw3UTq82cC7PB/Jj71BC+Y8/JZZPf25LD1DP5Q86quYvagG/zyPklA9BA3vPO2ahL2vV029lm+uPDzI4zs7YBq94Z+9vStCOj4+s1i8Pjk7vBESdL00FSE8zb/0PX8p0LyERSa9oJ2dPRa4ZD19XxK9vLL8PZxDsT1J6/c8rCrSPClXujxuncg8X9cnvQJUxD2XDg8+ACmUPFJl8T34GIC9htwPvVJVOzts2a68vX8zvWAp5T3JeQk+xgM+vfWFuz22aYu969iAvfTbQT2oZuS8HJgNPXuusr1QN429Pl8SvUbc+by+Mrs9hATPPOJkXL0CCTo992eHvXLWA71OGpK8hLMrPkpnpz3iEcI89yxiPZFmoT3yCzQ9tNFXvYQUobxEYIq9GjMAPiWZ7zwNN+s6k3GhPefnNr1djzo9kBX3u5kDgjxjsDQ9kUCavB/UQb3/rEE9ESfnPUuYNz1xW4I9w1rXPZHdSbzN+II9m+P3POxZh72dkGA999LgPDo76z3Rh429shn0PB8gljor6Z+9nDUsvW5+2LyBGOg9PHlxPZgbNT3fN4+8pM2nvT5q071yojo9o8GVvbSuoj1EwsC9QVu+vSqRzr3EmxK9pZ9kPb+bCD7NVZi9sNgVvazt0TyyiFK8RqxEPUMUKr3gMk69QouuuzY+R71VbNe8JGNxvfrcprxGz6Q8eFtBPH3bCz7DDjk8kpk9PCIQ5rwpQwA9AyMLvWHIMj104Dw91mtfPYn2AD32f3k8XLzJPZqVAz2z8m69GMiyPZwEmb1Caqs8ogiMvSBu0TyAeyG6w86lPRPrsz3gy8s8iDMfvRqs9j0GNBc9CHfru38+Kz5Lzw29Z7x3u7Aj7LzGnDw8ZJU+vVaDy713YMw9qrM4PHGZlztm+YO8eN2svfvnf7sOrwG925gPvWXAvbs56r+8q2MFPu6sqzsnOQg85d1UPYsXpz2mhVg7ytlzve/WLb3oNTW9g1oDvnmtBztuOs+7w0wLPT8W7r3aHgs+sFG6vOAmHr2DaZI7CNnOvFtg97w9vpm7g0RzuxGoKj2sAwk9hBTQvQ==";
        String path = "d:\\tmp\\test.png";
        byte[] b = Base64.getDecoder().decode(base64);
        IoUtil.write(new FileOutputStream(path), true, b);
    }

}
