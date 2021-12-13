package com.meli.coupons.object;

import org.springframework.stereotype.Component;

@Component
public class ResponseLevel2 {
	
	private String status;
	private String itemIds;
	private String amount;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getItemIds() {
		return itemIds;
	}
	public void setItemIds(String itemIds) {
		this.itemIds = itemIds;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	

}
