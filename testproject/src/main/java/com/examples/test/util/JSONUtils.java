package com.examples.test.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.examples.test.httppost.HttpPostUnifiedAccessTest;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @Author: cz
 * @Date: 2020/7/13
 * @Description:
 */
public class JSONUtils {

    public static String getPathFromFile(String relativePath) {
        String os = System.getProperty("os.name").toLowerCase();
        String path = "";
        if (os.contains("windows")) {
            path = JSONUtils.class.getResource("/").getPath();
        } else {
            path = "/opt/";
        }
        return path + relativePath;
    }

    /**
     * 非spring方式，读取json文件
     */
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * spring的方式
     * @param path
     * @param type
     * @param <T>
     * @return
     */
//    public static <T> T readJsonFromClassPath(String path, Type type) {
//        try{
//            ClassPathResource resource = new ClassPathResource(path);
//            if (resource.exists()) {
//                return JSON.parseObject(resource.getInputStream(), StandardCharsets.UTF_8, type,
//                        // 自动关闭流
//                        Feature.AutoCloseSource,
//                        // 允许注释
//                        Feature.AllowComment,
//                        // 允许单引号
//                        Feature.AllowSingleQuotes,
//                        // 使用 Big decimal
//                        Feature.UseBigDecimal);
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }

}
