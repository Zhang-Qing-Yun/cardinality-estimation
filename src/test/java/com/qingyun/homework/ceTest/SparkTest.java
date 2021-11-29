package com.qingyun.homework.ceTest;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @description：
 * @author: 張青云
 * @create: 2021-11-29 20:21
 **/
public class SparkTest implements Serializable {
    private transient SparkConf conf;
    private transient JavaSparkContext sc;

    @Before
    public void init() {
        conf = new SparkConf().setMaster("local").setAppName("createDataSet");
        sc = new JavaSparkContext(conf);
    }

    @Test
    public void createDataSet() {
        JavaRDD<String> rdd = sc.textFile("E:\\Mystudy\\大三上课内学习\\大数据\\count-estimation\\lib_base.txt");
        JavaRDD<String> rdd1 = rdd.flatMap(new FlatMapFunction<String, String>() {
            @Override
            public Iterator<String> call(String s) throws Exception {
                int length = s.length();
                ArrayList<String> list = new ArrayList<>();
                for (int i = 1; i <= length; i++) {
                    for (int left = 0; left <= length - i; left++) {
                        int right = left + i;
                        list.add(s.substring(left, right));
                    }
                }
                return list.iterator();
            }
        });
        rdd1.saveAsTextFile("E:\\Mystudy\\大三上课内学习\\大数据\\count-estimation\\lib_test_out");
    }

    @After
    public void after() {
        sc.stop();
    }
}
