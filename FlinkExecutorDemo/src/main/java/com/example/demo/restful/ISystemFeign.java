package com.example.demo.restful;

import com.smcaiot.cloud.common.entity.RestResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * 调用系统管理微服务
 */
@FeignClient(value = "smcaiot-system" ,fallback = SystemHystrix.class)
public interface ISystemFeign {

    @RequestMapping(value = "syspermission/updatePermission",method = RequestMethod.POST)
    RestResp updatePermission(@RequestBody List<Map> list);
}
