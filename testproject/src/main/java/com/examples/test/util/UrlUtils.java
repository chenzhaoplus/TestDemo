package com.examples.test.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: cz
 * @Date: 2020/7/16
 * @Description:
 */
public class UrlUtils {

    public final static String ENCODE_UTF_8 = "UTF-8";
    public final static String ENCODE_GBK = "GBK";

    /**
     * URL 解码
     *
     * @return String
     * @author lifq
     * @date 2015-3-17 下午04:09:51
     */
    public static String decode(String str, String encodeFormat) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, encodeFormat);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String decode(String str) {
        return decode(str, ENCODE_UTF_8);
    }

    /**
     * URL 转码
     *
     * @return String
     * @author lifq
     * @date 2015-3-17 下午04:10:28
     */
    public static String encode(String str, String encodeFormat) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, encodeFormat);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String encode(String str) {
        return encode(str, ENCODE_UTF_8);
    }

    public static String encodeChinese(String url) throws Exception {
        return encodeChinese(url, ENCODE_UTF_8);
    }

    /**
     * 对含有中文的字符串进行Unicode编码
     * \ue400 \u9fa5 Unicode表中的汉字的头和尾
     */
    public static String encodeChinese(String url, String encodeFormat) {
        StringBuffer sb = new StringBuffer();
        try {
            String regEx = "[\u4e00-\u9fa5]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(url);
            while (m.find()) {
                m.appendReplacement(sb, URLEncoder.encode(m.group(), encodeFormat));
            }
            m.appendTail(sb);
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * @return void
     * @author lifq
     * @date 2015-3-17 下午04:09:16
     */
    public static void main(String[] args) {
        String str = "测试1";
        String encode = encode(str, ENCODE_GBK);
        System.out.println(encode);
        System.out.println(decode(encode, ENCODE_GBK));
    }

}
