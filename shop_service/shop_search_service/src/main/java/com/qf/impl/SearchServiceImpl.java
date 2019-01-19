package com.qf.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.entity.Goods;
import com.qf.service.ISearchService;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;

@Service
public class SearchServiceImpl implements ISearchService {

    @Autowired
    private SolrClient solrClient;

    @Override
    public List<Goods> queryByIndexed(String keyword) {
        return null;
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
