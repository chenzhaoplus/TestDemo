package com.examples.test.httppost;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.examples.test.util.HttpUtils;
import com.examples.test.util.JSONUtils;

/**
 * @Author: cz
 * @Date: 2020/7/13
 * @Description: 通过http调用接口-统一接入接口造数据
 */
public class HttpPostUnifiedAccessTest {

    //uat
    public static String ip = "59.173.19.67";
    public static String port = "3333";

    //门禁流水上传url
    public static final String upsideEntranceUrl = "http://" + ip + ":" + port + "/iot/smcaiot/upsideEntranceRecord";
    //人脸流水上传url
    public static final String upsideFaceRecordUrl = "http://" + ip + ":" + port + "/iot/smcaiot/upsideFaceRecord";
    //uat环境fastdfs文件上传
    public static final String uploadFastdfsUrl = "http://" + ip + ":" + port + "/smcaiot-filemanagement/filestorage/storageFileFdfs";

    public static void main(String[] args) {
        // 门禁流水
        upsideEntrance();
        // 人脸流水
//        upsideFaceRecord();
    }

    public static void upsideEntrance() {
        //请求上传门禁流水
        String path = JSONUtils.getPathFromFile("httppost/Unified/Entrance.json");
        String s = JSONUtils.readJsonFile(path);
        JSONArray jobj = JSON.parseArray(s);
        int size = jobj.size();
        for (int i = 0; i < size; i++) {
            JSONObject o = (JSONObject) jobj.get(i);
            String data = HttpUtils.getJsonData(o, upsideEntranceUrl);
            System.out.println(data);
        }

        //sql修改门禁出入标识
//        update analysis_entrance_record set in_out='1',device_mark='1' WHERE community_id='f73dc1151df34239853941721a155c0b' and device_position_detail = '51号出';
//        update analysis_entrance_record set in_out='0',device_mark='2' WHERE community_id='f73dc1151df34239853941721a155c0b' and device_position_detail = '51号进';
        //sql修改门禁拍照
//        update analysis_entrance_record set store_path='group1/M00/01/6B/rBAEbV8ND9aASaamAAd55J4hfIM959.jpg' WHERE community_id='f73dc1151df34239853941721a155c0b' and id= '3074193653';
//        update analysis_entrance_record set store_path='group1/M00/01/6B/rBAEbV8NECWAbuhtAAA3Fs9QHbQ546.jpg' WHERE community_id='f73dc1151df34239853941721a155c0b' and id ='3074193802';
//        update analysis_entrance_record set store_path='group1/M00/01/6B/rBAEa18NEDGAVLHiAAA6sylp_Mg634.jpg' WHERE community_id='f73dc1151df34239853941721a155c0b' and id= '3074193782';
//        update analysis_entrance_record set store_path='group1/M00/01/6B/rBAEa18NEDqAGRPNAAArZINfa6M371.jpg' WHERE community_id='f73dc1151df34239853941721a155c0b' and id ='3074193762';
//        update analysis_entrance_record set store_path='group1/M00/01/6B/rBAEa18NEESAGgi8AABJiuWtT9I736.jpg' WHERE community_id='f73dc1151df34239853941721a155c0b' and id ='3074193743';
//        update analysis_entrance_record set store_path='group1/M00/01/6B/rBAEbF8NEE6AbC3yAAApHFsHnTE201.jpg' WHERE community_id='f73dc1151df34239853941721a155c0b' and id ='3074193723';
//        update analysis_entrance_record set store_path='group1/M00/01/6B/rBAEbF8NEFiAG8ZJAAApHFsHnTE920.jpg' WHERE community_id='f73dc1151df34239853941721a155c0b' and id ='3074193700';
//        update analysis_entrance_record set store_path='group1/M00/01/6B/rBAEbV8NEGKADWS1AAB8nfd1D4Y273.jpg' WHERE community_id='f73dc1151df34239853941721a155c0b' and id ='3074193689';
    }

    public static void upsideFaceRecord() {
        String path = JSONUtils.getPathFromFile("httppost/Unified/FaceRecord.json");
        String s = JSONUtils.readJsonFile(path);
        JSONArray jobj = JSON.parseArray(s);
        int size = jobj.size();
        for (int i = 0; i < size; i++) {
            JSONObject o = (JSONObject) jobj.get(i);
            String data = HttpUtils.getJsonData(o, upsideFaceRecordUrl);
            System.out.println(data);
        }
    }

}
