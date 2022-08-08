package com.example.demo.elastic.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.annotation.PostConstruct;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.elastic.Indices;
import com.example.demo.elastic.Util;


@Service
public class IndexService {
	
	private final List<String> INDICES_TO_CREATE = List.of(Indices.WINLOG_INDEX);
	private final RestHighLevelClient client;
	private static final Logger log = LoggerFactory.getLogger(Util.class);
	
	@Autowired
	public IndexService(RestHighLevelClient client) {
		this.client = client;
	}
	
	//index inca-winlog-yyyymm형식으로 생성 method
	
	public void tryToCreateIndices(String indexMonth) {
		
		final String settings = Util.loadAsString("static/es-settings.json");
		
		for(final String indexName : INDICES_TO_CREATE) {
			try {
				final String mappings = Util.loadAsString("static/mappings/"+indexName+".json");
				if(settings == null || mappings == null) {
					log.error("Failed to create index with name '{}'",indexName);
				}
				final CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName+"-"+indexMonth);
				createIndexRequest.settings(settings,XContentType.JSON);
				createIndexRequest.mapping(mappings,XContentType.JSON);
				
				client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
				
			}catch(final Exception e) {
				log.error(e.getMessage(),e);
			}
		}
	}
}
