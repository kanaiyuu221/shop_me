package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Goods;
import com.qf.service.ISearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {


    @Reference
    private ISearchService searchService;

    /**
     * 关键字查询
     */
    @RequestMapping("/query")
    public String search(String keyword,Model model){
        System.out.println("搜索关键字是>>>>> "+keyword);
        List<Goods> goodsList = searchService.queryByIndexed(keyword);
        model.addAttribute("goodslist",goodsList);

        return "searchlist";
    }
}