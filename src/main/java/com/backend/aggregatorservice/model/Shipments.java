package com.backend.aggregatorservice.model;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.backend.aggregatorservice.service.AggregatorService;

@Component
public class Shipments {
	
	protected static Logger logger = Logger.getLogger(AggregatorService.class.getName());
	@Autowired
	private  RestTemplate restTemplate;

	@Value("${shipment.url}")
	private String url;
	
	private String shipments;
	

	public Shipments() {}
	
	public Shipments(String shipments) {
		this.shipments = shipments;
	}
	
	public  URI urlBuilder(String parameters) {
		UriComponents uri = UriComponentsBuilder.fromHttpUrl(url).queryParam("q", parameters).build();
		return uri.toUri();
	}

	@SuppressWarnings("unchecked")
	
	public CompletableFuture<Map> getSigleAPIDetails(String parameter) {

		ResponseEntity<Map> response = null;
		List<MediaType> mediaTypes = new ArrayList<MediaType>();
		mediaTypes.add(MediaType.APPLICATION_JSON);
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(mediaTypes);
		HttpEntity<HashMap> httpEntity = new HttpEntity<HashMap>(null, headers);
		try {
			logger.info("url :" + url + "  parameter:" + parameter + "  time:" + LocalDateTime.now());
			response = restTemplate.exchange(urlBuilder(parameter), HttpMethod.GET, httpEntity, Map.class);

		} catch (HttpStatusCodeException e) {

			logger.info("In Catch Block:" + LocalDateTime.now());
			ResponseEntity<Map> errorResponse = ResponseEntity.status(e.getRawStatusCode())
					.headers(e.getResponseHeaders()).body(defaultResponse());
			return CompletableFuture.completedFuture(errorResponse.getBody());
		}
		logger.info("getSingleAPIDetails:  "+response.getBody());
		return CompletableFuture.completedFuture(response.getBody());
	}

	private  Map defaultResponse() {
		// TODO Auto-generated method stub
		return new ConcurrentHashMap<String, String>();
	}

}
