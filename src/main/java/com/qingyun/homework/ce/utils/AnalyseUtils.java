package com.qingyun.homework.ce.utils;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @description： 分析结果的工具类
 * @author: 張青云
 * @create: 2021-11-26 00:25
 **/
public final class AnalyseUtils {
    /**
     * 精确获取数据集的基数
     */
    public static long getUniqueDataCount() {
        //  使用set来进行去重
        Set<String> set = new HashSet<>(6000);
        BufferedReader reader = null;
        String line = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream("lib_final.txt")));
            while ((line = reader.readLine()) != null) {
                set.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return set.size();
    }
}
