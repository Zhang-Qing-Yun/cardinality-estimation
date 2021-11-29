package com.qingyun.homework.ce;

import com.qingyun.homework.ce.bloomFilter.BloomFilter;
import com.qingyun.homework.ce.hyperLogLog.HyperLogLog;
import com.qingyun.homework.ce.utils.DataUtils;

import java.io.*;

/**
 * @author: 張青云
 * @create: 2021-11-29 14:53
 **/
public class Main {
    public static void main(String[] args) throws IOException {
        DataUtils.createDataSet1();
    }

    private static long hll() throws IOException {
        HyperLogLog hyperLogLog = new HyperLogLog(0.01);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("lib_final.txt")));
        String line = null;
        while ((line = reader.readLine()) != null) {
            hyperLogLog.offer(line);
        }
        reader.close();
        return hyperLogLog.cardinality();
    }

    private static int bf() throws IOException {
        int count = 0;
        BloomFilter<String> bloomFilter = new BloomFilter<>(0.01, 3000000);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("lib_final.txt")));
        String line = null;
        while ((line = reader.readLine()) != null) {
            if (!bloomFilter.contains(line)) {
                bloomFilter.add(line);
                count++;
            }
        }
        reader.close();
        System.out.println(bloomFilter.size());
        System.out.println(bloomFilter.getK());
        return count;
    }
}
