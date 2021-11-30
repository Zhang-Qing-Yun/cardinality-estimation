package com.qingyun.homework.ce.hyperLogLog;

import com.qingyun.homework.ce.utils.AnalyseUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @description：
 * @author: 張青云
 * @create: 2021-11-29 22:23
 **/
public class Main {
    public static void main(String[] args) throws IOException {
        String filename = "lib1_3000000.txt";
//        String filename = "lib_two4_3000000.txt";
        HyperLogLog hyperLogLog = new HyperLogLog(0.1);
        long start = System.currentTimeMillis();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        String line = null;
        while ((line = reader.readLine()) != null) {
            hyperLogLog.offer(line);
        }
        reader.close();
        long time = System.currentTimeMillis() - start;
        System.out.println("HLL结果：" + hyperLogLog.count());
        System.out.println("准确结果：" + AnalyseUtils.getUniqueDataCount(filename));
        System.out.println("耗时：" + time + "毫秒");
    }
}
