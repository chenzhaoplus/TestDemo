package com.examples.test.freemark;

import cn.hutool.core.io.IoUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: cz
 * @Date: 2021/9/29
 * @Description:
 */
public class FreemarkerTest {

    public static Configuration cfg;

    static {
        cfg = new Configuration(new Version("2.3.23"));
    }

    public static void main(String[] args) {
        //test1();
        test2();
    }

    @SneakyThrows({IOException.class, TemplateException.class})
    public static void test2() {
        try (InputStream is = FreemarkerTest.class.getResourceAsStream("/freemarker/CreateMysqlTbl.sql")) {
            String createSql = IoUtil.read(is, StandardCharsets.UTF_8);
            Template template = new Template("strTpl", createSql, cfg);
            StringWriter result = new StringWriter();
            Map map = new HashMap();
            map = new HashMap();
            map.put("tableName", "analysis_air_quality_record");
            map.put("cols", "`id` bigint");
            template.process(map, result);
            System.out.println(result.toString());
        }
    }

    public static void test1() {
        try {
            Map map = new HashMap();
            map = new HashMap();
            map.put("name", "张三");
            map.put("money", 10.155);
            map.put("point", 10);
            Template template = new Template("strTpl", "您好${name}，晚上好！您目前余额：${money?string(\"#.##\")}元，积分：${point}", cfg);
            StringWriter result = new StringWriter();
            template.process(map, result);
            System.out.println(result.toString());
            //您好张三，晚上好！您目前余额：10.16元，积分：10
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
