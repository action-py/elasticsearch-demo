package com.example.demoelasticsearch.config;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.http.HttpHeaders;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 项目名称：demo-elasticsearch
 * 类 名 称：RestClientConfig
 * 类 描 述：TODO
 * 创建时间：2022/10/17 10:18
 * 创 建 人：panyong
 */
@Configuration
public class RestClientConfig extends AbstractElasticsearchConfiguration {

    @Override
    public RestHighLevelClient elasticsearchClient() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("some-header", "on every request");
        final ClientConfiguration build = ClientConfiguration.builder()
                .connectedTo("121.196.20.126:31647")
                .withBasicAuth("elastic","Axz0ES2021")
                .withDefaultHeaders(httpHeaders)
                .withConnectTimeout(Duration.ofSeconds(5))
                .withSocketTimeout(Duration.ofSeconds(3))
                .withHeaders(()->{
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("currentTime", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    return headers;
                }).withClientConfigurer(RestClients.RestClientConfigurationCallback.from(clientBuilder ->{
                    //todo 设置回调处理
                    return clientBuilder;
                })).build();
        /*final BasicCredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();
        basicCredentialsProvider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials("elastic","Axz0ES2021"));*/
        return RestClients.create(build).rest();
    }
}
