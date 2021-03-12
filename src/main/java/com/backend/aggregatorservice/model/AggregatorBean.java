package com.backend.aggregatorservice.model;

import java.util.Map;

public class AggregatorBean {
	
	private Map shipments;
	private Map track;
	private Map price;
	
	public AggregatorBean() {}
	public AggregatorBean(Map<String,String> shipments, Map<String,String> track, Map<String,String> price) {
		super();
		this.shipments = shipments;
		this.track = track;
		this.price = price;
	}
	public Map getTrack() {
		return track;
	}
	public void setTrack(Map track) {
		this.track = track;
	}
	public Map getShipments() {
		return shipments;
	}
	public void setShipments(Map shipments) {
		this.shipments = shipments;
	}
	public Map getPrice() {
		return price;
	}
	public void setPrice(Map price) {
		this.price = price;
	}
	
	@Override
	public String toString() {
		return "Agrregate{" + "shipments=" + shipments + ", Track=" + track + ", Price=" + price+"}";
	}

}
