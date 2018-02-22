package com.hhx.house.service;

import com.google.common.collect.Lists;
import com.hhx.house.HouseApplication;
import com.hhx.house.mapping.HouseInfoMapper;
import com.hhx.house.model.WordTuple;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.DocIdSetIterator;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

/**
 * @author hhx
 * @since 2018/2/18 10:07
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HouseApplication.class)
@WebAppConfiguration
public class HouseInfoServiceTest {

    @Autowired
    private HouseInfoService houseInfoService;

    @Autowired
    private HouseInfoMapper houseInfoMapper;

    @Test
    public void houseInfoGroupByArea() {
        int size = houseInfoService.houseInfoGroupByArea().size();
        System.out.println(size);

    }

    @Test
    public void wordFreq() {
        StringBuilder string = new StringBuilder();
        houseInfoMapper.selectList(null).forEach(houseInfo -> {
            string.append(houseInfo.getTitle());
            string.append(" ");
        });
        String text = string.toString();
        String fieldName = "text";
        // 实例化IKAnalyzer分词器
        Analyzer analyzer = new IKAnalyzer(true);

        Directory directory = null;
        IndexWriter iwriter = null;
        IndexReader ireader = null;
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


            Fields fields = MultiFields.getFields(ireader);

            Terms terms = fields.terms(fieldName);

            List<WordTuple> list = Lists.newArrayList();
            TermsEnum iterator = terms.iterator(null);
            BytesRef bytesRef;
            while ((bytesRef = iterator.next()) != null) {
                String x = bytesRef.utf8ToString();
                DocsEnum docs = iterator.docs(null, null);
                int freq = 0;
                while ((docs.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
                    freq += docs.freq();
                }
                list.add(new WordTuple(x, freq));
            }

            list.stream().sorted(Comparator.comparing(WordTuple::getPoint).reversed()).limit(100).forEach(System
                    .out::println);


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
}