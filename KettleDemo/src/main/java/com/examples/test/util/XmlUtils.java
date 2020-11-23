package com.examples.test.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

/**
 * @Author: cz
 * @Date: 2020/11/12
 * @Description:
 */
@Slf4j
public class XmlUtils {

    /**
     * xml字符串转文件
     * @param filePath
     * @param xmlStr
     */
    public static void xmlStrToFile(String filePath, String xmlStr){
        try {
            File file = new File(filePath);
            FileUtils.writeStringToFile(file, xmlStr, "UTF-8");
        } catch (IOException e) {
            log.error("[xml转文件失败], e = {}", e.getMessage());
        }
    }

    /**
     * 实体类转化成xml文件
     * @param bean
     * @param filePath
     * @param <T>
     */
    public static <T> void pojoToXmlFile(T bean, String filePath) {
        try {
            JAXBContext jAXBContext = JAXBContext.newInstance(bean.getClass());
            try {
                FileWriter categoryFile = new FileWriter(filePath);
                Marshaller marshaller = jAXBContext.createMarshaller();
                marshaller.marshal(bean, categoryFile);
            } catch (IOException e) {
                log.error("[pojo转xml失败], e = {}", e.getMessage());
            }
        } catch (JAXBException e) {
            log.error("[pojo转xml失败], e = {}", e.getMessage());
        }
    }

    /**
     * xml文件转换成实体类
     * @param bean
     * @param filePath
     * @param <T>
     */
    public static <T> void xmlFileToPojo(T bean, String filePath) {
        //xml转成实体对象
        try {
            JAXBContext jAXBContextW;
            try {
                jAXBContextW = JAXBContext.newInstance(bean.getClass());
                FileReader categoryFileRead = new FileReader(filePath);
                Unmarshaller unmarshaller = jAXBContextW.createUnmarshaller();
                T categoryW = (T) unmarshaller.unmarshal(categoryFileRead);
            } catch (JAXBException e) {
                log.error("[xml转pojo失败], e = {}", e.getMessage());
            }
        } catch (FileNotFoundException e) {
            log.error("[xml转pojo失败], e = {}", e.getMessage());
        }
    }

}
