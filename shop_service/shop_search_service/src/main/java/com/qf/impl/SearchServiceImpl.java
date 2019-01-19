package com.qf.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.entity.Goods;
import com.qf.service.ISearchService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements ISearchService {

    @Autowired
    private SolrClient solrClient;

    @Override
    public List<Goods> queryByIndexed(String keyword) {
        //查询索引库返回预览结果 不查询数据库
        SolrQuery solrQuery=new SolrQuery();
        if (keyword == null){
            solrQuery.setQuery("*:*");
        }else {
            solrQuery.setQuery("gtitle:"+keyword+"|| ginfo:"+keyword);
        }
        List<Goods> goods=new ArrayList<>();
        try {
            QueryResponse queryResponse=solrClient.query(solrQuery);
            SolrDocumentList solr=queryResponse.getResults();
            for (SolrDocument entries : solr) {
                int id= Integer.parseInt((String) entries.get("id"));
                String gtitle = (String) entries.get("gtitle");
                String ginfo = (String) entries.get("ginfo");
                float gprice = (float) entries.get("gprice");
                int gcount = (int) entries.get("gcount");
                String gimage = (String) entries.get("gimage");
                Goods goods1=new Goods(id,gtitle,ginfo,gcount,0,0,gprice,gimage);
                goods.add(goods1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return goods;
    }

    /**
     * 同步添加索引库
     */
    @Override
    public int insertIndexed(Goods goods) {
        System.out.println("索引库将添加的商品id："+goods.getId());
        SolrInputDocument solrInput=new SolrInputDocument();
        solrInput.setField("id",goods.getId());
        solrInput.setField("gtitle",goods.getTitle());
        solrInput.setField("ginfo",goods.getGinfo());
        solrInput.setField("gimage",goods.getGimage());
        solrInput.setField("gcount",goods.getGcount());
        solrInput.setField("gprice",goods.getPrice());


        try {
            solrClient.add(solrInput);
            solrClient.commit();
            return 1;
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
