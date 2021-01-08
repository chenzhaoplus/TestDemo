package com.examples.test.httppost;

import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.examples.test.entity.HttpPostRequestProperty;
import com.examples.test.util.HttpUtils;
import com.examples.test.util.JSONUtils;
import com.examples.test.util.MD5Utils;
import com.examples.test.util.UUIDUtils;

import java.util.UUID;

/**
 * @Author: cz
 * @Date: 2020/7/13
 * @Description: 通过http调用接口-捷顺接口
 */
public class HttpPostJieSoftTest {

    //uat
    public static String ip = "192.168.101.90";
    public static String port = "8091";
    public static String key = "a4d041e8-99bf-11ea-91ad-3a047cc651a1";

    //url-查询所有控制设备
    public static final String queryBaseDevicesUrl = "http://" + ip + ":" + port + "/api/base/devices";
    public static final String queryBaseDeptsUrl = "http://" + ip + ":" + port + "/api/base/depts";

    public static void main(String[] args) {
        String queryBaseDevicesRet = queryBaseDevices();
        String queryBaseDeptsRet = queryBaseDepts();
    }

    /**
     * 查询所有控制设备
     *
     * @return
     */
    public static String queryBaseDevices() {
        HttpPostRequestProperty requestProperty = setDefaultJiesoftHeader();
        String jsonData = HttpUtils.getJsonData(requestProperty, null, queryBaseDevicesUrl);
        return jsonData;
    }

    /**
     * 查询所有组织结构
     *
     * @return
     */
    public static String queryBaseDepts() {
        String path = JSONUtils.getPathFromFile("httppost/Jiesoft/QueryBaseDepts.json");
        String s = JSONUtils.readJsonFile(path);
        JSONObject jsonObject = JSON.parseObject(s);
        HttpPostRequestProperty requestProperty = setDefaultJiesoftHeader();
//        String jsonData = HttpUtils.getJsonData(requestProperty,jsonObject, queryBaseDeptsUrl);
//        return jsonData;

        JSONObject post = HttpUtils.post(queryBaseDeptsUrl, s);
        System.out.println(post.toString());
        return post.toString();
    }

    private static HttpPostRequestProperty setDefaultJiesoftHeader() {
        long timestamp = System.currentTimeMillis() / 1000L;
        String random = UUIDUtils.getUUID();
        HttpPostRequestProperty defaultHttpPostRequestProperty = HttpUtils.getDefaultHttpPostRequestProperty();
        defaultHttpPostRequestProperty.setAppId("app01");
        defaultHttpPostRequestProperty.setV("1.0");
        defaultHttpPostRequestProperty.setRandom(random);
        defaultHttpPostRequestProperty.setTimestamp(String.valueOf(timestamp));
        String sign = generateSign(timestamp, random);
        defaultHttpPostRequestProperty.setSign(sign);
        return defaultHttpPostRequestProperty;
    }

    public static String generateSign(long timestamp, String random) {
        String signRaw = "random" + random + "timestamp" + String.valueOf(timestamp) + "key" + key;
        System.out.println("signRaw = " + signRaw);
        String sign = MD5Utils.stringToMD5(signRaw);
        System.out.println("sign = " + sign);
        return sign;
    }

}
