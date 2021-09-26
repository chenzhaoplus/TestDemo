package com.tyg.model;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: cz
 * @Date: 2021/7/26
 * @Description:
 */
@Data
@Builder
public class FsConnConfig {

    private String username;
    private String pwd;
    private String ip;
    private String port;
    private String relativePath;

}
