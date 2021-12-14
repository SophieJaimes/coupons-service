package com.meli.coupons.object;

import java.util.List;

public class RequestBodyLevel2 {
	
	private List<String> items;
	private float amount;
	
	public List<String> getItems() {
		return items;
	}
	public void setItems(List<String> items) {
		this.items = items;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
}
