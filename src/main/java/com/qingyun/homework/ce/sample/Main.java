package com.qingyun.homework.ce.sample;

import com.qingyun.homework.ce.utils.AnalyseUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @description：
 * @author: 張青云
 * @create: 2021-11-29 22:45
 **/
public class Main {
    public static void main(String[] args) throws IOException {
//        String filename = "lib3_100000.txt";
        String filename = "lib_two6_10000.txt";
        SampleEstimation<String> sample = new SampleEstimation<>(10000, 0.3);
        long start = System.currentTimeMillis();
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        String line = null;
        while ((line = reader.readLine()) != null) {
            sample.add(line);
        }
        reader.close();
        long time = System.currentTimeMillis() - start;
        System.out.println("准确结果：" + AnalyseUtils.getUniqueDataCount(filename));
        System.out.println("抽样法：" + sample.getUniqueElementCount());
        System.out.println("耗时：" + time + "毫秒");
    }
}
