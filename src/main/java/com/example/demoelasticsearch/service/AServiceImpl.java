package com.example.demoelasticsearch.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 项目名称：demo-elasticsearch
 * 类 名 称：AServiceImpl
 * 类 描 述：TODO
 * 创建时间：2022/10/18 11:21
 * 创 建 人：panyong
 */
@Service
public class AServiceImpl implements AService{

    private final BService bService;


    public AServiceImpl(BService bService) {
        this.bService = bService;
    }

    @Override
    public void apply() {
        bService.apply();
        System.err.println(this.getClass().getName());
    }
}
