package com.examples.test.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author: cz
 * @Date: 2022/4/27
 * @Description:
 */
public class ZipUtil {

    public static void main(String[] args) {
        //带压缩文件集合
        ArrayList<String> srcFiles = new ArrayList<String>();
        srcFiles.add("D:\\workspace\\myfile\\书\\JAVA核心知识点整理.pdf");
        String destFile = "D:\\zip\\java.zip"; //压缩路径
        String passwd = "123"; //压缩包密码
        long fileSize = 65536; //分卷大小（当前为最小值）
        try {
            //1.压缩
            System.out.println(zipBySplit(srcFiles, destFile, passwd, fileSize));
            //2.解压
            String[] extractedFiles = unzipBySplit(destFile, "D:\\zip\\unzip", passwd);
            for (int i = 0; i < extractedFiles.length; i++) {
                System.out.println(extractedFiles[i]);
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param srcFiles 要压缩的文件绝对路径列表（支持多个文件的合并压缩）
     * @param destFile 要压缩的zip文件名
     * @param passwd   压缩密码
     * @param fileSize 分卷大小
     * @return 压缩文件路径（如分卷会返回以 "," 分隔的文件路径列表）
     */
    public static String zipBySplit(List<String> srcFiles, String destFile, String passwd, long fileSize) throws ZipException {
        if (CollectionUtil.isEmpty(srcFiles)) {
            return null;
        }
        //mkDirIfNotExist(destFile);
        FileUtil.mkdir(destFile);
        delFile(destFile);
        ZipFile zipFile = createZipFile(srcFiles, destFile, passwd, fileSize);
        return getSplitZipFiles(zipFile.getSplitZipFiles());
    }

    private static String getSplitZipFiles(List<String> zipList){
        if (CollectionUtil.isEmpty(zipList)) {
            return null;
        }
        replaceFileName(zipList);
        return String.join(",", zipList);
    }

    /**
     * 单独处理第10个包的文件名做特殊处理
     * @param zipList
     */
    private static void replaceFileName(List<String> zipList){
        String surFix = ".z010";
        String surFixReplace = ".z10";
        int size = zipList.size();
        for (int i = 0; i < size; i++) {
            String file = zipList.get(i).trim();
            int length = file.length();
            String surFixTmp = file.substring(length - 5, length);
            if (surFix.equals(surFixTmp)) {
                file = file.replace(surFix, surFixReplace);
            }
            zipList.set(i, file);
        }
    }

    private static ZipFile createZipFile(List<String> srcFiles, String destFile, String passwd, long fileSize) throws ZipException {
        ArrayList<File> filesToAdd = new ArrayList<File>();
        for(String srcFile:srcFiles){
            filesToAdd.add(new File(srcFile));
        }
        ZipParameters parameters = setZipParam(passwd, fileSize);
        ZipFile zipFile = new ZipFile(destFile);
        zipFile.createZipFile(filesToAdd, parameters, true, fileSize);
        return zipFile;
    }

    private static void delFile(String filePath){
        File tmpFile = new File(filePath);
        if (tmpFile.exists()) {
            tmpFile.delete();
        }
    }

    private static ZipParameters setZipParam(String passwd, long fileSize){
        ZipParameters params = new ZipParameters();
        //设置压缩密码
        setZipPwd(passwd, params);
        //设置压缩方式-默认
        params.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        //设置压缩级别-一般
        params.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        //设置默认分割大小为64KB
        //SplitLenth has to be greater than 65536 bytes
        if (fileSize == 0) {
            fileSize = 65536;
        }
        return params;
    }

    private static void setZipPwd(String passwd, ZipParameters parameters){
        if (StringUtils.isBlank(passwd)) {
            return;
        }
        parameters.setEncryptFiles(true);
        parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD);
        parameters.setPassword(passwd.toCharArray());
    }

    /**
     * 解压缩(支持分卷压缩解压)
     *
     * @param zipFilePath 指定的ZIP压缩文件 路径
     * @param dest        解压目录
     * @param passwd      ZIP文件的密码 。需要解压密码时必须传入，否则传入null即可
     * @return 解压后文件名数组
     * @throws ZipException 压缩文件有损坏或者解压缩失败抛出
     */
    public static String[] unzipBySplit(String zipFilePath, String dest, String passwd) throws ZipException {
        File zipFile = new File(zipFilePath);
        ZipFile zFile = new ZipFile(zipFile);
        //设置编码格式
        zFile.setFileNameCharset(CharsetUtil.UTF_8);
        if (!zFile.isValidZipFile()) {
            throw new ZipException("压缩文件不合法，可能被损坏!");
        }
        validFilePasswd(zFile, passwd);
        extractAllFiles(dest, zFile);
        return getExtractFiles(zFile);
    }

    private static void extractAllFiles(String dest, ZipFile zFile) throws ZipException {
        FileUtil.mkdir(dest);
        zFile.extractAll(dest);
    }

    private static String[] getExtractFiles(ZipFile zFile) throws ZipException {
        ArrayList<String> extractedFileList = new ArrayList<String>();
        List<FileHeader> headerList = zFile.getFileHeaders();
        if (CollectionUtil.isNotEmpty(headerList)) {
            for (FileHeader fileHeader : headerList) {
                if (!fileHeader.isDirectory()) {
                    extractedFileList.add(fileHeader.getFileName());
                }
            }
        }
        String[] extractedFiles = new String[extractedFileList.size()];
        extractedFileList.toArray(extractedFiles);
        return extractedFiles;
    }

    private static void validFilePasswd(ZipFile zFile, String passwd) throws ZipException {
        if (!zFile.isEncrypted()) {
            return;
        }
        if (StringUtils.isBlank(passwd)) {
            throw new ZipException("文件已加密，需要解压密码，解压密码不能为空！");
        }
        zFile.setPassword(passwd.toCharArray());
    }

}
