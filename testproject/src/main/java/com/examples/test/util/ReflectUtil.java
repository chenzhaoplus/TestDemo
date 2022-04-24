package com.examples.test.util;

import com.examples.test.entity.BasicFaceFeatureDTO;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Scanner;

/**
 * @Author: cz
 * @Date: 2022/4/22
 * @Description:
 */
public class ReflectUtil {

    public static void main(String[] args) {
        //test_getFieldValue();
        //test_createInstance();
        test_printConstructorsMethodsFields(args);
    }

    public static void test_printConstructorsMethodsFields(String[] args){
        // read class name from command line args or use input
        String name;
        if(args.length>0){
            name=args[0];
        }else{
            Scanner in = new Scanner(System.in);
            System.out.println("Enter class name (e.g. java.util.Date): ");
            name = in.next();
        }

        try{
            // print class name and superclass name (if != Object)
            Class cl = Class.forName(name);
            Class supercl = cl.getSuperclass();
            String modifiers = Modifier.toString(cl.getModifiers());
            if (modifiers.length()>0) {
                System.out.print(modifiers+" ");
            }
            System.out.print("class "+name);
            if(supercl != null && supercl != Object.class){
                System.out.print(" extends "+supercl.getName());
            }

            System.out.print("\n{\n");
            printConstructors(cl);
            System.out.println();
            printMethods(cl);
            System.out.println();
            printFields(cl);
            System.out.println("}");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        System.exit(0);
    }

    public static void printConstructors(Class cl){
        Constructor[] constructors = cl.getDeclaredConstructors();
        for(Constructor c:constructors){
            String name = c.getName();
            System.out.print("    ");
            String modifiers = Modifier.toString(c.getModifiers());
            if(modifiers.length()>0){
                System.out.print(modifiers+" ");
            }
            System.out.print(name+"(");

            //print parameter types
            Class[] paramTypes = c.getParameterTypes();
            for(int j=0;j<paramTypes.length;j++){
                if(j>0){
                    System.out.print(", ");
                }
                System.out.print(paramTypes[j].getName());
            }
            System.out.println(");");
        }
    }

    public static void printMethods(Class cl){
        Method[] methods = cl.getDeclaredMethods();
        for(Method m:methods){
            Class retType = m.getReturnType();
            String name = m.getName();

            System.out.print("    ");
            // print modifiers, return type and method name
            String modifiers=Modifier.toString(m.getModifiers());
            if(modifiers.length()>0){
                System.out.print(modifiers + " ");
            }
            System.out.print(retType.getName() + " " + name + "(");
            // print parameter types
            Class<?>[] paramTypes = m.getParameterTypes();
            for (int j = 0; j < paramTypes.length; j++) {
                if(j>0){
                    System.out.print(", ");
                }
                System.out.print(paramTypes[j].getName());
            }
            System.out.println(");");
        }
    }

    public static void printFields(Class cl){
        Field[] fields = cl.getDeclaredFields();
        for(Field f:fields){
            Class type=f.getType();
            String name=f.getName();
            System.out.print("    ");
            String modifiers = Modifier.toString(f.getModifiers());
            if(modifiers.length()>0){
                System.out.print(modifiers+" ");
            }
            System.out.println(type.getName()+" "+name+";");
        }
    }

    public static void test_createInstance(){
        BasicFaceFeatureDTO instance = createInstance(BasicFaceFeatureDTO.class);
        instance.setCommunityName("111");
        System.out.println(instance);
    }

    public static void test_getFieldValue(){
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
