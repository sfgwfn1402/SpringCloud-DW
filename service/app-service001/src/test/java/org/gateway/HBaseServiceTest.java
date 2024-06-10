package org.gateway;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.MD5Hash;
import org.gateway.utils.HBaseService;
import org.gateway.utils.HashChoreWoker;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Slf4j
@Configuration
@RunWith(SpringRunner.class)
@SpringBootTest
public class HBaseServiceTest {

    @Autowired
    private HBaseService hBaseService;

    //分享链数据表
    private static final String SHARING_PRODUCT_CHAIN_TABLE = "sharing_product_chain";
    //列簇
    private static final byte[] SHARING_PRODUCT_CHAIN_FAMILY = Bytes.toBytes("spc1");

    //交通流量数据表
    private static final String TRAFFIC_FLOW_TABLE = "traffic_flow";
    //列簇
    private static final byte[] TRAFFIC_FLOW_FAMILY = Bytes.toBytes("tf1");

    private static final byte[] TEST_COLUMN = Bytes.toBytes("col");
    private static final byte[] TEST_ROW_KEY = Bytes.toBytes("row1");
    private static final byte[] TEST_VALUE = Bytes.toBytes("value1");

    @BeforeEach
    public void setUp() throws Exception {
        // 假设这里可以配置HBase连接（如果需要的话）
        // 实际上，这个配置通常在application.properties或application.yml中完成
        // 这里可以添加删除测试表的逻辑（如果表已存在）
    }

    //    @AfterEach
    @Test
    public void tearDown() throws Exception {
        // 清理测试数据，例如删除测试表（如果需要的话）
        // 注意：这应该在非生产环境中进行，以避免数据丢失
//        hBaseService.deleteTable(SHARING_PRODUCT_CHAIN_TABLE);
        hBaseService.deleteTable(TRAFFIC_FLOW_TABLE);
    }

    /**
     * 测试创建表 - 单分区
     */
    @Test
    public void testCreateTable() {
        List<String> columnFamilies = Arrays.asList("spc1", "spc2");
        assertTrue(hBaseService.creatTable(SHARING_PRODUCT_CHAIN_TABLE, columnFamilies));
        // 验证表是否存在的逻辑可以在这里添加（例如，使用Admin API）
    }

    /**
     * 商品链分享表创建
     * 测试预分区的逻辑，通过HashChoreWoker生成分区键，然后创建表
     *
     * @throws Exception
     */
    @Test
    public void testProductChainHashAndCreateTable() {
        HashChoreWoker worker = new HashChoreWoker(1_000_000, 3, 1);
        byte[][] splitKeys = worker.calcSplitKeys();
        System.out.println(splitKeys);
        assertTrue(hBaseService.createTableBySplitKeys(SHARING_PRODUCT_CHAIN_TABLE, Arrays.asList("spc1", "spc2"), splitKeys));
    }

    /**
     * 交通流量表创建
     * 测试预分区的逻辑，通过HashChoreWoker生成分区键，然后创建表
     *
     * @throws Exception
     */
    @Test
    public void testTrafficFlowHashAndCreateTable() {
        HashChoreWoker worker = new HashChoreWoker(1_000_000, 3, 2);
        byte[][] splitKeys = worker.calcSplitKeys();
        System.out.println(splitKeys);
        assertTrue(hBaseService.createTableBySplitKeys(TRAFFIC_FLOW_TABLE, Arrays.asList("tf1", "tf2"), splitKeys));
    }

    /**
     * 预分区的目的主要是在创建表的时候指定分区数，提前规划表有多个分区，
     * 以及每个分区的区间范围，这样在存储的时候rowkey按照分区的区间存储，
     * 可以避免region热点问题。
     */
    @Test
    public void testCreateTableBySplitKeys() {
        List<String> columnFamilies = Arrays.asList("cf1", "cf2");
        String[] splitKeys = new String[5]; // 可以添加分区键进行测试
        byte[][] splitKeys1 = hBaseService.getSplitKeys(splitKeys);
        assertTrue(hBaseService.createTableBySplitKeys(SHARING_PRODUCT_CHAIN_TABLE, columnFamilies, splitKeys1));
        // 验证表是否存在的逻辑可以在这里添加（例如，使用Admin API）  
    }

    /**
     * 测试获取所有表名
     */
    @Test
    public void testGetAllTableNames() {
        List<String> tableNames = hBaseService.getAllTableNames();
        assertFalse(tableNames.isEmpty(), "No tables found");
//        assertTrue(tableNames.contains(TEST_TABLE_NAME), "Test table not found in table list");
    }

    /**
     * 查询指定表所有数据
     */
    @Test
    public void testGetResultScanner() {
        // 假设表已经被填充了数据  
        Map<String, Map<String, String>> results = hBaseService.getResultScanner(SHARING_PRODUCT_CHAIN_TABLE);
        assertNotNull(results);
        // 根据实际数据添加断言
    }

