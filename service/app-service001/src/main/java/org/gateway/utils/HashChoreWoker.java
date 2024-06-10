package org.gateway.utils;

import org.apache.hadoop.hbase.util.Bytes;

import java.util.Iterator;
import java.util.TreeSet;

public class HashChoreWoker {
    //随机取机数目  
    private int baseRecord;
    //rowkey生成器  
    private RowKeyGenerator rkGen;
    //每个分区的数量（取样时，由取样数目及region数相除所得的数量）
    private int splitKeysBase;
    //splitkeys个数  
    private int splitKeysNumber;
    //由抽样计算出来的splitkeys结果  
    private byte[][] splitKeys;

    /**
     * @param baseRecord     基准记录数目，比如100万
     * @param prepareRegions 预先准备的region数目，比如3个region
     */
    public HashChoreWoker(int baseRecord, int prepareRegions, int type) {
        this.baseRecord = baseRecord;
        //实例化rowkey生成器
        if (type == 1) {
            rkGen = new ProductSharingHashRowKeyGenerator();
        } else if (type == 2) {
            rkGen = new TrafficFlowHashRowKeyGenerator();
        }
        //计算splitkeys个数
        splitKeysNumber = prepareRegions - 1;
        //计算每个region的基准记录数,即每个region的splitkeys数目
        splitKeysBase = baseRecord / prepareRegions;
    }

    /**
     * 计算splitkeys，并返回splitkeys数组，每个元素代表一个region的splitkeys。（比如有2个key，代表3个region）
     * <p>
     * 比如：要分区为3个region，则每个region的splitkeys数目为100万/3=333333，
     * 则抽样100万次，抽样结果为：
     * 1. 第1个region的splitkeys为：rowkey1,rowkey2,rowkey3,rowkey4,rowkey5,rowkey6,rowkey7,rowkey8,rowkey9
     * ,rowkey10,rowkey11,rowkey12,rowkey13,rowkey14,rowkey15,rowkey16,rowkey17,rowkey18,rowkey19,rowkey20
     * ,rowkey21,rowkey22,rowkey23,rowkey24,rowkey25,rowkey26,rowkey27,rowkey28,rowkey29,rowkey30,rowkey31
     * ,rowkey32,rowkey33,rowkey34,rowkey35,rowkey36,rowkey37,rowkey38
     * 2. 第2个region的splitkeys为：rowkey39,rowkey40,rowkey41,rowkey42,rowkey43,rowkey44,rowkey45,rowkey46,rowkey47
     * ,rowkey48,rowkey49,rowkey50,rowkey51,rowkey52,rowkey53,rowkey54,rowkey55,rowkey56,rowkey57,rowkey58
     * ,rowkey59,rowkey60,rowkey61,rowkey62,rowkey63,rowkey64,rowkey65,rowkey66,rowkey67,rowkey68,rowkey69
     *
     * @return
     */
    public byte[][] calcSplitKeys() {
        splitKeys = new byte[splitKeysNumber][];

        //使用treeset保存抽样数据100万个rowkey，已排序过
        TreeSet<byte[]> rows = new TreeSet<byte[]>(Bytes.BYTES_COMPARATOR);

        //循环100万次，生成100万个rowkey，并将其加入treeset中
        for (int i = 0; i < baseRecord; i++) {
            rows.add(rkGen.nextId());
        }

        //循环每一个rowkey
        int pointer = 0;
        Iterator<byte[]> rowKeyIter = rows.iterator();
        int index = 0;
        while (rowKeyIter.hasNext()) {
            byte[] tempRow = rowKeyIter.next();
            rowKeyIter.remove();
            if ((pointer != 0) && (pointer % splitKeysBase == 0)) {//分3个区，100万
                if (index < splitKeysNumber) {
                    splitKeys[index] = tempRow;
                    index++;
                }
            }
            pointer++;
        }
        rows.clear();
        rows = null;
        return splitKeys;
    }
}  