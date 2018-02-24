package com.hhx.house.test;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hhx
 * @since 2018/2/18 19:51
 */
public class MyTest {

    //155人关注/58次带看2个月以前发布房本满五年535万单价54498元/平米
    @Test
    public void getNum() {
        String str = "155人关注/58次带看2个月以前发布房本满五年535万单价54498元/平米";
        Pattern pattern = Pattern.compile("(\\d+)\\D+(\\d+)");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            //System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
        }

    }

    @Test
    public void test01() {
        double v = Double.parseDouble("121.25平米");
        System.out.println(v);
    }

    @Test
    public void test02() {
        String s = "高楼层(共13层)2003年建板楼/天通苑";
        Pattern compile = Pattern.compile("(\\d+)年");
        Matcher matcher = compile.matcher(s);
        if (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }

    @Test
    public void ikTest() throws Exception {
        // Lucene Document的域名
        String fieldName = "text";
        // 检索内容
        String text = "IK Analyzer Analyzer 是一个结合词典分词和文法分词的中文分词开源工具包 工具包。它使用了全新的正向迭代最细粒度切分算法。";

        // 实例化IKAnalyzer分词器
        Analyzer analyzer = new IKAnalyzer(true);

        Directory directory = null;
        IndexWriter iwriter = null;
        IndexReader ireader = null;
        IndexSearcher isearcher = null;
        try {
            // 建立内存索引对象
            directory = new RAMDirectory();

            // 配置IndexWriterConfig
            IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_43, analyzer);
            iwConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            iwriter = new IndexWriter(directory, iwConfig);
            // 写入索引
            Document doc = new Document();
            doc.add(new StringField("ID", "10000", Field.Store.YES));
            doc.add(new TextField(fieldName, text, Field.Store.YES));
            iwriter.addDocument(doc);
            iwriter.close();


            // 搜索过程**********************************
            // 实例化搜索器
            ireader = DirectoryReader.open(directory);
            isearcher = new IndexSearcher(ireader);
            IndexReader indexReader = isearcher.getIndexReader();


            Fields fields = MultiFields.getFields(ireader);

            Terms terms = fields.terms(fieldName);

            TermsEnum iterator = terms.iterator(null);
            BytesRef bytesRef;
            while ((bytesRef = iterator.next()) != null) {
                String x = bytesRef.utf8ToString();
                System.out.println(x);
                DocsEnum docs = iterator.docs(null, null);
                while ((docs.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
                    System.out.println(docs.freq());
                }
                System.out.println(iterator.docFreq());
            }


        } catch (CorruptIndexException e) {
            e.printStackTrace();
        } catch (LockObtainFailedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ireader != null) {
                try {
                    ireader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (directory != null) {
                try {
                    directory.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void streamTest() {
        Arrays.asList(1, 2, 3, 4).stream().map(item -> item * 100).forEach(System.out::println);
    }

    @Test
    public void testTime() {
        java.util.Date date = new java.util.Date();
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        System.out.println(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

}
