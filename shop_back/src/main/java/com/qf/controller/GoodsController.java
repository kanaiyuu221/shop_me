package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController {


    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Reference
    private IGoodsService goodsService;


    private String fdfsPath="http://10.211.55.5/";


    @RequestMapping("/list")
    public String goodsManger(Model model){

        List<Goods> goods = goodsService.queryAll();
        System.out.println("goodlist>>>" + goods);
        model.addAttribute("goods", goods);

        model.addAttribute("fdfsPath", fdfsPath);

        return "goodslist";
    }

    @RequestMapping("/insert")
    public String insertGoods(Goods goods){

        goodsService.insert(goods);

        return "redirect:/goods/list";
    }


    @RequestMapping("/uploadimg")
    @ResponseBody
    public String uploadImg(MultipartFile file) throws Exception {

        //上传到FastDFS服务
        StorePath result = fastFileStorageClient.uploadImageAndCrtThumbImage(
                file.getInputStream(),
                file.getSize(),
                "png",
                null);

        //group1/M00/00/00/fhffghfgf.PNG
        System.out.println("上传到FastDFS中的文件路径：" + result.getFullPath());

        return "{\"imgpath\":\"" + result.getFullPath() + "\"}";
    }
}
