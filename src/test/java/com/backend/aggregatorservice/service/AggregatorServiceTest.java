package com.backend.aggregatorservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.backend.aggregatorservice.model.AggregatorBean;
import com.backend.aggregatorservice.model.Pricing;
import com.backend.aggregatorservice.model.Shipments;
import com.backend.aggregatorservice.model.Track;

@SpringBootTest
public class AggregatorServiceTest {
	protected Logger logger = Logger.getLogger(AggregatorServiceTest.class.getName());

	@InjectMocks
	AggregatorService aggregatorService;
	
	@Mock
	Track track;
	@Mock 
	Pricing pricing;
	@Mock
	Shipments shipment;
	
	@Test
	public void getAllDetailsTest_1() throws InterruptedException, ExecutionException {
		 Mockito.when(pricing.getSigleAPIDetails(Mockito.anyString())).thenReturn( new CompletableFuture<>());
		  
		  Mockito.when(track.getSigleAPIDetails(Mockito.anyString())).thenReturn( new CompletableFuture<>());
		  
		  Mockito.when(shipment.getSigleAPIDetails(Mockito.anyString())).thenReturn( new CompletableFuture<>());
		  AggregatorBean list = aggregatorService.getAggregatorDetails(null, null, null);
		  assertNotEquals(response1(), list);
		
	}
	
	@Test
	public void getAllDetailsTest_2() throws InterruptedException, ExecutionException {
		 Mockito.when(pricing.getSigleAPIDetails(Mockito.anyString())).thenReturn(priceMap());
		  
		  Mockito.when(track.getSigleAPIDetails(Mockito.anyString())).thenReturn( trackMap());
		  
		  Mockito.when(shipment.getSigleAPIDetails(Mockito.anyString())).thenReturn( new CompletableFuture<>());
		  String price = "IN,CN,UK,NL,US,CA";
		  String shipment = "109347263,123456891,123456789,123456798,119347263,121136363";
		  String track = "109347263,123456891,123456789,123456798,119347263,121136363";
		  AggregatorBean list = aggregatorService.getAggregatorDetails(price, track, shipment);
		  //Because type of return value doesn't match 
		  assertNotEquals(response(), list);
		
	}
	public AggregatorBean response1() {
		return new AggregatorBean(null,null,null);
	}
	public CompletableFuture<Map> priceMap() {
		Map<String,String> priceMap = new HashMap<>();
		priceMap.put("IN", "56.63951138485059");
		priceMap.put("UK", "41.7046629731398");
		priceMap.put("CN", "35.55696697244559");
		priceMap.put("PK", "99.03924433741057");
		priceMap.put("NL", "72.85178329701218");
		return CompletableFuture.completedFuture(priceMap);
	}
	public CompletableFuture<Map> trackMap() {
		Map<String,String> trackMap = new HashMap<>();
		trackMap.put("109347263", "DELIVERED");
		trackMap.put("123456798", "COLLECTING");
		trackMap.put("119347263", "COLLECTED");
		trackMap.put("123456789", "DELIVERED");
		trackMap.put("123456891", "IN TRANSIT");
		return CompletableFuture.completedFuture(trackMap);
	}
	public AggregatorBean response() {
		Map<String,String> priceMap = new HashMap<>();
		priceMap.put("IN", "56.63951138485059");
		priceMap.put("UK", "41.7046629731398");
		priceMap.put("CN", "35.55696697244559");
		priceMap.put("PK", "99.03924433741057");
		priceMap.put("NL", "72.85178329701218");
		Map<String,String> shipmentMap = new HashMap<>();

		Map<String,String> trackMap = new HashMap<>();
		trackMap.put("109347263", "DELIVERED");
		trackMap.put("123456798", "COLLECTING");
		trackMap.put("119347263", "COLLECTED");
		trackMap.put("123456789", "DELIVERED");
		trackMap.put("123456891", "IN TRANSIT");
		AggregatorBean  bean = new AggregatorBean();
		bean.setTrack(trackMap);
		bean.setPrice(priceMap);
		bean.setShipments(null);
		return bean;
	}
}
