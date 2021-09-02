package com.examples.test.file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @Author: cz
 * @Date: 2021/9/2
 * @Description:
 */
public class WriteHtmlTest {

    public static void main(String[] args) throws IOException {
        String text = "<html></html>";
        String filePath = "D:\\test.html";
        File file = new File(filePath);

        //textToFile(text, filePath);

        FileUtils.writeStringToFile(new File(filePath), text);
    }

    public static void textToFile(String text, String filePath) {
        File file = new File(filePath);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            IOUtils.write(text.getBytes(), fos);
        } catch (IOException e) {
            //log.error("[文本转文件失败], err = {}", e);
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
