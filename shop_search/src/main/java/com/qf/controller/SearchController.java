package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.service.ISearchService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/search")
public class SearchController {


    @Reference
    private ISearchService searchService;

    /**
     * 关键字查询
     */
    @RequestMapping("/query")
    public String search(String keyword){
        System.out.println("搜索关键字是>>>>> "+keyword);


        return "searchlist";
    }
}