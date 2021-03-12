package com.backend.aggregatorservice.service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.backend.aggregatorservice.model.AggregatorBean;
import com.backend.aggregatorservice.model.Pricing;
import com.backend.aggregatorservice.model.Shipments;
import com.backend.aggregatorservice.model.Track;

@Service
public class AggregatorService {

	protected static Logger logger = Logger.getLogger(AggregatorService.class.getName());
	@Autowired
	private Pricing pricing;
	
	@Autowired
	private Track track;
	
	@Autowired
	private Shipments shipment;

	private static final BlockingQueue <String> queuePrice = new ArrayBlockingQueue(5);
	private static final BlockingQueue<String> queueShipment = new ArrayBlockingQueue(5);
	private static final BlockingQueue<String> queueTrack = new ArrayBlockingQueue(5);
	
	public static LocalTime timeAdded = LocalTime.now();

	@Async
	public CompletableFuture<AggregatorBean> getAllDetails_AS1(String pricingValues, String trackValues,
			String shipmentValues) throws InterruptedException, ExecutionException {
		AggregatorBean bean1 = new AggregatorBean();
		CompletableFuture<Map> priceFutureObject = (CompletableFuture<Map>) pricing.getSigleAPIDetails(pricingValues);

		CompletableFuture<Map> trackFutureObject = (CompletableFuture<Map>) track.getSigleAPIDetails(trackValues);

		CompletableFuture<Map> shipmentFutureObject = (CompletableFuture<Map>) shipment.getSigleAPIDetails(shipmentValues);

		CompletableFuture.allOf(priceFutureObject,  shipmentFutureObject).join();
		if (priceFutureObject.isDone())
			bean1.setPrice(priceFutureObject.get());

		if (trackFutureObject.isDone())
			bean1.setTrack(trackFutureObject.get());

		if (shipmentFutureObject.isDone())
			bean1.setShipments(shipmentFutureObject.get());

		logger.info("getAgrregatorDetails AS1  method() :" + bean1);
		return CompletableFuture.completedFuture(bean1);

	}

	@SuppressWarnings("unchecked")
	public AggregatorBean getAggregatorDetails(String pricingValues, String trackValues,
			String shipmentValues) throws InterruptedException, ExecutionException {
		
		AggregatorBean bean = new AggregatorBean(); 
		List<Future<Map>> priceFuture = new ArrayList<>();
		List<Future<Map>>trackFuture = new ArrayList<>();
		List<Future<Map>>shipmentFuture = new ArrayList<>();

		if((pricingValues == null) && (trackValues == null) &&( shipmentValues == null)){
			return bean;
		}
		List<String> pricingList = getList(pricingValues);
		List<String> trackList = getList(trackValues);
		List<String> shipList = getList(shipmentValues);

			ScheduledExecutorService executor = Executors.newScheduledThreadPool(4);
			
				  for (int i = 0; i < getList(pricingValues).size(); i++) {
			            System.out.println("[Producer] Put : " + pricingList.get(i));
			            queuePrice.add(pricingList.get(i));
			            if (queuePrice.size() == 1)
							timeAdded = LocalTime.now();
			            if(queuePrice.size()>=5) {
			            	break;
			            	
			            }
					}
					for (int i = 0; i < trackList.size(); i++) {
			            System.out.println("[Producer] Put : " + trackList.get(i));
			            queueTrack.add(trackList.get(i));
			            if (queueTrack.size() == 1)
							timeAdded = LocalTime.now();
			            if(queueTrack.size()>=5) {
			            	break;
			            }
			    	}
					for (int i = 0; i < shipList.size(); i++) {
			            System.out.println("[Producer] Put : " + shipList.get(i));
			            queueShipment.add(shipList.get(i));
			            if (queueShipment.size() == 1)
							timeAdded = LocalTime.now();
			            if(queueShipment.size()>=5) {
			            	break;
			            }
			    	}
				
			// executor.scheduleAtFixedRate(() -> { 
				 if (queuePrice.size() == 5) { 
					 String[] array = queuePrice.toArray(new String[queuePrice.size()]); 
				 	priceFuture.add(pricing.getSigleAPIDetails(getQueryParam(array)));
				 	//logger.info(" pricemap:"+priceMap);
		 			queuePrice.clear(); 
			 		} 
				if (queueTrack.size() == 5) { 
					String[] array = queueTrack.toArray(new String[queueTrack.size()]);
					trackFuture.add(track.getSigleAPIDetails(getQueryParam(array)));
					queueTrack.clear();
				}
				
				if (queueShipment.size() == 5) {
					String[] array = queueShipment.toArray(new String[queueShipment.size()]);
					shipmentFuture.add(shipment.getSigleAPIDetails(getQueryParam(array)));
					queueShipment.clear();
				}
			//	},1, 5000, TimeUnit.MILLISECONDS);
			
			 executor.scheduleAtFixedRate(() -> {
				
				 if (Duration.between(LocalTime.now(),
					 timeAdded.plusSeconds(5)).isNegative() && !queuePrice.isEmpty()) {
				 String[] array = queuePrice.toArray(new String[queuePrice.size()]);
				 priceFuture.add(pricing.getSigleAPIDetails(getQueryParam(array)));
				 queuePrice.clear(); 
				 logger.info(" response getting due to Time");
				 } 
			    if(Duration.between(LocalTime.now(), timeAdded.plusSeconds(4)).isNegative() &&
						 !queueTrack.isEmpty()) { 
			    	String[] array = queueTrack.toArray(new String[queueTrack.size()]);
			    	trackFuture.add(track.getSigleAPIDetails(getQueryParam(array)));
			    	 logger.info(" response getting due to Time");
			    	queueTrack.clear();
			    }
			    if(Duration.between(LocalTime.now(), timeAdded.plusSeconds(4)).isNegative() &&
						 !queueShipment.isEmpty()) { String[] array = queueShipment.toArray(new
								 String[queueShipment.size()]);
						 shipmentFuture.add(shipment.getSigleAPIDetails(getQueryParam(array)));
						 queueShipment.clear();
						 logger.info(" response getting due to Time");
						 } }, 1,
					 500, TimeUnit.MILLISECONDS);
			 
			  
			  while(true) {
			
			  for (int i = 0; i < trackFuture.size(); i++) {
				  if(trackFuture.get(i).isDone()) {
					  bean.setTrack(trackFuture.get(i).get());
					  logger.info(" check value"+trackFuture.get(i).get());
				  }
			  }
			  for (int i = 0; i < priceFuture.size(); i++) {
				  if(priceFuture.get(i).isDone()) {
					  bean.setPrice(priceFuture.get(i).get());
					  logger.info(" check value"+priceFuture.get(i).get());
				  }
			  }
			  for (int i = 0; i < shipmentFuture.size(); i++) {
				  if(shipmentFuture.get(i).isDone()) {
					  bean.setShipments(shipmentFuture.get(i).get());
					  logger.info(" check value"+shipmentFuture.get(i).get());
				  }
			  }
				  executor.isShutdown();
				 
				  return bean;
			  }
			
			
	}

	private String getQueryParam(String[] arr) {
		StringJoiner sj1 = new StringJoiner(",");
		for (String str : arr)
			sj1.add(str);

		return sj1.toString();
	}
	
	private List<String> getList(String str){
		List<String> pricingList = new ArrayList<>();
		if(str != null) {
			String[] pricingArray = str.split(",");
			 pricingList = Arrays.asList(pricingArray);
		}
		return pricingList;
	}

}
