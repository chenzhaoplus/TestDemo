package com.example.demo.restful;

import com.smcaiot.cloud.common.entity.RestResp;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public class SystemHystrix implements ISystemFeign {

    @Override
    public RestResp updatePermission(List<Map> list) {
        return RestResp.failureRestResp("服务调用失败");
    }
}
