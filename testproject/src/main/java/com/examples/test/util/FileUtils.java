package com.examples.test.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
    private static final Float MAX_PIC_LENGTH = 50.00f;
    // 文件结束标识
    private static final int EOF = -1;
    public static final String SUB_FILE_PREFIX = "sub-file-";

    public static void main(String[] args) throws IOException {
        //String fileBase64ByUrl = getFileBase64ByUrl("http://test.smcaiot.com/group1/M00/40/16/rBAE1l_iowSAfPpyAACG9EMJ78s.d1fe6f");
        //System.out.println(fileBase64ByUrl);

        //testSliceMergeFile();

        //testImageCompression();

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

    //public static void testImageCompression() throws IOException {
    //    String oldImg = "D:\\zip\\pic\\1.jpg";
    //    String newImg = "D:\\zip\\pic\\2.jpg";
    //    Float qty = 0.1f;
    //    imageCompression(oldImg, newImg);
    //}
    //
    //public static void imageCompression(String oldImg, String newImg) throws IOException {
    //    imageCompression(oldImg, newImg, null);
    //}
    //
    //public static void imageCompression(String oldImg, String newImg, Float quality) throws IOException {
    //    imageCompression(oldImg, newImg, null, null, quality);
    //}
    //
    ///**
    // * @param imgsrc  原图片路径
    // * @param imgdist 压缩后新文件路径
    // * @param width   宽
    // * @param height  高
    // * @param quality 图片质量 (0 - 1.0f)浮点型
    // * @author kcy
    // * @version 2018-11-8
    // */
    //public static void imageCompression(String oldImg, String newImg, Integer width, Integer height, Float quality) throws IOException {
    //    //Thumbnails.of() <- 这个方法虽然可以直接使用原图路径读取图片,但是有很大几率会使压缩后的图片颜色失真。也建议不要用ImageIO.read()这种方法读取图片，灰常容易失真!
    //    //使用Toolkit.getDefaultToolkit().getImage()读取图片，防止图片颜色失真。
    //    Image image = Toolkit.getDefaultToolkit().getImage(oldImg);
    //    //Image转换BufferedImage
    //    BufferedImage bufferedImage = toBufferedImage(image);
    //    quality = Optional.ofNullable(quality).orElse(getPicQualityByRatio(oldImg));
    //    width = Optional.ofNullable(width).orElse(bufferedImage.getWidth() / 2);
    //    height = Optional.ofNullable(height).orElse(bufferedImage.getHeight() / 2);
    //    //图片压缩
    //    Thumbnails.of(bufferedImage).size(width, height).outputFormat("jpg").keepAspectRatio(true).outputQuality(quality).toFile(newImg);
    //}
    //
    //private static Float getPicQualityByRatio(String filePath){
    //    long length = getFileLength(filePath) / ONE_KB;
    //    return Float.valueOf(MAX_PIC_LENGTH / length);
    //}
    //
    //private static long getFileLength(String filePath){
    //    File file = new File(filePath);
    //    return file.length();
    //}
    //
    //public static BufferedImage toBufferedImage(Image image) {
    //    if (image instanceof BufferedImage) {
    //        return (BufferedImage) image;
    //    }
    //    // This code ensures that all the pixels in the image are loaded
    //    image = new ImageIcon(image).getImage();
    //    BufferedImage bimage = null;
    //    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    //    try {
    //        int transparency = Transparency.OPAQUE;
    //        GraphicsDevice gs = ge.getDefaultScreenDevice();
    //        GraphicsConfiguration gc = gs.getDefaultConfiguration();
    //        bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), transparency);
    //    } catch (HeadlessException e) {
    //        // The system does not have a screen
    //    }
    //    if (bimage == null) {
    //        // Create a buffered image using the default color model
    //        int type = BufferedImage.TYPE_INT_RGB;
    //        bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
    //    }
    //    // Copy image to buffered image
    //    Graphics g = bimage.createGraphics();
    //    // Paint the image onto the buffered image
    //    g.drawImage(image, 0, 0, null);
    //    g.dispose();
    //    return bimage;
    //}

}
