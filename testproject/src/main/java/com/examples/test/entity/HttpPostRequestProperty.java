package com.examples.test.entity;

import lombok.Data;

/**
 * @Author: cz
 * @Date: 2020/7/15
 * @Description:
 */
@Data
public class HttpPostRequestProperty {

    //通用头部
    private String connection;//Connection
    private String charset;//Charset
    private String contentType;//Content-Type
    private String accept;//accept
    private String contentLength;//Content-Length
    //业务头部
    private String appId;
    private String v;
    private String random;
    private String timestamp;
    private String sign;

}
