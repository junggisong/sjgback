package com.example.demo.elastic.service;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.elastic.Indices;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class elasticsearchService {
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final Logger log = LoggerFactory.getLogger(elasticsearchService.class);
	
	private final RestHighLevelClient client;
	
	@Autowired
	public elasticsearchService(RestHighLevelClient client) {
		this.client = client;
	}
	
	public Boolean save(final String message,String indexMonth) {
		try{
			
			final IndexRequest request = new IndexRequest(Indices.WINLOG_INDEX+"-"+indexMonth);
			request.source(message, XContentType.JSON);
			final IndexResponse response = client.index(request, RequestOptions.DEFAULT);
			System.out.println("ok");
			return true;
		}catch(final Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}
	
}
