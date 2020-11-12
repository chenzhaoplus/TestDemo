package com.examples.test.bloomfilter;

import cn.hutool.bloomfilter.BitMapBloomFilter;

/**
 * @Author: cz
 * @Date: 2020/10/2
 * @Description: Bloom过滤器可以广泛应用于判断集合中是否存在某个元素的大量数据场景，比如黑名单、爬虫访问记录。
 */
public class BloomFilterTest {

    public static void main(String[] args) {
        // 初始化 注意 构造方法的参数大小10 决定了布隆过滤器BitMap的大小
        BitMapBloomFilter filter = new BitMapBloomFilter(10);
        filter.add("123");
        filter.add("abc");
        filter.add("ddd");
        // 查找
        boolean abc = filter.contains("abc1");
        System.out.println(abc);
    }

}
