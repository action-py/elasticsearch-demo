package com.example.demoelasticsearch.controller.repository;

import com.example.demoelasticsearch.dao.EsRepository;
import com.example.demoelasticsearch.po.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目名称：demo-elasticsearch
 * 类 名 称：SaveController
 * 类 描 述：TODO
 * 创建时间：2022/10/13 11:28
 * 创 建 人：panyong
 */
@RequestMapping("/repository/save/")
@RestController
public class SaveController {
    @Autowired
    private EsRepository esRepository;

    @PostMapping("hotel")
    private Hotel saveHotel(@RequestBody Hotel hotel){
        esRepository.save(hotel);
        return hotel;
    }
}
