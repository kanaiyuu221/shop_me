package com.qf.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goods implements Serializable {

    @TableId( type = IdType.AUTO )
    private int id;
    private String title;
    private String ginfo;
    private int gcount;
    private int tid;//分类表的外键
    private double allprice;
    private double price;
    private String gimage;
}
