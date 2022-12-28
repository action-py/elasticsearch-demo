package com.example.demoelasticsearch.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * 项目名称：demo-elasticsearch
 * 类 名 称：BServiceImpl
 * 类 描 述：TODO
 * 创建时间：2022/10/18 11:22
 * 创 建 人：panyong
 */
@Service
public class BServiceImpl implements BService{


    private final AService aService;

    public BServiceImpl(@Lazy AService aService) {
        this.aService = aService;
    }

    @Override
    public void apply() {
        aService.apply();
        System.err.println(this.getClass().getName());
    }
}
