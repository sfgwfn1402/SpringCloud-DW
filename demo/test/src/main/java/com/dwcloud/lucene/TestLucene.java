package com.dwcloud.lucene;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;


public class TestLucene {
    //索引文件地址
    private static final String INDEX_PATH = "d:/data/lucene";

    public static void main(String[] args) throws Exception {
        TestLucene testLucene = new TestLucene();
        // 写测试日志
//        testLucene.writeLog();

        // 创建索引并初始化数据
//        testLucene.createIndex();

        // 搜索内容 京A1000999","point":"116.309983,39.989983
//        String plateNumber = "京A1000999";//1到10
        String plateNumber = "";//1到10
        String point = "116.309983,39.989983";//1到10
//        String point = "";//1到10
        long current = DateUtil.current(false);
        VehiclePoint vp = testLucene.search(plateNumber, point);
        System.out.println("查询耗时:" + (DateUtil.current(false) - current) + "毫秒, 结果: " + JSON.toJSONString(vp));

    }

    /**
     * 搜索内容
     *
     * @param plateNumber 车牌号
     * @param point       经纬度
     */
    public VehiclePoint search(String plateNumber, String point) {
        VehiclePoint vp = new VehiclePoint();
        try {
            if (StringUtils.isEmpty(plateNumber) && StringUtils.isEmpty(point)) {
                return vp;
            }
            // 定义索引存储目录
            Directory directory = FSDirectory.open(Paths.get(INDEX_PATH));

            // 打开索引
            IndexReader indexReader = DirectoryReader.open(directory);
            // 创建搜索器
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);
            // 定义分析器
//        Analyzer analyzer = new StandardAnalyzer();
//        // 关键词解析器
//        QueryParser queryParser = new QueryParser("车牌A", analyzer);
//        // 解析查询关键词
//        String keywords = "120";
//        Query query = queryParser.parse(keywords);
//        // 进行搜索
//        TopDocs topDocs = indexSearcher.search(query, 10);

            // 多条件搜索
            BooleanQuery.Builder builder = new BooleanQuery.Builder();
            // 车牌号
            if (!StringUtils.isEmpty(plateNumber)) {
                builder.add(new TermQuery(new Term(VehiclePointFieldName.PLATE_NUMBER, plateNumber)), BooleanClause.Occur.MUST);
            }
            // 坐标点
            if (!StringUtils.isEmpty(point)) {
                builder.add(new TermQuery(new Term(VehiclePointFieldName.POINT, point)), BooleanClause.Occur.MUST);
            }

            TopDocs topDocs = indexSearcher.search(builder.build(), 10);

            // 获取搜索结果
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            for (ScoreDoc scoreDoc : scoreDocs) {
                // 获取文档
                Document document = indexSearcher.doc(scoreDoc.doc);
                setVo(scoreDoc, document, vp);
            }
            // 关闭reader
            indexReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vp;
    }

    private void setVo(ScoreDoc scoreDoc, Document document, VehiclePoint vp) {
        // 车牌号
        String plateNumber = document.get(VehiclePointFieldName.PLATE_NUMBER);
        // 坐标点
        String point = document.get(VehiclePointFieldName.POINT);
        // 时间
        String time = document.get(VehiclePointFieldName.TIME);
        // 速度
        String speed = document.get(VehiclePointFieldName.SPEED);
        // 其它
        String other = document.get(VehiclePointFieldName.OTHER);
        // 获取文档得分
        float score = scoreDoc.score;
        vp.setPlateNumber(plateNumber);
        vp.setPoint(point);
        vp.setTime(time);
        vp.setSpeed(speed);
        vp.setOther(other);
        vp.setScore(score + "");
    }

