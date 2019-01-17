package com.qf.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Goods implements Serializable {
    private int id;
    private String title;
    private String ginfo;
    private int gcount;
    private int tid;//分类表的外键
    private double allprice;
    private double price;
    private String gimage;
}
