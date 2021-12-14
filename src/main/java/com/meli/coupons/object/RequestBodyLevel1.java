package com.meli.coupons.object;

import java.util.Map;

public class RequestBodyLevel1 {
	
	private Map<String, Float> items;
	private Float amount;
	
	public Map<String, Float> getItems() {
		return items;
	}
	public void setItems(Map<String, Float> items) {
		this.items = items;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
}
