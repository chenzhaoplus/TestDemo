package com.examples.test.util;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import sun.nio.ch.IOUtil;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import java.util.Objects;

public class ImageUtil {

    public static void main(String[] args) throws IOException {

        String srcFile = "D:\\zip\\pic\\1.jpg";
        String zipFile = "D:\\zip\\pic\\2.jpg";
        compressImg(srcFile, 50 * 1024, zipFile, 0.8);

    }

    public static void compressImg(String srcFile, long maxSize, String zipFile, double scale) throws IOException {
        compressImg(new File(srcFile), maxSize, new File(zipFile), scale);
    }

    /**
     * 将图片压缩到指定大小以内
     *
     * @param srcImgData 源图片数据
     * @param maxSize    目的图片大小
     * @return
     * @date 2020年11月18日
     */
    public static void compressImg(File imageFile, long maxSize, File zipFile, double scale) throws IOException {
        //long timeStart = System.currentTimeMillis();
        zipFile.delete();
        //byte[] data = getByteByPic(imageFile);
        byte[] imgData = null;
        try(FileInputStream fis = new FileInputStream(imageFile)){
            byte[] data = IoUtil.readBytes(fis);
            imgData = Arrays.copyOf(data, data.length);
        }
        do {
            try {
                imgData = compress(imgData, scale);
            } catch (IOException e) {
                throw new IllegalStateException("压缩图片过程中出错,请及时联系管理员!", e);
            }
        } while (imgData.length > maxSize);
        byteToImage(imgData, zipFile);
        //long timeEnd = System.currentTimeMillis();
        //System.out.println("耗时：" + (timeEnd - timeStart));
    }

    /**
     * 获取图片文件字节
     *
     * @param imageFile
     * @return
     * @throws IOException
     * @date 2020年11月18日
     */
    public static byte[] getByteByPic(File imageFile) throws IOException {
        byte[] data = new byte[0];
        try (InputStream inStream = new FileInputStream(imageFile)) {
            try(BufferedInputStream bis = new BufferedInputStream(inStream)){
                BufferedImage bm = ImageIO.read(bis);
                try(ByteArrayOutputStream bos = new ByteArrayOutputStream()){
                    String imageUrl = imageFile.getAbsolutePath();
                    String type = imageUrl.substring(imageUrl.length() - 3);
                    ImageIO.write(bm, type, bos);
                    bos.flush();
                    data = bos.toByteArray();
                    return data;
                }
            }
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * 按照宽高比例压缩
     *
     * @param srcImgData 待压缩图片输入流
     * @param scale      压缩刻度
     * @return
     * @throws IOException
     * @date 2020年11月18日
     */
    public static byte[] compress(byte[] srcImgData, double scale) throws IOException {
        try(ByteArrayInputStream input = new ByteArrayInputStream(srcImgData)){
            BufferedImage bi = ImageIO.read(input);
            int width = (int) (bi.getWidth() * scale); // 源图宽度
            int height = (int) (bi.getHeight() * scale); // 源图高度
            Image image = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.setColor(Color.RED);
            g.drawImage(image, 0, 0, null); // 绘制处理后的图
            g.dispose();
            try (ByteArrayOutputStream bOut = new ByteArrayOutputStream()){
                ImageIO.write(tag, "JPEG", bOut);
                return bOut.toByteArray();
            } catch (IOException e) {
                throw e;
            }
        }
    }

    /**
     * byte数组转图片
     *
     * @param data
     * @param path
     * @date 2020年11月18日
     */
    public static void byteToImage(byte[] data, File zipFile) {
        if (data.length < 3){
            return;
        }
        try (FileImageOutputStream imageOutput = new FileImageOutputStream(zipFile)){
            imageOutput.write(data, 0, data.length);
        } catch (Exception ex) {
        }
    }

}
