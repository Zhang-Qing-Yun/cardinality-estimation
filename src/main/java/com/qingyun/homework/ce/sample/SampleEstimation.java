package com.qingyun.homework.ce.sample;

import java.io.Serializable;
import java.util.*;

/**
 * @description： 基于水库抽样算法的基数估计
 * @author: 張青云
 * @create: 2021-11-29 18:37
 **/
public class SampleEstimation<E> implements Serializable {
    /**
     * 抽样大小的上阈
     */
    private static final int TOP_THRESHOLD = 200000;
    /**
     * 抽样大小的下阈
     */
    private static final int DOWN_THRESHOLD = 100;

    /**
     * 预估的数据大小
     */
    private int expectedNumber;

    /**
     * 抽样率
     */
    private double sampleRate;

    /**
     * 抽样大小
     */
    private int k;

    /**
     * 真实处理过的元素个数
     */
    private int realElementNum;

    /**
     * 样本
     */
    private List<E> sample;

    public SampleEstimation(int expectedNumber) {
        this(expectedNumber, 0.01);
    }

    public SampleEstimation(int expectedNumber, double sampleRate) {
        int n = (int) (expectedNumber * sampleRate);
        if (n <= DOWN_THRESHOLD) {
            k = expectedNumber;
        } else if (n >= TOP_THRESHOLD) {
            k = TOP_THRESHOLD;
        } else {
            k = n;
        }
        this.expectedNumber = expectedNumber;
        this.sampleRate = sampleRate;
        sample = new ArrayList<>(k);

    }

    /**
     * 添加一个元素，在添加的过程中完成抽样的过程
     */
    public void add(E element) {
        realElementNum++;
        if (realElementNum <= k) {
            sample.add(element);
        } else {
            // 生成[1,realElementNum]之间的随机数
            int p = new Random().nextInt(realElementNum) + 1;
            if (p < k) {
                // 将样本中任意一个元素替换为当前元素
                sample.set(new Random().nextInt(k), element);
            }
        }
    }

    /**
     * 通过样本来估计总体，获取总体中不重复元素个数的估计值
     */
    public int getUniqueElementCount() {
        // 使用HashSet来对样本进行去重工作
        Set<E> set = new HashSet<>();
        for(E one: sample) {
            set.add(one);
        }
        return (int) (((double)set.size() / k) * realElementNum);
    }

    /**
     * 获取抽样结果
     */
    public List<E> getSample() {
        return this.sample;
    }
}
