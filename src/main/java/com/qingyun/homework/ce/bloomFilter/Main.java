package com.qingyun.homework.ce.bloomFilter;

import com.qingyun.homework.ce.utils.AnalyseUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

/**
 * @description：
 * @author: 張青云
 * @create: 2021-11-29 22:13
 **/
public class Main {
    public static void main(String[] args) {
        String filename = "lib_two1_100000.txt";
        BloomFilter<String> bloomFilter = new BloomFilter<>(0.1, 100000);
        SparkConf conf = new SparkConf().setMaster("local").setAppName("createDataSet");
        JavaSparkContext sc = new JavaSparkContext(conf);
        JavaRDD<String> rdd = sc.textFile(filename);
        JavaRDD<String> filterRDD = rdd.filter(new Function<String, Boolean>() {
            @Override
            public Boolean call(String s) throws Exception {
                if (!bloomFilter.contains(s)) {
                    bloomFilter.add(s);
                    return true;
                }
                return false;
            }
        });
        System.out.println(filterRDD.count());
        System.out.println(AnalyseUtils.getUniqueDataCount(filename));
        sc.stop();
    }
}
