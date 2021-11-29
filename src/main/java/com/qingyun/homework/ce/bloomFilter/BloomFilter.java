package com.qingyun.homework.ce.bloomFilter;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.Objects;

/**
 * @description： 布隆过滤器
 * @author: 張青云
 * @create: 2021-11-29 14:08
 **/
public class BloomFilter<E> implements Serializable {
    /**
     * 位数组
     */
    private BitSet bitset;
    /**
     * bitset的大小
     */
    private int bitSetSize;
    /**
     * 每个元素使用的位数
     */
    private double bitsPerElement;
    /**
     * 要添加到布隆过滤器中的元素的预估数目
     */
    private int expectedNumberOfFilterElements;

    /**
     * 实际添加到Bloom过滤器的元素数
     */
    private int numberOfAddedElements;

    /**
     * 实际添加到Bloom过滤器中的不重复元素数目
     */
    private int uniqueElementsNumber;

    /**
     * 哈希函数的个数
     */
    private int k;

    static final Charset charset = StandardCharsets.UTF_8;
    static final String hashName = "MD5";
    static final MessageDigest digestFunction;
    static {
        MessageDigest tmp;
        try {
            tmp = java.security.MessageDigest.getInstance(hashName);
        } catch (NoSuchAlgorithmException e) {
            tmp = null;
        }
        digestFunction = tmp;
    }

    /**
     * bitSetSize = c*n；
     * @param c 每个元素使用的位数
     * @param n 预期要添加到布隆过滤器中的元素的个数
     * @param k 要使用的哈希函数的个数
     */
    public BloomFilter(double c, int n, int k) {
        this.expectedNumberOfFilterElements = n;
        this.k = k;
        this.bitsPerElement = c;
        this.bitSetSize = (int)Math.ceil(c * n);
        numberOfAddedElements = 0;
        this.bitset = new BitSet(bitSetSize);
    }

    public BloomFilter(int bitSetSize, int expectedNumberOElements) {
        this(bitSetSize / (double)expectedNumberOElements,
                expectedNumberOElements,
                (int) Math.round((bitSetSize / (double)expectedNumberOElements) * Math.log(2.0)));
    }

    /**
     * @param falsePositiveProbability 预估错误率
     * @param expectedNumberOfElements 预期要添加到布隆过滤器中的元素的个数
     */
    public BloomFilter(double falsePositiveProbability, int expectedNumberOfElements) {
        this(Math.ceil(-(Math.log(falsePositiveProbability) / Math.log(2))) / Math.log(2), // c = k / ln(2)
                expectedNumberOfElements,
                (int)Math.ceil(-(Math.log(falsePositiveProbability) / Math.log(2)))); // k = ceil(-log_2(false prob.))
    }

    /**
     * Generates a digest based on the contents of a String.
     *
     * @param val specifies the input data.
     * @param charset specifies the encoding of the input data.
     * @return digest as long.
     */
    public static int createHash(String val, Charset charset) {
        return createHash(val.getBytes(charset));
    }

    /**
     * 根据字符串的内容生成hash值
     */
    public static int createHash(String val) {
        return createHash(val, charset);
    }

    public static int createHash(byte[] data) {
        return createHashes(data, 1)[0];
    }

    /**
     * 使用指定数目的hash函数对指定内容去hash
     */
    public static int[] createHashes(byte[] data, int hashes) {
        int[] result = new int[hashes];

        int k = 0;
        byte salt = 0;
        while (k < hashes) {
            byte[] digest;
            synchronized (digestFunction) {
                digestFunction.update(salt);
                salt++;
                digest = digestFunction.digest(data);
            }

            for (int i = 0; i < digest.length/4 && k < hashes; i++) {
                int h = 0;
                for (int j = (i*4); j < (i*4)+4; j++) {
                    h <<= 8;
                    h |= ((int) digest[j]) & 0xFF;
                }
                result[k] = h;
                k++;
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BloomFilter<?> that = (BloomFilter<?>) o;
        return bitSetSize == that.bitSetSize &&
                Double.compare(that.bitsPerElement, bitsPerElement) == 0 &&
                expectedNumberOfFilterElements == that.expectedNumberOfFilterElements &&
                numberOfAddedElements == that.numberOfAddedElements &&
                k == that.k &&
                bitset.equals(that.bitset);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bitset, bitSetSize, bitsPerElement, expectedNumberOfFilterElements, numberOfAddedElements, k);
    }

    /**
     * 根据预期插入的元素数目和位数组的大小来计算错误率
     */
    public double expectedFalsePositiveProbability() {
        return getFalsePositiveProbability(expectedNumberOfFilterElements);
    }

    public double getFalsePositiveProbability(double numberOfElements) {
        // (1 - e^(-k * n / m)) ^ k
        return Math.pow((1 - Math.exp(-k * (double) numberOfElements
                / (double) bitSetSize)), k);

    }

    /**
     * 获取当前的错误率，概率是根据布隆过滤器的大小和当前添加到其中的元素数量计算的
     */
    public double getFalsePositiveProbability() {
        return getFalsePositiveProbability(numberOfAddedElements);
    }

    public int getK() {
        return k;
    }

    /**
     * 清空布隆过滤器
     */
    public void clear() {
        bitset.clear();
        numberOfAddedElements = 0;
    }

    /**
     * 向布隆过滤器中添加一个元素
     */
    public void add(E element) {
        add(element.toString().getBytes(charset));
    }

    public void add(byte[] bytes) {
        int[] hashes = createHashes(bytes, k);
        boolean unique = true;
        for (int hash : hashes) {
            int index = Math.abs(hash % bitSetSize);
            if (!getBit(index)) {
                unique = false;
                bitset.set(index, true);
            }
        }
        if (!unique) {
            uniqueElementsNumber++;
        }
        numberOfAddedElements++;
    }

    /**
     * 判断布隆过滤器中是否包含指定元素
     */
    public boolean contains(E element) {
        return contains(element.toString().getBytes(charset));
    }

    public boolean contains(byte[] bytes) {
        int[] hashes = createHashes(bytes, k);
        for (int hash : hashes) {
            if (!bitset.get(Math.abs(hash % bitSetSize))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取位数组的某一位的值
     */
    public boolean getBit(int bit) {
        return bitset.get(bit);
    }

    /**
     * 设置位数组指定的某一位
     */
    public void setBit(int bit, boolean value) {
        bitset.set(bit, value);
    }

    public BitSet getBitSet() {
        return bitset;
    }

    public int size() {
        return this.bitSetSize;
    }

    /**
     * 获取已经添加进来的元素的数目
     */
    public int getCount() {
        return this.numberOfAddedElements;
    }

    /**
     * 获取BloomFilter中不重复的元素的数目
     */
    public int getUniqueCount() {
        return this.uniqueElementsNumber;
    }
}
