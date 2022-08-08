package com.example.demo.kafka;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import com.example.demo.elastic.service.IndexService;
import com.example.demo.elastic.service.elasticsearchService;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaConsumerService {
	
	private static RestHighLevelClient client;	
	private static final ObjectMapper mapper = new ObjectMapper()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.setSerializationInclusion(Include.NON_NULL);
	
	//public private 로 변경
	private static IndexService indexService;
	private static elasticsearchService elasticService;
	
	@Autowired
	public KafkaConsumerService(RestHighLevelClient client) {
		this.client = client;
	}
	
	public static void KafkaConsumerService() {
		try {
	         // set kafka properties
	         Properties props = new Properties();
	         props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.10.20.200:9092");  // kafka cluster
	         props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());// KEY_SERIALIZER
	         props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()); // VALUE_SERIALIZER
	         props.put(ConsumerConfig.GROUP_ID_CONFIG,"test");
	         props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

	         // init KafkaConsumer
	         KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

	         consumer.subscribe(Arrays.asList("BigTree-Xdr-Transactions")); // topic list
	         final int minBatchSize = 50;

	         while (true) {
	             ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));  // polling interval
	             for (ConsumerRecord<String, String> record : records) {
	            	 
	            	 JSONParser jsonparse = new JSONParser();
	        		 
	        		 try {
	        			JSONObject obj = (JSONObject) jsonparse.parse(record.value());
	        			String indexMonth = obj.get("@timestamp").toString().substring(0, 7).replace("-","");
	        			
	        			//1. 인덱스 있는가 확인 후 없으면 생성
	        			boolean indexExists = client.indices().exists(new GetIndexRequest("inca-winlog-"+indexMonth), RequestOptions.DEFAULT);
	        			if(indexExists == false) {
	        				indexService = new IndexService(client);
	        				indexService.tryToCreateIndices(indexMonth);
	        			}
	        			
	        			//2. 인덱스 에 저장.
	        			elasticService = new elasticsearchService(client);
	        			boolean test = elasticService.save(record.value(), indexMonth);
	        			consumer.commitSync();
			             System.out.println("commit@@");
			              
	        			if(test) {
	        				System.out.println("insert success");
	        			}else {
	        				System.out.println("insert failed");
	        			}
	        			
	        		} catch (ParseException e) {
	        			e.printStackTrace();
	        		}
	        		 System.out.println("receive message : " + record.value());
	        		 System.out.println("offset : " + record.offset());
	        		 
	             }
	         }
	     } catch (Exception e) {
	         System.out.println(e);
	     }
	}
}
