package com.examples.test.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 人脸基本信息
 *
 * @author chenz
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BasicFaceFeatureDTO extends FaceDTO implements Serializable {
    /**
     * 证件号
     */
    private String idCard;
    /**
     * 图片路径
     */
    private String facePath;
    /**
     * 图片路径
     */
    private String appFacePath;
    /**
     * 姓名
     */
    private String name;
    /**
     * 手机号
     */
    private String mobileNumber;
    /**
     * 社区id
     */
    private String communityId;
    /**
     * 小区名称
     */
    private String communityName;
    /**
     * 是否重点
     */
    private String important;
    /**
     * 登记详细地址
     */
    private String liveLocation;
}
