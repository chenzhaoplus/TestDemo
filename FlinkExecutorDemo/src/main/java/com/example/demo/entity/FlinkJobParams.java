package com.example.demo.entity;

import lombok.Data;

import java.util.List;

/**
 * @Author: cz
 * @Date: 2021/1/12
 * @Description:
 */
@Data
public class FlinkJobParams {

    private String jarHdfsUrl;
    private List<String> argsList;

}
