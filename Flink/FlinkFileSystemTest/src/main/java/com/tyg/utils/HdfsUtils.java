package com.tyg.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.tyg.model.FsConnConfig;
import com.tyg.model.FsResponseEntity;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: cz
 * @Date: 2021/9/23
 * @Description:
 */
@Slf4j
public class HdfsUtils {

    private HdfsUtils() {
    }

    @SneakyThrows({IOException.class, URISyntaxException.class})
    public static FileSystem getFileSystem(FsConnConfig connConf) {
        return FileSystem.get(new URI(concatHdfsUrl(connConf.getIp(), connConf.getPort())), new Configuration());
    }

    @SneakyThrows(IOException.class)
    public static String create(FsConnConfig connConf, String fileContent) {
        Validate.isTrue(!exist(connConf, connConf.getRelativePath()) && !isFile(connConf, connConf.getRelativePath()), "要创建的文件已存在！");
        FileSystem fileSystem = getFileSystem(connConf);
        FSDataOutputStream fos = fileSystem.create(new Path(connConf.getRelativePath()), false);
        fos.write(fileContent.getBytes());
        fos.flush();
        IOUtils.closeStream(fos);
        return connConf.getRelativePath();
    }

    public static Map<String, Object> uploadCsvByExcel(FsConnConfig connConf, String srcFilePath, String dstDirPath) {
        String dstFileName = FilenameUtils.getBaseName(srcFilePath) + ".csv";
        String tmpFilePath = "/tmp/" + dstFileName;
        List<String> columns = CsvUtil.excelToCsv(srcFilePath, tmpFilePath);
        String csvFilePath = uploadFile(connConf, true, tmpFilePath, dstDirPath);
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("path", csvFilePath);
        resultMap.put("columns", columns);
        return resultMap;
    }

    @SneakyThrows(IOException.class)
    public static String downFileByCsv(FsConnConfig connConf, String srcDir, String dstDir) {
        Validate.isTrue(exist(connConf, srcDir), "文件所在目录不存在！");
        List<FsResponseEntity> files = listFiles(connConf, srcDir).stream()
                .filter(entity -> StrUtil.equals("file", entity.getType()))
                .collect(Collectors.toList());
        Validate.notEmpty(files, "文件所在目录没有文件可以下载");

        FileSystem fileSystem = getFileSystem(connConf);

        File file = new File(dstDir);
        if (!file.exists()) {
            file.mkdirs();
        }

        List<List<String>> csvDatas = new ArrayList<>();
        for (FsResponseEntity f : files) {
            try (FSDataInputStream fis = fileSystem.open(new Path(srcDir + "/" + f.getName()))) {
                List<List<String>> lines = CsvUtil.readCsv(new InputStreamReader(fis));
                if (!CollectionUtils.isEmpty(lines)) {
                    csvDatas.addAll(lines);
                }
            }
        }

        if (CollectionUtils.isEmpty(csvDatas)) {
            return "";
        }

        if (csvDatas.size() <= 65535) {
            ExcelWriter writer = ExcelUtil.getWriter(dstDir + "/out.xls");
            writer.write(csvDatas);
            writer.close();
        } else {
            CsvUtil.writeCsv(dstDir + "/out.csv", csvDatas);
        }

        return "";
    }

    @SneakyThrows(IOException.class)
    public static boolean delResources(FsConnConfig connConf, String path, Boolean recursive) {
        Validate.isTrue(exist(connConf, path), "要删除的文件或目录不存在！");
        FileSystem fileSystem = getFileSystem(connConf);
        return fileSystem.delete(new Path(path), recursive);
    }

    public static boolean delFiles(FsConnConfig connConf, String dirPath, Boolean recursive) {
        Validate.isTrue(exist(connConf, dirPath), "目录不存在！");
        FileSystem fileSystem = getFileSystem(connConf);
        List<FsResponseEntity> files = listFiles(connConf, dirPath).stream()
                .filter(entity -> StrUtil.equals("file", entity.getType()))
                .collect(Collectors.toList());
        return delFiles(connConf, dirPath, recursive, files);
    }

    public static boolean delFiles(FsConnConfig connConf, String dirPath, Boolean recursive, List<FsResponseEntity> files) {
        if (CollectionUtils.isEmpty(files)) {
            return true;
        }
        try {
            FileSystem fileSystem = getFileSystem(connConf);
            boolean b = true;
            for (FsResponseEntity file : files) {
                b = b && fileSystem.delete(new Path(dirPath + "/" + file.getName()), recursive);
            }
            return b;
        } catch (IOException e) {
            return false;
        }
    }

    @SneakyThrows(IOException.class)
    public static String downFile(FileSystem fileSystem, String srcFilePath, String dstDir) {
        String dstFilePath = dstDir + "/" + FilenameUtils.getName(srcFilePath);
        fileSystem.copyToLocalFile(new Path(srcFilePath), new Path(dstFilePath));
        return dstFilePath;
    }

