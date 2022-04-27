package com.examples.test.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: cz
 * @Date: 2020/7/16
 * @Description:
 */
public class FileUtils {

    // 1MB
    private static final int ONE_MB = 1 * 1024 * 1024;
    // 1KB
    private static final int ONE_KB = 1 * 1024;
    // 文件结束标识
    private static final int EOF = -1;
    public static final String SUB_FILE_PREFIX = "sub-file-";

    public static void main(String[] args) throws IOException {
        //String fileBase64ByUrl = getFileBase64ByUrl("http://test.smcaiot.com/group1/M00/40/16/rBAE1l_iowSAfPpyAACG9EMJ78s.d1fe6f");
        //System.out.println(fileBase64ByUrl);

        testSliceMergeFile();
    }

    private static void testSliceMergeFile() throws IOException {
        String srcFile = "D:\\workspace\\myfile\\书\\JAVA核心知识点整理.pdf";
        String destDir = "D:\\zip\\slice";
        sliceFile(srcFile, destDir, 200 * ONE_KB);
        mergeFile(destDir, "D:\\zip\\merge\\JAVA核心知识点整理.pdf");
    }

    public static byte[] getFileByteByUrl(String url) {
        byte[] data = null;
        try {
            String encodeUrl = UrlUtils.encodeChinese(url);
            URL urlConet = new URL(encodeUrl);
            HttpURLConnection con = (HttpURLConnection) urlConet.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(4 * 1000);
            InputStream inStream = con.getInputStream();//通过输入流获取图片数据
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            inStream.close();
            data = outStream.toByteArray();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public static String getFileBase64ByUrl(String url) {
        byte[] data = getFileByteByUrl(url);
        String base64 = Base64Utils.encode(data);
        return base64;
    }

    public static void sliceFile(String srcFile, String destDir, int sliceSize) throws IOException {
        FileUtil.mkdir(destDir);
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile))) {
            //int available = bis.available();
            //System.out.println(available);
            //System.out.println(String.format("%.2fMB", available * 1.0 / FILE_SIZE));
            byte[] bytes = new byte[sliceSize];
            int length = EOF;
            int subFileIndex = 1;
            while ((length = bis.read(bytes)) > EOF) {
                try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destDir + "\\" + SUB_FILE_PREFIX + (subFileIndex++) + ".data"));) {
                    bos.write(bytes, 0, length);
                }
            }
        }
    }

    public static void mergeFile(String srcDir, String destFile) throws IOException {
        File file = new File(srcDir);
        if (!file.isDirectory() || ArrayUtil.isEmpty(file.list())) {
            return;
        }
        FileUtil.mkdir(FilenameUtils.getFullPath(destFile));
        // 这里zzz为通用占位符、匹配拆分文件时下标数字
        String filename = SUB_FILE_PREFIX + "zzz.data".replace("zzz", "\\d+");
        //String name = file.getName();
        //System.out.println(name);
        List<String> subFileNames = Arrays.stream(file.list()).filter(s -> s.matches(filename)).sorted().collect(Collectors.toList());
        if (CollectionUtil.isEmpty(subFileNames)) {
            return;
        }
        // 组装文件
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile))) {
            for (String subFilename : subFileNames) {
                try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcDir + "\\" + subFilename));) {
                    //int available = bis.available();
                    //System.out.println(available);
                    // 每次读取1Mb大小的文件
                    byte[] bytes = new byte[ONE_MB];
                    int length = EOF;
                    while ((length = bis.read(bytes)) > EOF) {
                        bos.write(bytes, 0, length);
                    }
                    bos.flush();
                }
            }
        }
    }

}
