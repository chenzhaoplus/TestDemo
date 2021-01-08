package com.examples.test.util;

import org.apache.commons.lang3.StringUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * @Author: cz
 * @Date: 2020/7/30
 * @Description:
 */
public class StrUtils {
    public static final String UTF8 = "UTF-8";
    public static final String GBK = "GBK";
    public static final String ISO88591 = "iso8859-1";

    public static String toCharset(String s, String charset) {
        if (StringUtils.isBlank(s)) {
            return "";
        }
        try {
            String encode = encode(s, charset);
            String decode = decode(s, charset);
            return decode;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String encode(String s, String charset) {
        if (StringUtils.isBlank(s)) {
            return "";
        }
        try {
            return URLEncoder.encode(s, charset);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String decode(String s, String charset) {
        if (StringUtils.isBlank(s)) {
            return "";
        }
        try {
            return URLDecoder.decode(s, charset);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void main(String[] args) {
        try {
            String s = "你我他";
            String gbkStr = encode(s, GBK);
            System.out.println(gbkStr);
            String utf8Str = encode(s, UTF8);
            System.out.println(utf8Str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
