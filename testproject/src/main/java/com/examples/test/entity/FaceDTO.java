package com.examples.test.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: cz
 * @Date: 2020/8/29
 * @Description:
 */
@Data
public class FaceDTO implements Serializable {

    /**
     * 性别
     */
    private String sex;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 人脸特征base64
     */
    private String featureData;
    /**
     * 相似度
     */
    private Float score;

}
