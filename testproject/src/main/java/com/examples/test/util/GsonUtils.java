package com.examples.test.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.examples.test.entity.FaceDTO;
import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author chenz
 */
public class GsonUtils {

    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("date", new Date());
        jsonObject.put("timestampe", System.currentTimeMillis());
        String s = GsonUtils.toJson(jsonObject);
        JSONObject jsonObject1 = GsonUtils.fromJson(s, JSONObject.class);
//        Date d = (Date) jsonObject1.get("date");
        System.out.println(s);
        System.out.println(jsonObject1.toJSONString());

        Map<String, Object> map = JSON.parseObject(jsonObject.toString(), new TypeReference<Map<String,Object>>(){});
        System.out.println(map);

        List<FaceDTO> list = new ArrayList<>();
        FaceDTO faceDTO = new FaceDTO();
        faceDTO.setAge(1);
        faceDTO.setScore(2f);
        list.add(faceDTO);

        String faceJson = JSON.toJSONString(faceDTO);
        FaceDTO faceDTO1 = JSON.parseObject(faceJson, new TypeReference<FaceDTO>() {});
        System.out.println(faceDTO1);

//        for(int i = 0; i<3; i++){
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    String s = "{\"communityId\":\"1\"}";
//                    GsonUtils.toJson(s);
//                    System.out.println("GsonUtils#toJson " + Thread.currentThread().getName());
//                }
//            }).start();
//            System.out.println("start a thread");
//        }
    }

    private static ThreadLocal<Gson> threadLocal = new ThreadLocal<Gson>() {
        @Override
        public Gson initialValue() {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new DateSerializer()).setDateFormat(DateFormat.LONG);
            builder.registerTypeAdapter(Date.class, new DateDeserializer()).setDateFormat(DateFormat.LONG);
            return builder.create();
        }
    };


    public static String toJson(Object obj) {
        String json = threadLocal.get().toJson(obj);
        return json;
    }

    public static <T> T  fromJson(String json, Class<T> clazz){
        if(StringUtils.isEmpty(json)){
            return null;
        }
        T t = threadLocal.get().fromJson(json, clazz);
        return t;
    }

    /**
     *
     * @param json
     * @param typeOfT 格式： new TypeToken<List<MdBaseDrug>>(){}.getType()
     * @param <T>
     * @return T
     */
    public static <T> T fromJson(String json, Type typeOfT){
        if(StringUtils.isEmpty(json)){
            return null;
        }
        T t = threadLocal.get().fromJson(json, typeOfT);
        return t;
    }

    public static <T> List<T> fromJson2List(String json, Class clazz) {
        Type type = new ParameterizedTypeImpl(clazz);
        List<T> list =  threadLocal.get().fromJson(json, type);
        return list;
    }

    /**
     * json串转成map
     * @param str
     * @return Map
     */
    public static Map toMap(String str){
        Map map = new HashMap();
        map = threadLocal.get().fromJson(str, map.getClass());
        return map;
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {
        Class clazz;

        public ParameterizedTypeImpl(Class clz) {
            clazz = clz;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{clazz};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }

    public static class DateSerializer implements JsonSerializer<Date> {
        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getTime());
        }

    }

    public static class DateDeserializer implements JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String res = json.getAsString();
            if (null == json || res.length() < 1) {
                return null;
            }
            LocalDate localDate = LocalDate.parse(json.getAsString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            ZoneId zone = ZoneId.systemDefault();
            Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
            return Date.from(instant);
        }
    }

}
