package com.qingyun.homework.ce.utils;

import java.io.*;
import java.util.*;

/**
 * @description：
 * @author: 張青云
 * @create: 2021-11-29 15:14
 **/
public final class DataUtils {
    public static void createDataSet(int dataSize, String filename) {
        //  保存全部不重复样本
        List<String> baseData = new ArrayList<>(6000);
        //  最终包含重复元素的数据集
        List<String> dataSet = new LinkedList<>();

        InputStream inputStream = null;
        BufferedReader reader = null;
        BufferedWriter writer = null;
        String line = null;
        try {
            //  读入所有的元素
            inputStream = new FileInputStream("lib_base.txt");
            reader = new BufferedReader(new InputStreamReader(inputStream));
            while ((line = reader.readLine()) != null) {
                baseData.add(line);
            }

            //  生成重复数据
            int baseSize = baseData.size();
            Random random = new Random();
            for(int i = 0; i < dataSize; i++) {
                int index = random.nextInt(baseSize);
                dataSet.add(baseData.get(index));
            }

            //  写入文件中
            writer = new BufferedWriter(new FileWriter(filename));
            Iterator<String> iterator = dataSet.iterator();
            while (iterator.hasNext()) {
                String one = iterator.next();
                writer.write(one);
                writer.newLine();
                iterator.remove();
            }
            writer.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
