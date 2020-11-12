package com.examples.test.xml;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author: cz
 * @Date: 2020/11/12
 * @Description:
 */
public class XmlTest {

    public static void main(String[] args) {
        XmlTest xmlTest = new XmlTest();
        xmlTest.pojoToXml();
        xmlTest.xmlToPojo();
        //输出结果
        /*
        <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <category categoryID="c1" categoryName="电子产品" createDate="2020-11-12T15:11:19.259+08:00" deleted="true">
            <products createDate="2020-11-12T15:11:19.261+08:00" price="9000.88" productID="p1" productName="笔记本电脑"/>
            <products createDate="2020-11-12T15:11:19.261+08:00" price="4200.99" productID="p3" productName="PAD"/>
            <products createDate="2020-11-12T15:11:19.261+08:00" price="5200.99" productID="p2" productName="手机"/>
        </category>
        */
    }

    private void pojoToXml() {
        Category category  = initCategory();
        //实体类转化成xml
        try {
            JAXBContext jAXBContext = JAXBContext.newInstance(Category.class);
            try {
                FileWriter categoryFile = new FileWriter("D:\\category.xml");
                Marshaller marshaller = jAXBContext.createMarshaller();
                marshaller.marshal(category, categoryFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private void xmlToPojo() {
        //xml转成实体对象
        try {
            JAXBContext jAXBContextW;
            try {
                jAXBContextW = JAXBContext.newInstance(Category.class);
                FileReader categoryFileRead = new FileReader("D:\\category.xml");
                Unmarshaller unmarshaller = jAXBContextW.createUnmarshaller();
                Category categoryW = (Category) unmarshaller.unmarshal(categoryFileRead);
                System.out.println("种类的名称为："+categoryW.getCategoryName());
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Category initCategory(){
        Category category = new Category();
        category.setCategoryID("c1");
        category.setCategoryName("电子产品");
        category.setDeleted(true);
        category.setCreateDate(new Date());
        category.setProducts(initProducts());
        return category;
    }

    public static Set<Product> initProducts() {
        Set<Product> products = new HashSet<Product>();
        Product product_1 = new Product();
        product_1.setProductID("p1");
        product_1.setProductName("笔记本电脑");
        product_1.setPrice(9000.88);
        product_1.setCreateDate(new Date());


        Product product_2 = new Product();
        product_2.setProductID("p2");
        product_2.setProductName("手机");
        product_2.setPrice(5200.99);
        product_2.setCreateDate(new Date());


        Product product_3 = new Product();
        product_3.setProductID("p3");
        product_3.setProductName("PAD");
        product_3.setPrice(4200.99);
        product_3.setCreateDate(new Date());

        products.add(product_1);
        products.add(product_2);
        products.add(product_3);

        return products;
    }

}
