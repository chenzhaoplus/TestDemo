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

        BasicFaceFeatureDTO instance = createInstance(BasicFaceFeatureDTO.class);
        instance.setCommunityName("111");
        System.out.println(instance);
    }

    public static <T> T getFieldValue(Object bean, String fieldName){
        Field field = null;
        try {
            field = bean.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            String name = field.getName();
            return (T) field.get(bean);
        } catch (NoSuchFieldException e) {
            System.out.println("获取对象属性值失败，NoSuchFieldException：" + e);
        } catch (IllegalAccessException e) {
            System.out.println("获取对象属性值失败，IllegalAccessException：" + e);
        }
        return null;
    }

    public static <T> T createInstance(Class<T> clz) {
        try {
            return (T) clz.newInstance();
        } catch (InstantiationException e) {
            System.out.println("创建实例对象失败，InstantiationException：" + e);
        } catch (IllegalAccessException e) {
            System.out.println("创建实例对象失败，IllegalAccessException：" + e);
        }
        return null;
    }

}
