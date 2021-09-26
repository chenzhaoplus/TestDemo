package com.tyg.model;

import lombok.Data;

/**
 * @author khzhang
 * @date 2020/10/17 11:41
 * @description 文件管理系统返回数据对象
 */
@Data
public class FsResponseEntity {

    public FsResponseEntity(){

    }

    public FsResponseEntity(String name){
        this.name = name;
    }

    public FsResponseEntity(String name, String type){
        this.name = name;
        this.type = type;
    }

    /**
     * 文件名称
     */
    private String name;

    /**
     * 操作权限
     */
    private String permission;

    /**
     * 文件类型
     */
    private String type;

    /**
     * 创建时间
     */
    //private Long createTime;

    /**
     * 修改时间
     */
    private Long updateTime;

    /**
     * 文件大小
     */
    private Long size;

}
