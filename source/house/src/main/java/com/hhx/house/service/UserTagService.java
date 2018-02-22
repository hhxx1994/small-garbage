package com.hhx.house.service;

import com.clearspring.analytics.util.Lists;
import com.hhx.house.mapping.HouseInfoMapper;
import com.hhx.house.model.WordTuple;
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
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * @author hhx
 * @since 2018/2/22 21:09
 */
@Service
public class UserTagService {
    private List<String> tags = Arrays.asList("花园", "朝南", "通透", "采光", "电梯", "安静", "精装修", "高层", "复式", "地铁", "温馨");

    @Autowired
    private HouseInfoMapper houseInfoMapper;

    private List<WordTuple> list = Lists.newArrayList();

    @PostConstruct
    public void init() {
        Analyzer analyzer = null;
        Directory directory = null;

        try {
            String fieldName = "text";
            // 实例化IKAnalyzer分词器
            analyzer = new IKAnalyzer(true);


            IndexReader ireader = null;

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
            tags.forEach(tag -> {
                TermQuery query = new TermQuery(new Term("text", tag));
                TopDocs topDocs;
                try {
                    topDocs = indexSearcher.search(query, Integer.MAX_VALUE);
                    ScoreDoc[] scoreDocs = topDocs.scoreDocs;
                    //循环文档id,文档得分数组,获取单个文档id,根据文档id,获取文档对象
                    List<String> ids = Lists.newArrayList();
                    for (ScoreDoc scoreDoc : scoreDocs) {
                        //获取文档id
                        int docId = scoreDoc.doc;
                        //使用搜索核心对象,搜索文档:根据文档id唯一定义一个文档
                        Document doc = indexSearcher.doc(docId);
                        //获取文档数据
                        //获取文档域id
                        String id = doc.get("ID");
                        ids.add(id);

                    }
                    list.add(new WordTuple(tag, null, ids));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (analyzer != null) {
                analyzer.close();
            }
            if (directory != null) {
                try {
                    directory.close();
                } catch (IOException e) {
                    directory = null;
                }
            }
        }

    }

    public List<WordTuple> getList() {
        return list;
    }
}