    /**
     * 根据startRowKey和stopRowKey遍历查询指定表中的所有数据
     * 注意：startRowKey和stopRowKey必须是字符串类型，不能是字节数组类型
     */
    @Test
    public void testGetResultScannerWithStartStopRow() {
        // 需要有两个或以上的行来测试startRow和stopRow  
        Map<String, Map<String, String>> results = hBaseService.getResultScanner(SHARING_PRODUCT_CHAIN_TABLE, Bytes.toString("111_0".getBytes()), Bytes.toString("111_a".getBytes())); // 假设startRow和stopRow相同以获取单行
        assertNotNull(results);
        // 断言结果中应只包含指定行的数据  
    }

    // ... 类似地，为其他方法编写测试 ...  

    /**
     * 商品分享链数据实时存储
     * rowkey：MD5(product_id) + "_" + 分享第几人 + "_" + 用户ID)
     * 列簇：spc1
     * 列：user_id,product_id
     * 值：用户ID，产品ID
     */
    @Test
    public void testSharingProductChainPutData() {
        String rowKey = "";
        for (int i = 100; i < 200; i++) {
            //分享链：123456_3_789
            int product_id = i;//RandomUtil.randomInt(1, 100);
            for (int j = 1; j < 20; j++) {
                int user_id = j;//RandomUtil.randomInt(100, 999);
                rowKey = MD5Hash.getMD5AsHex((product_id + "").getBytes()) + "_" + j + "_" + RandomUtil.randomInt(100, 200);

                String[] columns = {Bytes.toString("user_id".getBytes()), Bytes.toString("product_id".getBytes())};
                String[] values = {Bytes.toString(("" + user_id).getBytes()), Bytes.toString(("" + product_id).getBytes())};

                hBaseService.putData(SHARING_PRODUCT_CHAIN_TABLE, rowKey, Bytes.toString(SHARING_PRODUCT_CHAIN_FAMILY), columns, values);
                log.info("put product sharing chain data table:{}, rowKey:{}", SHARING_PRODUCT_CHAIN_TABLE, rowKey);
            }
        }
        // 验证数据是否被正确写入的逻辑可以在这里添加（例如，通过getRow或getScanner）  
    }

    /**
     * 交通流量数据实时存储
     * rowkey：MD5(路口ID) + "_" + 方向 + "_" 车道号 + "_" +时间戳
     * 列簇：spc1
     * 列：crossing_id,direction,lane_id,flow,time
     * 值：用户ID，产品ID，速度，方向，时间
     */
    @Test
    public void testTrafficFlowDataPutData() {
        String rowKey = "";
        for (int i = 1; i < 25; i++) {//25个路口
            //路口
            int crossing_id = i;
            for (int j = 1; j < 4; j++) {//4个方向
                int direction = j;
                for (int k = 1; k < 5; k++) {//5个车道
                    int lane_id = k;
                    for (int l = 1; l < 1000; l++) {//1000个时间戳
                        long time = System.currentTimeMillis();
                        rowKey = MD5Hash.getMD5AsHex((crossing_id + "").getBytes()) + "_" + crossing_id + "_" + direction + "_" + lane_id + "_" + time;


                        String[] columns = {Bytes.toString("crossing_id".getBytes()), Bytes.toString("direction".getBytes()), Bytes.toString("lane_id".getBytes()), Bytes.toString("flow".getBytes()), Bytes.toString("time".getBytes())};
                        String[] values = {Bytes.toString(("" + crossing_id).getBytes()), Bytes.toString(("" + direction).getBytes()), Bytes.toString(("" + lane_id).getBytes()), Bytes.toString(("" + RandomUtil.randomInt(100, 200)).getBytes()), Bytes.toString(("" + time).getBytes())};

                        hBaseService.putData(TRAFFIC_FLOW_TABLE, rowKey, Bytes.toString(TRAFFIC_FLOW_FAMILY), columns, values);
                        log.info("put 交通流量数据表 table:{}, rowKey:{}", TRAFFIC_FLOW_TABLE, rowKey);
                    }
                }
            }
        }
        // 验证数据是否被正确写入的逻辑可以在这里添加（例如，通过getRow或getScanner）
    }

    @Test
    public void testDeleteColumn() {
        hBaseService.putData(SHARING_PRODUCT_CHAIN_TABLE, Bytes.toString(TEST_ROW_KEY), Bytes.toString(SHARING_PRODUCT_CHAIN_FAMILY), new String[]{Bytes.toString(TEST_COLUMN)}, new String[]{Bytes.toString(TEST_VALUE)});
        assertTrue(hBaseService.deleteColumn(SHARING_PRODUCT_CHAIN_TABLE, Bytes.toString(TEST_ROW_KEY), Bytes.toString(SHARING_PRODUCT_CHAIN_FAMILY), Bytes.toString(TEST_COLUMN)));
        // 验证列是否被删除的逻辑可以在这里添加（例如，通过getRow或getScanner）  
    }

    // ... 其他方法的测试代码 ...  
}