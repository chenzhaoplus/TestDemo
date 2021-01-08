package com.examples.test.util;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: cz
 * @Date: 2020/8/12
 * @Description:
 */
public class PageUtils {

    public static void main(String[] args) {
        List<String> keyList = new ArrayList<>();
        keyList.add("1");
        keyList.add("2");
        keyList.add("3");
        keyList.add("4");
        keyList.add("5");
        keyList.add("6");
        keyList.add("7");
        int pageSize = 7;
        int totalElements = keyList.size();
        Long totalPages = PageUtils.getTotalPages(pageSize, Long.valueOf(totalElements));
        for (int pageNum = 0; pageNum < totalPages; pageNum++) {
            int fromIndex = PageUtils.getFromIndex(pageNum, pageSize);
            int toIndex = PageUtils.getToIndex(pageNum, pageSize, Long.valueOf(totalElements));
            System.out.println("fromIndex = " + fromIndex);
            System.out.println("toIndex = " + toIndex);
            List<String> subList = keyList.subList(fromIndex, toIndex);
            System.out.println("subList = " + JSON.toJSONString(subList));
        }
    }

    public static Long getTotalPages(Integer pageSize, Long totalElements) {
        Long totalPages = 0L;
        if (pageSize <= 0 || totalElements <= 0) {
            return totalPages;
        }
        if (totalElements <= pageSize) {
            totalPages = 1L;
        } else if (totalElements > pageSize) {
            if (totalElements % pageSize == 0) {
                totalPages = totalElements / pageSize;
            } else {
                totalPages = totalElements / pageSize + 1;
            }
        }
        return totalPages;
    }

    /**
     * 根据页号，一页大小，算出 List 的 fromIndex
     *
     * @param pageNum  从 0 开始
     * @param pageSize
     * @return int fromIndex
     */
    public static int getFromIndex(Integer pageNum, Integer pageSize) {
        int fromIndex = pageNum * pageSize;
        return fromIndex;
    }

    /**
     * 根据页号，一页大小，总数，算出 List 的 toIndex
     *
     * @param pageNum       从 0 开始
     * @param pageSize
     * @param totalElements
     * @return int toIndex
     */
    public static int getToIndex(Integer pageNum, Integer pageSize, Long totalElements) {
        int toIndex = (pageNum + 1) * pageSize;
        if (pageSize == 0 || totalElements == 0) {
            toIndex = 0;
        } else if (pageSize > totalElements) {
            toIndex = totalElements.intValue();
        } else if (pageSize <= totalElements) {
            Long totalPages = getTotalPages(pageSize, totalElements);
            if (pageNum == (totalPages - 1)) {
                toIndex = totalElements.intValue();
            } else {
                toIndex = (pageNum + 1) * pageSize;
            }
        }
        return toIndex;
    }

    /**
     * 获取list的分页subList
     *
     * @param pageNum       从0开始
     * @param pageSize
     * @param totalElements
     * @param list
     * @param <E>
     * @return List<E>
     */
    public static <E> List<E> getPageList(Integer pageNum, Integer pageSize, Long totalElements, List<E> list) {
        int fromIndex = PageUtils.getFromIndex(pageNum, pageSize);
        int toIndex = PageUtils.getToIndex(pageNum, pageSize, totalElements);
        List<E> subList = list.subList(fromIndex, toIndex);
        return subList;
    }

}
