package com.example.demoelasticsearch.controller.client;

import com.alibaba.fastjson2.JSONObject;
import com.example.demoelasticsearch.po.Hotel;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.index.reindex.UpdateByQueryRequest;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.range.DateRangeAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名称：demo-elasticsearch
 * 类 名 称：SearchController
 * 类 描 述：TODO
 * 创建时间：2022/10/17 15:12
 * 创 建 人：panyong
 */
@RequestMapping("/client/hotel/")
@RestController
public class HotelController {

    @Autowired
    private RestHighLevelClient client;

    //插入
    @GetMapping("singleSave")
    public List<Hotel> singleSave() throws IOException {
        IndexRequest indexRequest = new IndexRequest("demo_hotel");
        Hotel hotel = new Hotel();
        hotel.setCity("上海");
        hotel.setPrice("150");
        hotel.setTitle("张三");
        String json = JSONObject.toJSONString(hotel);
        indexRequest.source(json, XContentType.JSON);
        IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
        DocWriteResponse.Result result = response.getResult();
        return null;
    }
    //批量插入
    @GetMapping("batchSave")
    public BulkResponse batchSave() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();

        List<IndexRequest> requests = new ArrayList<>();
        IndexRequest indexRequest = new IndexRequest("demo_hotel");
        Hotel hotel = new Hotel();
        hotel.setCity("上海");
        hotel.setPrice("666");
        hotel.setTitle("张三111");
        String s = JSONObject.toJSONString(hotel);
        indexRequest.source(s,XContentType.JSON);
        requests.add(indexRequest);
        IndexRequest indexRequest1 = new IndexRequest("demo_hotel");
        Hotel hotel1 = new Hotel();
        hotel1.setCity("北京");
        hotel1.setPrice("150");
        hotel1.setTitle("张三3333");
        String s1 = JSONObject.toJSONString(hotel1);
        indexRequest1.source(s1,XContentType.JSON);
        requests.add(indexRequest1);

        for (IndexRequest request : requests) {
            bulkRequest.add(request);
        }
        bulkRequest.timeout(TimeValue.timeValueSeconds(3));
        BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        return bulk;
    }

    @GetMapping("/esTest")
    public SearchResponse esTest() throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //sourceBuilder.from(0);
        sourceBuilder.size(10);

        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("evn", "test1");
        TermQueryBuilder termUnitId = QueryBuilders.termQuery("unitId", "9305");

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(termQueryBuilder);
        boolQueryBuilder.must(termUnitId);
        sourceBuilder.query(boolQueryBuilder);

        /*TermsAggregationBuilder biddingPattern = AggregationBuilders.terms("biddingPattern_agg").field("biddingPattern");
        sourceBuilder.aggregation(biddingPattern);*/
        SearchRequest searchRequest = new SearchRequest("bidding_simple_list");
        searchRequest.source(sourceBuilder);
        SearchResponse search = client.search(searchRequest,RequestOptions.DEFAULT);

        return search;
    }


    /**
     * 组合查询
     * 前十条 【2018年1月26日早八点到晚八点】标题中包涵【费德勒】的【体育】新闻的【标题】。
     * {
     *     "from":"0",
     *     "size":"10",
     *     "_source":["title"],
     *     "query":{
     *         "bool":{
     *             "must":{
     *                 "match":{
     *                     "title":"费德勒" }
     *             },
     *             "must":{
     *                 "term":{"tag.keyword":"体育"}
     *             },
     *             "must":{
     *                 "range":{
     *                     "publishTime":{ "gte":"2018-01-26T08:00:00Z", "lte":"2018-01-26T20:00:00Z" } }
     *             }
     *         }
     *     }
     *
     * }
     */
    @GetMapping("boolQuery")
    public SearchHits boolQuery() throws IOException {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0);
        sourceBuilder.size(10);
        sourceBuilder.fetchSource(new String[] {"title"},new String[] {""});//指定返回的字段

        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("tag.keyword", "体育");
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", "费德勒");
        RangeQueryBuilder publishTime = QueryBuilders.rangeQuery("publishTime");
        publishTime.gte("2018-01-26T08:00:00Z");
        publishTime.lte("2018-01-26T20:00:00Z");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(termQueryBuilder);
        boolQueryBuilder.must(matchQueryBuilder);
        boolQueryBuilder.must(publishTime);
        sourceBuilder.query(boolQueryBuilder);
        SearchRequest searchRequest = new SearchRequest("demo");
        searchRequest.source(sourceBuilder);
        SearchResponse search = client.search(searchRequest,RequestOptions.DEFAULT);

        System.out.println(search);
        return search.getHits();
    }

    /**
     * 修改数据
     * @throws IOException
     */
    @GetMapping("modify")
    public void modify() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("demo","1hjh6YMBiGu3PXaWCuNI");
        String [] tag = new String[] {"运动","网球"};
        Map<String,Object> map = new HashMap<String,Object>(){{put("tag",tag);}};
        updateRequest.doc(map);
        /*数据不存在时执行插入操作
        IndexRequest indexRequest = new IndexRequest("demo");
        indexRequest.source("{tag:[体育]}");
        updateRequest.upsert(indexRequest);*/
        UpdateResponse update = client.update(updateRequest,RequestOptions.DEFAULT);
        System.out.println(update.getResult());
    }

    public void batchModify() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        for (int i = 0; i < 3; i++) {
            UpdateRequest updateRequest = new UpdateRequest("demo","1hjh6YMBiGu3PXaWCuNI");
            String [] tag = new String[] {"运动","网球"};
            Map<String,Object> map = new HashMap<String,Object>(){{put("tag",tag);}};
            updateRequest.doc(map);
            bulkRequest.add(updateRequest);
        }
        client.bulk(bulkRequest,RequestOptions.DEFAULT);
    }

    public void updateCityByQuery() throws IOException {
        UpdateByQueryRequest request = new UpdateByQueryRequest("demo");
        request.setQuery(QueryBuilders.termQuery("city","上海"));
        String city = "厦门";
        request.setScript(new Script("ctx._source['city']='"+city+"';"));
        client.updateByQuery(request, RequestOptions.DEFAULT);
    }

    @GetMapping("delete")
    public void delete() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("demo","1hjh6YMBiGu3PXaWCuNI");
        client.delete(deleteRequest,RequestOptions.DEFAULT);
    }

    public void batchDelete() throws IOException {
        BulkRequest request = new BulkRequest();
        for (int i = 0; i < 3; i++) {
            DeleteRequest deleteRequest = new DeleteRequest("demo","1hjh6YMBiGu3PXaWCuNI");
            request.add(deleteRequest);
        }
        client.bulk(request,RequestOptions.DEFAULT);
    }

    public void deleteByParam() throws IOException {
        DeleteByQueryRequest request = new DeleteByQueryRequest("demo");
        request.setQuery(QueryBuilders.termQuery("tag.keyword","篮球"));
        client.deleteByQuery(request, RequestOptions.DEFAULT);
    }
}
