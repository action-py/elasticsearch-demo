package com.example.demoelasticsearch.controller.repository;

import com.alibaba.fastjson2.JSONObject;
import com.example.demoelasticsearch.dao.EsRepository;
import com.example.demoelasticsearch.po.Hotel;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目名称：demo-elasticsearch
 * 类 名 称：SearchController
 * 类 描 述：TODO
 * 创建时间：2022/10/10 11:38
 * 创 建 人：panyong
 */
@RequestMapping("/repository/search/")
@RestController
public class SearchController {
    @Autowired
    private EsRepository esRepository;
    @Autowired
    private ElasticsearchOperations operations;


    @GetMapping("title")
    public List<Hotel> list(String title){
        List<Hotel> hotels = esRepository.findByTitleLike(title);
        return hotels;
    }

    @GetMapping("search")
    public List<Hotel> serach(){
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(QueryBuilders.matchAllQuery());
        SearchHits<Hotel> search = operations.search(nativeSearchQuery, Hotel.class);
        List<Hotel> list = search.getSearchHits().stream().map(SearchHit<Hotel>::getContent).collect(Collectors.toList());
        return list;
    }
    @GetMapping("boolQuery")
    public SearchHits<Hotel>  boolQuery(){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must().add(QueryBuilders.matchQuery("title","心"));
        boolQueryBuilder.must().add(QueryBuilders.termQuery("city","厦门"));
        boolQueryBuilder.must().add(QueryBuilders.rangeQuery("price").gte(200).gte(500));
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(boolQueryBuilder);
        SearchHits<Hotel> search = operations.search(nativeSearchQuery, Hotel.class);
        return search;
    }

    @GetMapping("highLightQuery")
    public SearchHits<Hotel> highLightQuery(){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must().add(QueryBuilders.matchQuery("title","张三"));
        boolQueryBuilder.must().add(QueryBuilders.termQuery("city.keyword","上海"));
        //boolQueryBuilder.must().add(QueryBuilders.rangeQuery("price").gte(200).gte(1000));

        HighlightBuilder highlightCity = new HighlightBuilder().field("city").preTags("<font color='red'>").postTags("</font>");

        HighlightBuilder city = new HighlightBuilder().field("city");
        //FieldSortBuilder sortBuilder = new FieldSortBuilder("price").order(SortOrder.DESC);

        PageRequest pageable = PageRequest.of(0, 5);
        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withHighlightBuilder(city)
                //.withSorts(sortBuilder)
                .withPageable(pageable).build();
        SearchHits<Hotel> search = operations.search(build, Hotel.class);
        return search;
    }
}
