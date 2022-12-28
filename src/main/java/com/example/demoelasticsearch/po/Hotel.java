package com.example.demoelasticsearch.po;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * 项目名称：demo-elasticsearch
 * 类 名 称：Hotel
 * 类 描 述：TODO
 * 创建时间：2022/10/10 11:39
 * 创 建 人：panyong
 */
@Document(indexName = "demo_hotel")
@Data
public class Hotel {
    @Id
    private String id;
    private String title;
    private String city;
    private String price;

}