    public static String downFile(FsConnConfig connConf, String srcFilePath, String dstDir) {
        FileSystem fileSystem = getFileSystem(connConf);
        return downFile(fileSystem, srcFilePath, dstDir);
    }

    public static String uploadFile(FsConnConfig connConf, String srcFilePath, String dstDirPath) {
        return uploadFile(connConf, false, srcFilePath, dstDirPath);
    }

    @SneakyThrows(IOException.class)
    public static String uploadFile(FsConnConfig connConf, boolean delSrc, String srcFilePath, String dstDirPath) {
        FileSystem fileSystem = getFileSystem(connConf);
        if (!exist(connConf, dstDirPath)) {
            fileSystem.mkdirs(new Path(dstDirPath));
        }

        String fileName = FilenameUtils.getName(srcFilePath);
        String dstFileName = dstDirPath + "/" + fileName;
        //if (exist(connConf, dstFileName)) {
        //    throw new RuntimeException("目标目录下已存在同名文件！");
        //}

        fileSystem.copyFromLocalFile(delSrc, new Path(srcFilePath), new Path(dstDirPath));
        return dstFileName;
    }

    @SneakyThrows(IOException.class)
    public static boolean exist(FsConnConfig connConf, String path) {
        FileSystem fileSystem = getFileSystem(connConf);
        return fileSystem.exists(new Path(path));
    }

    @SneakyThrows(IOException.class)
    public static boolean isFile(FsConnConfig connConf, String path) {
        FileSystem fileSystem = getFileSystem(connConf);
        return fileSystem.isFile(new Path(path));
    }

    @SneakyThrows(IOException.class)
    public static boolean dirEmpty(FsConnConfig connConf, String dirPath) {
        Validate.isTrue(exist(connConf, dirPath), "要查看的目录不存在！");
        FileSystem fileSystem = getFileSystem(connConf);
        FileStatus[] fileStatuses = fileSystem.listStatus(new Path(dirPath));
        return Stream.of(fileStatuses).anyMatch(fileStatus -> fileStatus.isFile());
    }

    public static List<FsResponseEntity> listFiles(FsConnConfig connConf, String dirPath) {
        return listFiles(connConf, dirPath, false);
    }

    @SneakyThrows(IOException.class)
    public static List<FsResponseEntity> listFiles(FsConnConfig connConf, String dirPath, boolean onlyDir) {
        Validate.isTrue(exist(connConf, dirPath), "要查看的目录不存在！");

        List<FsResponseEntity> result = new ArrayList<>();
        FileSystem fileSystem = getFileSystem(connConf);
        FileStatus[] fileStatuses = fileSystem.listStatus(new Path(dirPath));

        if (ArrayUtils.isNotEmpty(fileStatuses)) {
            for (FileStatus fstus : fileStatuses) {
                FsResponseEntity entity = new FsResponseEntity(
                        FilenameUtils.getName(fstus.getPath().toString()),
                        fstus.isDirectory() ? "folder" : "file");
                if (!fstus.isDirectory()) {
                    entity.setSize(fstus.getLen());
                }
                entity.setUpdateTime(fstus.getModificationTime());
                entity.setPermission(fstus.getPermission().toString());
                if (!onlyDir || fstus.isDirectory()) {
                    result.add(entity);
                }
            }
        }

        return result;
    }

    private static String concatHdfsUrl(String ip, String port) {
        return concatHdfsUrl(ip, port, null);
    }

    private static String concatHdfsUrl(String ip, String port, String relativePath) {
        String relative = StringUtils.isBlank(relativePath) ? "" : relativePath;
        return String.format("hdfs://%s:%s%s", ip, port, relative);
    }

    private static String concatHdfsUrl(FsConnConfig connConf) {
        return concatHdfsUrl(connConf.getIp(), connConf.getPort(), connConf.getRelativePath());
    }

    public static void main(String[] args) throws Exception {
        String fileName = "/test/user3.csv";
        String fileContent = "我们,你们\n1,2\n3,4\n5,6";
        //String srcExcel = "C:\\Users\\chenz\\Desktop\\测试组任务工作表--9月2日.xlsx";
        String srcExcel = "C:\\Users\\chenz\\Desktop\\123.xlsx";
        String dstDir = "/csv/input";
        String modelId = "45s6d4fs6f16ertwfw";
        String downDirPath = "/csv/output";
        String toDirPath = "C:\\Users\\chenz\\Desktop";
        FsConnConfig connConf = FsConnConfig.builder()
                .ip("172.16.4.102")
                .port("8020")
                .relativePath(fileName)
                .build();

        //log.debug("create = {}", create(connConf, fileContent));
        //log.debug("uploadFile = {}", uploadFile(connConf, srcExcel, dstDir));
        //log.debug("uploadExcelSaveCsv = {}", uploadCsvByExcel(connConf, srcExcel, dstDir + "/" + modelId));
        //log.debug("downFile = {}", downFileByCsv(connConf, downDirPath + "/" + modelId, toDirPath));
    }

}
