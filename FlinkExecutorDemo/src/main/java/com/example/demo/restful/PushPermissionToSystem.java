package com.example.demo.restful;

import com.smcaiot.cloud.common.service.AbstractPermissionPushTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 权限信息推送
 */
@Component
public class PushPermissionToSystem extends AbstractPermissionPushTask {

   protected static Logger logger= LoggerFactory.getLogger(PushPermissionToSystem.class);

    @Autowired
    private ISystemFeign systemHystrix;

    @Override
    public void run(String... args) {
        List<Map> list=this.getMappingPath();
        try{
            systemHystrix.updatePermission(list);
        }catch (Exception e){
            logger.error("服务调用失败：{}",e.getMessage());
        }
    }
}
