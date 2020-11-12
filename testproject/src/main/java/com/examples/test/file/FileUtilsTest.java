package com.examples.test.file;

import com.examples.test.util.Base64Utils;
import com.examples.test.util.FileUtils;
import com.examples.test.util.UrlUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: cz
 * @Date: 2020/7/16
 * @Description:
 */
public class FileUtilsTest {

    public static void main(String[] args) {
//        String url = "http://192.168.101.90:9012/down/pic/19700101/park/193449984/080110_鄂AS1458_BLUE_SB.jpg";
//        String url = "http://172.16.4.84/group1/M00/A2/A9/rBAEUl8Wp9SABSRSAADS0AQFX3A.142c46";
//        try{
////            String encode = UrlUtils.encodeChinese(url);
////            System.out.println(encode);
//            String fileBase64ByUrl = FileUtils.getFileBase64ByUrl(url);
//            System.out.println(fileBase64ByUrl);
//        }catch(Exception e){
//            e.printStackTrace();
//        }

        try {
            File file = new File("D:\\hikvision1\\pic\\");
            if(!file.exists()){
                file.mkdirs();
            }
            FileOutputStream fout = new FileOutputStream("D:\\hikvision1\\pic\\" + new String("124.jpg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
