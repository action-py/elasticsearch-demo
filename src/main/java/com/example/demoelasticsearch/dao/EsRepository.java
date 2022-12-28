package com.example.demoelasticsearch.dao;

import com.example.demoelasticsearch.po.Hotel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EsRepository extends CrudRepository<Hotel,String> {
    //生成一个模糊查询
    List<Hotel> findByTitleLike(String title);
}
