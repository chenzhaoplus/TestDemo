package com.examples.test.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author: cz
 * @Date: 2020/7/16
 * @Description:
 */
public class FileUtils {

    public static void main(String[] args) {
        String fileBase64ByUrl = getFileBase64ByUrl("http://test.smcaiot.com/group1/M00/40/16/rBAE1l_iowSAfPpyAACG9EMJ78s.d1fe6f");
        System.out.println(fileBase64ByUrl);
    }

    public static byte[] getFileByteByUrl(String url) {
        byte[] data = null;
        try {
            String encodeUrl = UrlUtils.encodeChinese(url);
            URL urlConet = new URL(encodeUrl);
            HttpURLConnection con = (HttpURLConnection) urlConet.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(4 * 1000);
            InputStream inStream = con.getInputStream();//通过输入流获取图片数据
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            inStream.close();
            data = outStream.toByteArray();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String getFileBase64ByUrl(String url) {
        byte[] data = getFileByteByUrl(url);
        String base64 = Base64Utils.encode(data);
        return base64;
    }

}
