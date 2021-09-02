package com.examples.test.file;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;

/**
 * @Author: cz
 * @Date: 2021/9/2
 * @Description:
 */
public class WriteHtmlTest {

    public static void main(String[] args) {
        String input = "<html></html>";
        File file = new File("D:\\test.html");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            IOUtils.write(input.getBytes(), fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static InputStream getStringStream(String input) {
        if (StringUtils.isBlank(input)) {
            return null;
        }
        try {
            return new ByteArrayInputStream(input.getBytes());
        } catch (Exception ex) {
            return null;
        }
    }

}
