package com.meli.coupons.controller;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.meli.coupons.object.RequestBodyLevel1;
import com.meli.coupons.object.RequestBodyLevel2;
import com.meli.coupons.services.CalculateService;
import com.meli.coupons.services.HttpRequestService;

@RestController
public class Controller {

	@Autowired
	CalculateService calculateService;
	@Autowired
	HttpRequestService httpRequestService;

	public static final Logger logger = LoggerFactory.getLogger(Controller.class);

	@PostMapping(value = "/challengeLevel1/",produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public String getSuggestedProductsToBuyWithCouponsLevel1(@RequestBody RequestBodyLevel1 requestBody) {

		logger.info("requestBodyItems: {} requestBodyAmount: {}",requestBody.getItems(),requestBody.getAmount());
		List<String> suggestedProductsToBuyList = calculateService.calculate(requestBody.getItems(), requestBody.getAmount());

		if(!suggestedProductsToBuyList.isEmpty()) {
			return suggestedProductsToBuyList.toString();
		}else {
			return "There aren't suggested products to buy";
		}
	}

	@PostMapping(value = "/coupon/",produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public String getSuggestedProductsToBuyWithCouponsLevel2(@RequestBody RequestBodyLevel2 requestBody) {
		String response = null;
		try {
			logger.info("requestBodyItems: {} requestBodyAmount: {}",requestBody.getItems(),requestBody.getAmount());

			Map<String,Float> itemsWithPricesMap = httpRequestService.getPriceByItemId(requestBody.getItems());
			Map<String,String>  suggestedProductsToBuy = calculateService.calculateLevel2(itemsWithPricesMap, requestBody.getAmount());

			boolean isAmountEnoughToBuyOneProductAtLeast = false;
			for (Entry<String, Float> itemMap : itemsWithPricesMap.entrySet()) {
				if (itemMap.getValue() < requestBody.getAmount()) {
					isAmountEnoughToBuyOneProductAtLeast = true;
					break;
				}
			}	
			
			if (!isAmountEnoughToBuyOneProductAtLeast) {
				response = "404-NOT_FOUND";
				logger.info("Coupon amount is not enough to buy any item: {}",response);
			}		
			else if(null != suggestedProductsToBuy) {
				
				response = suggestedProductsToBuy.toString();
				logger.info("Post response: {}",suggestedProductsToBuy);
			}
		} catch (Exception e) {
			logger.error("RequestBody Error");
			response = "400-BAD_REQUEST";
		}
		return response;
	}
	
}
