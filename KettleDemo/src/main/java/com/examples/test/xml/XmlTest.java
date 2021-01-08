package com.examples.test.xml;

import com.examples.test.kettle.entity.Connection;
import com.examples.test.util.JaxbUtil;
import com.examples.test.util.XmlUtils;

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
        //pojo类和xml文件互转
        Category category = initCategory();

        XmlUtils.pojoToXmlFile(category, "D:\\category.xml");
        XmlUtils.xmlFileToPojo(category, "D:\\category.xml");

        //pojo类和xml字符串互转
        try {
            String xml = JaxbUtil.convertToXml(category);
            System.out.println(xml);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //pojo类和xml字符串互转
        Connection connection = new Connection();
        connection.setCommit("0");
        connection.setName("dev-mysql-172.16.4.83");
        connection.setServer("172.16.4.83");
        connection.setType("MYSQL");
        connection.setAccess("Native");
        connection.setDatabase("ibmp");
        connection.setPort("3306");
        connection.setUsername("ibmp_test");
        connection.setPassword("Encrypted 585a64666a5849457372756d47726644eaf5c9a41ec586a5be288d55fda7abd6");
        try {
            String xml = JaxbUtil.convertToXml(connection);
            System.out.println(xml);
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    public static Category initCategory() {
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
