package org.gateway.utils;

import cn.hutool.core.util.RandomUtil;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.MD5Hash;

import java.util.Random;

/**
 * This class generates a row key based on the current time and a random number.
 * The row key is generated by concatenating the MD5 hash of the current time and the random number,
 * and the current ID.
 */
public class ProductSharingHashRowKeyGenerator implements RowKeyGenerator {
    private long currentId = 1;
    private long currentTime = System.currentTimeMillis();
    private Random random = new Random();

    /**
     * 商品分享链生成规则
     *
     * @return
     */
    public byte[] nextId() {
        try {
//            currentTime += random.nextInt(1000);
//            byte[] lowT = Bytes.copy(Bytes.toBytes(currentTime), 4, 4);
//            byte[] lowU = Bytes.copy(Bytes.toBytes(currentId), 4, 4);
//            return Bytes.add(MD5Hash.getMD5AsHex(Bytes.add(lowU, lowT)).substring(0, 8).getBytes(), Bytes.toBytes(currentId));

            String product_id = RandomUtil.randomInt(100, 200) + "";
            String user_id = RandomUtil.randomInt(100, 200) + "";
            byte[] add = Bytes.add(Bytes.add((currentId + "").getBytes(), "_".getBytes()), user_id.getBytes());
            return Bytes.add(Bytes.add(MD5Hash.getMD5AsHex((product_id + "").getBytes()).getBytes(), "_".getBytes()), add);
//            return Bytes.add(MD5Hash.getMD5AsHex(Bytes.add(product_id.getBytes(), Bytes.toBytes(currentId))).getBytes(), user_id.getBytes());
        } finally {
            currentId++;
        }
    }

}