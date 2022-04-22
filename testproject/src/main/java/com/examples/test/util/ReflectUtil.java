package com.examples.test.util;

import com.examples.test.entity.BasicFaceFeatureDTO;

import java.lang.reflect.Field;

/**
 * @Author: cz
 * @Date: 2022/4/22
 * @Description:
 */
public class ReflectUtil {

    public static void main(String[] args) {
        BasicFaceFeatureDTO dto = new BasicFaceFeatureDTO();
        dto.setCommunityId("123445");
        Object fieldValue = getFieldValue(dto, "communityId");
        System.out.println(fieldValue);
    }

    public static <T> T getFieldValue(Object bean, String fieldName){
        Field field = null;
        try {
            field = bean.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            String name = field.getName();
            return (T) field.get(bean);
        } catch (NoSuchFieldException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

}
