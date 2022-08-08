package com.example.demo.elastic;

import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;

public class ElasticSearchModule {

	
	private String hostname = "localhost";
	private int port = 9200;
	private String index = "inca-winlog-";
	
    public void insertJson(String message) throws IOException {
    	
        IndexRequest indexRequest = new IndexRequest(index);

        indexRequest.source(message,XContentType.JSON);
        IndexResponse response = this.getClient().index(indexRequest, RequestOptions.DEFAULT);
        
        String result_data = response.getId();
        System.out.println(result_data);
    }
       
	
    public RestHighLevelClient getClient() throws UnknownHostException {
    	
    	HttpHost host = new HttpHost(hostname,port);
		RestClientBuilder restClientBuilder = RestClient.builder(host);
		
		RestHighLevelClient client = new RestHighLevelClient(restClientBuilder);
        return client;
    }
    
	
}
