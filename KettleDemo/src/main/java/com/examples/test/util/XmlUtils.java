package com.examples.test.util;

import com.examples.test.xml.Category;
import com.examples.test.xml.XmlTest;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @Author: cz
 * @Date: 2020/11/12
 * @Description:
 */
@Slf4j
public class XmlUtils {

    /**
     * 实体类转化成xml
     * @param bean
     * @param filePath
     * @param <T>
     */
    public static <T> void pojoToXml(T bean, String filePath) {
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

    public static <T> void xmlToPojo(T bean, String filePath) {
        //xml转成实体对象
        try {
            JAXBContext jAXBContextW;
            try {
                jAXBContextW = JAXBContext.newInstance(Category.class);
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
