package com.hhx.house.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.hhx.house.HouseApplication;
import com.hhx.house.entity.HouseInfo;
import com.hhx.house.mapping.HouseInfoMapper;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.IOException;
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
        String fieldName = "text";
        // 实例化IKAnalyzer分词器
        Analyzer analyzer = new IKAnalyzer(true);

        Directory directory = null;
        IndexReader ireader = null;
        try {
            // 建立内存索引对象
            directory = new RAMDirectory();

            // 配置IndexWriterConfig
            IndexWriterConfig iwConfig = new IndexWriterConfig(Version.LUCENE_43, analyzer);
            iwConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            IndexWriter iwriter = new IndexWriter(directory, iwConfig);
            List<Document> docsx = Lists.newArrayList();
            // 写入索引
            houseInfoMapper.selectList(null).forEach(houseInfo -> {
                Document doc = new Document();
                doc.add(new StringField("ID", houseInfo.getHouseid(), Field.Store.YES));
                doc.add(new TextField(fieldName, houseInfo.getTitle(), Field.Store.YES));
                docsx.add(doc);
            });
            for (Document d : docsx) {
                iwriter.addDocument(d);
            }
            iwriter.close();


            // 搜索过程**********************************
            // 实例化搜索器
            ireader = DirectoryReader.open(directory);
            IndexSearcher indexSearcher = new IndexSearcher(ireader);
            TermQuery query = new TermQuery(new Term("text","花园"));
            TopDocs topDocs = indexSearcher.search(query, Integer.MAX_VALUE);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            //循环文档id,文档得分数组,获取单个文档id,根据文档id,获取文档对象
            for (ScoreDoc scoreDoc : scoreDocs) {
                //获取文档id
                int docId = scoreDoc.doc;

                //使用搜索核心对象,搜索文档:根据文档id唯一定义一个文档
                Document doc = indexSearcher.doc(docId);
                //获取文档数据
                //获取文档域id
                String id = doc.get("ID");
                System.out.println("文档域ID:"+id);

            }


//            Fields fields = MultiFields.getFields(ireader);
//
//            Terms terms = fields.terms(fieldName);
//
//            List<WordTuple> list = Lists.newArrayList();
//            TermsEnum iterator = terms.iterator(null);
//            BytesRef bytesRef;
//            while ((bytesRef = iterator.next()) != null) {
//                String x = bytesRef.utf8ToString();
//                DocsEnum docs = iterator.docs(null, null);
//                int freq = 0;
//                List<String> ids = Lists.newArrayList();
//                while ((docs.nextDoc()) != DocIdSetIterator.NO_MORE_DOCS) {
//                    int id = docs.docID();
//                    Document document = ireader.document(id);
//                    String idx = document.getField("ID").toString();
//                    ids.add(idx);
//                    freq += docs.freq();
//                }
//                list.add(new WordTuple(x, freq, ids));
//            }
//
//            list.stream()
//                    .filter(wordTuple -> wordTuple.getKey().length() >= 2)
//                    .sorted(Comparator.comparing(WordTuple::getPoint).reversed())
//                    .limit(30)
//                    .forEach(System.out::println);


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
    public void pageTest(){
        Page<HouseInfo> page = new Page<>(1,10);
        int size = houseInfoMapper.houseInfoList(page).size();
        System.out.println(size);
    }
}