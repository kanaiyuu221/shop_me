package com.qf.service;

import com.qf.entity.Goods;

import java.util.List;

public interface ISearchService {
    List<Goods> queryByIndexed(String keyword);
    int insertIndexed(Goods goods);
}
