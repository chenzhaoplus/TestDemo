package com.examples.test.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.examples.test.entity.HttpPostRequestProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

/**
 * @Author: cz
 * @Date: 2020/7/13
 * @Description:
 */
public class HttpUtils {

    /**
     * post方式
     * @param url
     * @param param 参数名
     * @param Map 参数名和参数值
     * @param requestCode 请求编码 例如utf-8
     * @param responseCode 返回编码 例如utf-8
     * @author www.yoodb.com
     * @return
     */
    public static String getJsonData(JSONObject jsonParam, String urls) {
        HttpPostRequestProperty defaultHttpPostRequestProperty = getDefaultHttpPostRequestProperty();
        String jsonData = getJsonData(defaultHttpPostRequestProperty, jsonParam, urls);
        return jsonData;
    }

    public static HttpPostRequestProperty getDefaultHttpPostRequestProperty(){
        HttpPostRequestProperty requestProperty = new HttpPostRequestProperty();
        requestProperty.setConnection("Keep-Alive");
        requestProperty.setCharset("UTF-8");
        requestProperty.setAccept("application/json");
        requestProperty.setContentType("application/json; charset=UTF-8");
        return requestProperty;
    }

    public static String getJsonData(HttpPostRequestProperty requestProperty,JSONObject jsonParam, String urls) {
        StringBuffer sb=new StringBuffer();
        try {
            if(jsonParam==null){
                jsonParam=new JSONObject();
            }
            // 创建url资源
            URL url = new URL(urls);
            // 建立http连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置允许输出
            conn.setDoOutput(true);
            // 设置允许输入
            conn.setDoInput(true);
            // 设置不用缓存
            conn.setUseCaches(false);
            // 设置传递方式
            conn.setRequestMethod("POST");
            if(!Objects.isNull(requestProperty)){
                // 设置维持长连接
                setString(conn,"Connection",requestProperty.getConnection());
                // 设置文件字符集:
                setString(conn,"Charset",requestProperty.getCharset());
                // 设置文件长度
                setString(conn,"Content-Length",requestProperty.getContentLength());
                // 设置文件类型:
                setString(conn,"Content-Type",requestProperty.getContentType());
                setString(conn,"accept",requestProperty.getAccept());
                setString(conn,"appId",requestProperty.getAppId());
                setString(conn,"v",requestProperty.getV());
                setString(conn,"random",requestProperty.getRandom());
                setString(conn,"timestamp",requestProperty.getTimestamp());
                setString(conn,"sign",requestProperty.getSign());
            }
            if(StringUtils.isBlank(requestProperty.getContentLength())){
                byte[] data = (jsonParam.toString()).getBytes();
                String contentLength = String.valueOf(data.length);
                setString(conn,"Content-Length",contentLength);
            }
            // 开始连接请求
            conn.connect();
            OutputStream out = new DataOutputStream(conn.getOutputStream()) ;
            // 写入请求的字符串
            out.write((jsonParam.toString()).getBytes());
            out.flush();
            out.close();
            System.out.println(conn.getResponseCode());
            // 请求返回的状态
            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()){
                System.out.println("连接成功");
                // 请求返回的数据
                InputStream in1 = conn.getInputStream();
                try {
                    String readLine=new String();
                    BufferedReader responseReader=new BufferedReader(new InputStreamReader(in1,"UTF-8"));
                    while((readLine=responseReader.readLine())!=null){
                        sb.append(readLine).append("\n");
                    }
                    responseReader.close();
                    System.out.println(sb.toString());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } else {
                System.out.println("error++");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static void setString(HttpURLConnection conn, String key, String value){
        if(StringUtils.isBlank(key) || StringUtils.isBlank(value)){
            return;
        }
        conn.setRequestProperty(key,value);
    }

    public static JSONObject post(String url, String json) {
        HttpPost post = new HttpPost(url);
        JSONObject response = null;
        try {
            // 中文乱码在此解决
            StringEntity s = new StringEntity(json, "UTF-8");
            s.setContentType("application/json");
            post.setEntity(s);
            Header[] headers = new Header[]{};
            post.setHeaders(headers);
            CloseableHttpResponse res = HttpClients.createDefault().execute(post);
            // 返回json格式：
            String result = EntityUtils.toString(res.getEntity());
            response = JSON.parseObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

}
