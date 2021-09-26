package com.tyg.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class CsvUtil {

    /**
     * 将excel表格转成csv格式
     *
     * @param src
     * @param target
     */
    public static List<String> excelToCsv(String src, String dstFilePath) {
        Workbook wb = readExcel(src);
        if (wb == null) {
            return null;
        }

        Sheet sheet = wb.getSheetAt(0);
        int rownum = sheet.getPhysicalNumberOfRows();
        List<List<String>> csvLines = new ArrayList<>();
        List<String> firstCols = new ArrayList<>();
        //只允许按表头的列数来遍历
        int colNum = sheet.getRow(0).getPhysicalNumberOfCells();

        boolean append = false;
        for (int i = 0; i < rownum; i++) {
            Row row = sheet.getRow(i);
            List<String> cols = new ArrayList<>();

            for (int j = 0; j < colNum; j++) {
                String cellVal = getCellFormatValue(row.getCell(j));
                if (i == 0) {
                    if (StringUtils.isNotBlank(cellVal)) {
                        firstCols.add(cellVal);
                    } else {
                        firstCols.add("column" + (j + 1));
                    }
                }
                cols.add(onDoubleQuotation(cellVal));
            }

            if (i != 0) {
                csvLines.add(cols);
            }

            if (csvLines.size() == 1000) {
                writeCsv(dstFilePath, csvLines, append);
                csvLines.clear();
                append = true;
            }
        }

        writeCsv(dstFilePath, csvLines, append);
        csvLines.clear();

        //--test
        //IntStream.range(0, 100_0000).forEach(i -> {
        //    List<String> cols = new ArrayList<>();
        //    IntStream.range(0, colNum).forEach(j -> {
        //        cols.add("1");
        //    });
        //    csvLines.add(cols);
        //
        //    if (csvLines.size() == 1000) {
        //        writeCsv(dstFilePath, csvLines, true);
        //        csvLines.clear();
        //    }
        //});
        //writeCsv(dstFilePath, csvLines, true);
        //csvLines.clear();
        //--test

        return firstCols;
    }

    public static String onDoubleQuotation(String s) {
        return s.contains(",") ? "\"" + s + "\"" : s;
    }

    public static String offDoubleQuotation(String s) {
        return s.contains("\"") ? s.replaceAll("\"", "") : s;
    }

    public static void writeCsv(String dstFilePath, List<List<String>> lines) {
        writeCsv(dstFilePath, lines, false);
    }

    public static void writeCsv(String dstFilePath, List<List<String>> lines, boolean append) {
        if (CollectionUtils.isEmpty(lines)) {
            return;
        }

        File saveCsv = new File(dstFilePath);
        try {
            if (!saveCsv.exists()) {
                if (!FileUtil.exist(FilenameUtils.getFullPath(saveCsv.getAbsolutePath()))) {
                    FileUtil.mkParentDirs(saveCsv);
                }
                saveCsv.createNewFile();
            }

            try (FileWriter writer = new FileWriter(saveCsv, append)) {
                for (List<String> cols : lines) {
                    writer.write(StringUtils.join(cols, ",") + "\n");
                }
            }
        } catch (IOException e) {
            log.error("写Csv文件失败：{}", e);
            return;
        }
    }

    @SneakyThrows(IOException.class)
    public static List<List<String>> readCsv(String filePath) {
        return readCsv(new FileReader(filePath));
    }

    @SneakyThrows(IOException.class)
    public static List<List<String>> readCsv(Reader r) {
        try (Reader r0 = r) {
            CsvReader reader = cn.hutool.core.text.csv.CsvUtil.getReader();
            CsvData csvData = reader.read(r0);
            if (CollectionUtils.isEmpty(csvData.getRows())) {
                return new ArrayList<>();
            }
            return csvData.getRows().stream().map(row -> {
                List<String> cols = new ArrayList<>();
                row.stream().forEach(col -> cols.add(onDoubleQuotation(col)));
                return cols;
            }).collect(Collectors.toList());
        }
    }

    public static Workbook readExcel(String filePath) {
        Workbook wb = null;
        if (filePath == null) {
            return null;
        }
        String ext = filePath.substring(filePath.lastIndexOf("."));
        try (InputStream is = new FileInputStream(filePath)) {
            if (".xls".equals(ext)) {
                return wb = new HSSFWorkbook(is);
            } else if (".xlsx".equals(ext)) {
                return wb = new XSSFWorkbook(is);
            } else {
                return wb = null;
            }
        } catch (Exception e) {
            log.error("读取Excel失败：{}", e);
        }
        return wb;
    }

    public static String getCellFormatValue(Cell cell) {
        if (cell != null) {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return String.valueOf(cell.getNumericCellValue()).replaceAll("\n", " ");
                case FORMULA:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return String.valueOf(cell.getDateCellValue()).replaceAll("\n", " ");
                    } else {
                        return String.valueOf(cell.getNumericCellValue()).replaceAll("\n", " ");
                    }
                case STRING:
                    return cell.getRichStringCellValue().getString().replaceAll("\n", " ");
                default:
                    return "";
            }
        } else {
            return "";
        }
    }

    public static void main(String[] args) throws Exception {
        //String src = "C:\\Users\\chenz\\Desktop\\测试组任务工作表--9月2日.xlsx";
        //String target = "C:\\Users\\chenz\\Desktop\\csv.csv";
        //excelToCsv(src, target);

        String readFilePath = "D:\\tmp\\123.csv";
        List<List<String>> csvData = readCsv(readFilePath);
        log.debug("csvData = {}", csvData);

    }
}
