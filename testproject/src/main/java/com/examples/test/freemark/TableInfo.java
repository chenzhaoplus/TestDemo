package com.examples.test.freemark;

import lombok.Builder;
import lombok.Data;

/**
 * @Author: cz
 * @Date: 2021/9/29
 * @Description:
 */
@Data
@Builder
public class TableInfo {

    private String tableName;

    private String cols;

}
