package com.backend.aggregatorservice.model;

import java.util.List;

public class TrackRequest {
	private List<Integer> orderNo;

	public List<Integer> getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(List<Integer> orderNo) {
		this.orderNo = orderNo;
	}
	
	@Override
	public String toString() {
		
		return orderNo.toString();
		
	}
	
}
