package com.qingyun.homework.ce;

import com.qingyun.homework.ce.bloomFilter.BloomFilter;
import com.qingyun.homework.ce.hyperLogLog.HyperLogLog;
import com.qingyun.homework.ce.sample.SampleEstimation;
import com.qingyun.homework.ce.utils.DataUtils;

import java.io.*;

/**
 * @author: 張青云
 * @create: 2021-11-29 14:53
 **/
public class Main {
    public static void main(String[] args) throws IOException {
//        DataUtils.createDataSet(50000, "lib5_50000.txt");

        String filename = "lib5_50000.txt";
        System.out.println(sample(filename, 0.1, 50000));
        System.out.println(hll(filename, 0.01));
        System.out.println(bf(filename, 0.01, 50000));
    }

    private static long hll(String filename, double expectedErrorRate) throws IOException {
        HyperLogLog hyperLogLog = new HyperLogLog(expectedErrorRate);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        String line = null;
        while ((line = reader.readLine()) != null) {
            hyperLogLog.offer(line);
        }
        reader.close();
        return hyperLogLog.cardinality();
    }

    private static int bf(String filename, double expectedErrorRate, int expectedNumber) throws IOException {
        BloomFilter<String> bloomFilter = new BloomFilter<>(expectedErrorRate, expectedNumber);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        String line = null;
        while ((line = reader.readLine()) != null) {
            bloomFilter.add(line);
        }
        reader.close();
        return bloomFilter.getUniqueCount();
    }

    private static int sample(String filename, double expectedErrorRate, int expectedNumber) throws IOException {
        SampleEstimation<String> sample = new SampleEstimation<>(expectedNumber, expectedErrorRate);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        String line = null;
        while ((line = reader.readLine()) != null) {
            sample.add(line);
        }
        reader.close();
        return sample.getUniqueElementCount();
    }
}
