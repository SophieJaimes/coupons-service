package com.meli.coupons.controller;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.meli.coupons.object.RequestBodyObject;
import com.meli.coupons.object.Response;
import com.meli.coupons.object.ResponseLevel2;
import com.meli.coupons.services.CalculateService;
import com.meli.coupons.services.HttpRequestService;

@RestController
public class Controller {
	
	@Autowired
	Response response;
	@Autowired
	ResponseLevel2 responseLevel2;
	@Autowired
	CalculateService calculateService;
	@Autowired
	HttpRequestService httpRequestService;
	
	public static final Logger logger = LoggerFactory.getLogger(Controller.class);
	
	@PostMapping(value = "/couponLevel1/",produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public Response getSuggestedProductsToBuyWithCouponsLevel1(@RequestBody RequestBodyObject requestBody) {
		
		try {
			logger.info("requestBodyItems: {} requestBodyAmount: {}",requestBody.getItems(),requestBody.getAmount());
			
			Map<String,Float> itemsWithPricesMap = httpRequestService.getPriceByItemId(requestBody.getItems());
			List<String> suggestedProductsToBuyList = calculateService.calculate(itemsWithPricesMap, requestBody.getAmount());
			
			boolean isAmountEnoughToBuyOneProductAtLeast = false;
			for (Entry<String, Float> itemMap : itemsWithPricesMap.entrySet()) {
			    if (itemMap.getValue() < requestBody.getAmount()) {
			    	isAmountEnoughToBuyOneProductAtLeast = true;
			    	break;
			    }
			}	
			
			if (!isAmountEnoughToBuyOneProductAtLeast) {
				response.setStatus("404-NOT_FOUND");
				response.setMessage("");
			}		
			else if(null != suggestedProductsToBuyList) {
				response.setStatus("200-OK");
				response.setMessage(suggestedProductsToBuyList.toString());
			}
		} catch (Exception e) {
			logger.error("RequestBody Error");
			response.setStatus("400-BAD_REQUEST");
			response.setMessage("");
		}
		return response;
	}
	
	@PostMapping(value = "/coupon/",produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public Response getSuggestedProductsToBuyWithCouponsLevel2(@RequestBody RequestBodyObject requestBody) {
		
		try {
			logger.info("requestBodyItems: {} requestBodyAmount: {}",requestBody.getItems(),requestBody.getAmount());
			
			Map<String,Float> itemsWithPricesMap = httpRequestService.getPriceByItemId(requestBody.getItems());
			JSONObject suggestedProductsToBuy = calculateService.calculateLevel2(itemsWithPricesMap, requestBody.getAmount());
			
			boolean isAmountEnoughToBuyOneProductAtLeast = false;
			for (Entry<String, Float> itemMap : itemsWithPricesMap.entrySet()) {
			    if (itemMap.getValue() < requestBody.getAmount()) {
			    	isAmountEnoughToBuyOneProductAtLeast = true;
			    	break;
			    }
			}	
			
			if (!isAmountEnoughToBuyOneProductAtLeast) {
				response.setStatus("404-NOT_FOUND");
			}		
			else if(null != suggestedProductsToBuy) {

				response.setStatus("200-OK");
				response.setMessage(suggestedProductsToBuy.toString().replace("\\", ""));
			}
		} catch (Exception e) {
			logger.error("RequestBody Error");
			responseLevel2.setStatus("400-BAD_REQUEST");
		}
		return response;
	}
}
