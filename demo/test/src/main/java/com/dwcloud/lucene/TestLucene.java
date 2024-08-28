package com.dwcloud.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestLucene {
    static Directory directory = new RAMDirectory(); // 使用内存索引，也可以使用其他存储方式

    public static void main(String[] args) throws Exception {
        createIndex();
        search();

    }

    public static void search() throws IOException, ParseException {
        // 定义索引存储目录
//        Directory directory = FSDirectory.open(Paths.get("/tmp/lucene"));

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

        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        builder.add(new TermQuery(new Term("carA", "1202")), BooleanClause.Occur.MUST);
        builder.add(new TermQuery(new Term("point", "1302")), BooleanClause.Occur.MUST);

        TopDocs topDocs = indexSearcher.search(builder.build(), 100);
        // 进行搜索
//        TopDocs topDocs = indexSearcher.search(query, 10);
        // 获取搜索结果
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            // 获取文档
            Document document = indexSearcher.doc(scoreDoc.doc);
            // 获取文件名
            String car = document.get("carA");
            // 获取内容
            String content = document.get("point");
            // 获取文档得分
            float score = scoreDoc.score;
            System.out.println(car + " " + content + " " + score);
        }
        // 关闭reader
        indexReader.close();

    }

    public static void createIndex() throws IOException {
        // 定义索引存储目录
//        Directory directory = FSDirectory.open(Paths.get("/tmp/lucene"));
        // 定义分析器
        KeywordAnalyzer analyzer = new KeywordAnalyzer();
        // 配置索引写入器
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        // 清空索引库
        indexWriter.deleteAll();
        // 创建文档
        Document document = new Document();

        // 1. 添加字段
//        document.add(new TextField("fileName", "jmeter.log", Field.Store.YES));
//        File file = new File("/Users/duwei/jmeter.log");
//        document.add(new TextField("content", new String(Files.readAllBytes(file.toPath())), Field.Store.NO));

        // 2. 添加文档字段
        for (int i = 0; i < 10; i++) {
            document.add(new TextField("content", "Java程序设计入门到精通", Field.Store.YES));
            document.add(new TextField("carA", "120" + i, Field.Store.YES));
            document.add(new TextField("point", "130" + i, Field.Store.YES));
            // 添加文档到索引库
            indexWriter.addDocument(document);
            // 提交索引
            indexWriter.commit();
            // 在createIndex方法结束后，可以立即添加一个验证查询的代码段
            searchExact("120" + i, "130" + i); // 临时添加，验证索引
        }


        // 关闭writer
        indexWriter.close();
        printAllDocuments();
    }


    // 添加方法
    private static void searchExact(String carValue, String pointValue) throws IOException {
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
    public static void printAllDocuments() throws IOException {
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
}