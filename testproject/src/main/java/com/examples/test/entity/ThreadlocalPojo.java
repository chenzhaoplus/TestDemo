package com.examples.test.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: cz
 * @Date: 2020/8/29
 * @Description:
 */
public class ThreadlocalPojo implements Serializable {

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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getFeatureData() {
        return featureData;
    }

    public void setFeatureData(String featureData) {
        this.featureData = featureData;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }
}
