package com.examples.test.util;

import com.google.gson.*;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenz
 */
public class GsonUtils {

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