    /**
     * 创建索引
     */
    public void createIndex() {
        try {
            // 1. 定义索引存储目录
            Directory directory = FSDirectory.open(Paths.get(INDEX_PATH));
            // 2. 定义分析器
            KeywordAnalyzer analyzer = new KeywordAnalyzer();
            // 3. 配置索引写入器
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
            IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
            // 清空索引库
            indexWriter.deleteAll();
            // 创建文档
//        Document document = new Document();
            // 1. 添加字段
//        document.add(new TextField("fileName", "jmeter.log", Field.Store.YES));
//        File file = new File("/Users/duwei/jmeter.log");
//        document.add(new TextField("content", new String(Files.readAllBytes(file.toPath())), Field.Store.NO));

            // 2. 添加文档字段
//        for (int i = 0; i < 10; i++) {
//            document = new Document();
//            document.add(new TextField("content", "Java程序设计入门到精通", Field.Store.YES));
//            document.add(new TextField("carA", "120" + i, Field.Store.YES));
//            document.add(new TextField("point", "130" + i, Field.Store.YES));
//            // 添加文档到索引库
//            indexWriter.addDocument(document);
//            // 提交索引
//            indexWriter.commit();
            // 在createIndex方法结束后，可以立即添加一个验证查询的代码段
//            searchExact("120" + i, "130" + i); // 临时添加，验证索引
//        }
            // 4. 添加数据
            addDocument(indexWriter);

            // 5. 关闭writer
            indexWriter.close();
//            printAllDocuments();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 解析文件并添加数据
     */
    public void addDocument(IndexWriter indexWriter) {
        try {
            //1. 解析文件内容
            List<VehiclePoint> vehiclePoints = Lists.newArrayList();
            String filePath = "d:/data/lucene/test.log";
            BufferedReader reader = null;

            try {
                reader = new BufferedReader(new FileReader(filePath));
                String line;
                int n = 0;
                while ((line = reader.readLine()) != null) {
                    VehiclePoint vp = JSON.parseObject(line, VehiclePoint.class);
                    vehiclePoints.add(vp);
                    n++;
                    if (n % 1000000 == 0){
                        // 1000000条数据写入索引库
                        dataInsertIndex(indexWriter, vehiclePoints);
                        // 清空集合
                        vehiclePoints.clear();
                        n = 0;
                    }
                }
                if (vehiclePoints.size() > 0){
                    dataInsertIndex(indexWriter, vehiclePoints);
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
            System.out.println("添加数据到索引库完成！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dataInsertIndex(IndexWriter indexWriter, List<VehiclePoint> vehiclePoints) throws IOException {
        int count = vehiclePoints.size();

        // 设置文档并添加到索引库
        List<Document> documents = Lists.newArrayList();
        for (int i = 0; i < count; i++) {
            Document document = new Document();
            //车牌号
            document.add(new TextField(VehiclePointFieldName.PLATE_NUMBER, vehiclePoints.get(i).getPlateNumber(), Field.Store.YES));
            //经纬度
            document.add(new TextField(VehiclePointFieldName.POINT, vehiclePoints.get(i).getPoint(), Field.Store.YES));
            //时间
            document.add(new TextField(VehiclePointFieldName.TIME, vehiclePoints.get(i).getTime(), Field.Store.YES));
            //速度
            document.add(new TextField(VehiclePointFieldName.SPEED, vehiclePoints.get(i).getSpeed(), Field.Store.YES));
            //其它
            document.add(new TextField(VehiclePointFieldName.OTHER, vehiclePoints.get(i).getOther(), Field.Store.YES));
            documents.add(document);
            System.out.println("添加数据到索引库进度：" + i + "/" + count);
        }
        // 添加文档到索引库
        indexWriter.addDocuments(documents);
        // 提交索引
        indexWriter.commit();
    }

    /**
     * 解析文件内容
     */
    private void parseFilecontents(List<VehiclePoint> vehiclePoint) {
        String filePath = "d:/data/lucene/test.log";
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                VehiclePoint vp = JSON.parseObject(line, VehiclePoint.class);
                vehiclePoint.add(vp);
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
    }

    /**
     * 写测试日志
     */
    public void writeLog() {
        BufferedWriter writer = null;
        try {
            File file = new File("d:/data/lucene/test.log");
            writer = new BufferedWriter(new FileWriter(file));
            for (int i = 0; i < 1000; i++) {
                for (int j = 0; j < 10000; j++) {
                    VehiclePoint vp = new VehiclePoint();
                    //车牌号
                    vp.setPlateNumber("京A1000" + i);
                    //经纬度
                    vp.setPoint("116.30" + j + ",39.98" + j);
                    //时间
                    vp.setTime(DateUtil.current(false) + "");
                    //速度
                    vp.setSpeed("60" + j);
                    //其它
                    vp.setOther("其它信息" + j);
                    //输出文件
                    writeFile(JSON.toJSONString(vp), writer);
                    System.out.println("写日志进度：" + i + "," + j);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 写一个方法，将内容输出到指定的文件中
     */
    public void writeFile(String content, BufferedWriter writer) {
        try {
            writer.write(content);
            writer.newLine(); // 添加换行符
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 搜索内容
     *
     * @param carValue
     * @param pointValue
     * @throws IOException
     */
    private static void searchExact(String carValue, String pointValue) throws IOException {
        Directory directory = FSDirectory.open(Paths.get(INDEX_PATH));
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        // 使用精确TermQuery进行验证
        Query queryCar = new TermQuery(new Term("carA", carValue));
        Query queryPoint = new TermQuery(new Term("point", pointValue));
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        builder.add(queryCar, BooleanClause.Occur.SHOULD);
        builder.add(queryPoint, BooleanClause.Occur.MUST);
        TopDocs topDocs = indexSearcher.search(builder.build(), 100);

        // 输出验证结果
        System.out.println("验证查询结果:");
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println(document.get("carA") + " " + document.get("point") + " " + scoreDoc.score);
        }

        indexReader.close();
    }

    /**
     * 打印所有文档
     *
     * @throws IOException
     */
    public void printAllDocuments() throws IOException {
        Directory directory = FSDirectory.open(Paths.get(INDEX_PATH));

        // 打开索引
        IndexReader indexReader = DirectoryReader.open(directory);
        // 创建搜索器
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        // 获取索引中的所有文档
        int numDocs = indexReader.numDocs();
        for (int i = 0; i < numDocs; i++) {
            Document document = indexSearcher.doc(i);
            System.out.println("Document " + i + ":");
            System.out.println("Content: " + document.get("content"));
            System.out.println("carA: " + document.get("carA"));
            System.out.println("point: " + document.get("point"));
            System.out.println();
        }

        // 关闭reader
        indexReader.close();
    }


    /**
     * 定义车辆坐标点字段名称常量值
     */
    class VehiclePointFieldName {
        public static final String PLATE_NUMBER = "plateNumber";
        public static final String POINT = "point";
        public static final String TIME = "time";
        public static final String SPEED = "speed";
        public static final String OTHER = "other";
    }
}