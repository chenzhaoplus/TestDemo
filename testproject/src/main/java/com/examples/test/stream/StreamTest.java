package com.examples.test.stream;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: cz
 * @Date: 2020/9/22
 * @Description:
 */
public class StreamTest {

    public static void main(String[] args) {
        listToMapTest();
        filterTest();
    }

    /**
     * java lambda 方式 list 过滤掉空元素
     */
    private static void filterTest() {
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
        System.out.println(filtered);
    }

    /**
     * java lambda 方式 list转map
     */
    private static void listToMapTest() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("lookupCode", i + "code");
            map.put("lookupValue", i + "value");
            list.add(map);
        }
        Map<String, Object> map3 = list.stream().collect(Collectors.toMap(item -> item.get("lookupCode").toString(),
                item -> item.get("lookupValue"),
                (oldVal, currVal) -> currVal, HashMap::new));
        System.out.println(map3);
    }

}
