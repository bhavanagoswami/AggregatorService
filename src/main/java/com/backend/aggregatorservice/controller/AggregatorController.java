package com.backend.aggregatorservice.controller;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.aggregatorservice.model.AggregatorBean;
import com.backend.aggregatorservice.model.AggregatorResponse;
import com.backend.aggregatorservice.service.AggregatorService;

@RestController
@RequestMapping("/aggregation")
public class AggregatorController {
	
	protected Logger logger = Logger.getLogger(AggregatorController.class.getName());
	
	@Autowired 
	AggregatorService aggregatorService;
	
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE )
	
	
    public AggregatorBean getAgrregatorDetails(@RequestParam ("pricing") String pricingValues,@RequestParam("track") String trackValues,@RequestParam("shipments") String shipmentValues) throws InterruptedException, ExecutionException {

		logger.info("getAgrregatorDetails controllers method() start :"+LocalDateTime.now());
		AggregatorBean bean = null;
		try {
			 bean = aggregatorService.getAggregatorDetails(pricingValues,trackValues,shipmentValues);
			
		} catch (InterruptedException e) {
			e.printStackTrace();
			 
		}
		logger.info("getAgrregatorDetails controllers method() end :"+LocalDateTime.now());
        return  bean;
    }
	
@GetMapping(value = "as1/",produces = MediaType.APPLICATION_JSON_VALUE )
	
    public CompletableFuture
	<AggregatorBean> getAgrregatorDetails_AS1(@RequestParam ("pricing") String pricingValues,@RequestParam("track") String trackValues,@RequestParam("shipments") String shipmentValues) throws InterruptedException, ExecutionException {

	logger.info("getAgrregatorDetails_AS1 controllers method() start :"+LocalDateTime.now());
	CompletableFuture <AggregatorBean> bean = null;
		try {
			 bean = aggregatorService.getAllDetails_AS1(pricingValues,trackValues,shipmentValues);
			 logger.info("getAgrregatorDetails controllers method() :"+LocalDateTime.now()+bean);
		        return bean;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//
		logger.info("getAgrregatorDetails_AS1 controllers method() end :"+LocalDateTime.now()+bean);
        return bean;

    }

}
