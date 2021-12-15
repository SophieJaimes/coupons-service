package com.meli.coupons.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class CalculateService {

	public static final Logger logger = LoggerFactory.getLogger(CalculateService.class);

	/**
	 * This method sorts the itemsList from lowest to highest by price and delete duplicates by item_id
	 * Adds all prices as long as their sum is lower than coupon amount
	 * method created to develop the level 1 from the challenge
	 */
	public List<String> getSuggestedItemsList(Map<String, Float> items, Float amount){

		Map<String, Float> orderedByPriceItemsList = items.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
						(oldValue, newValue) -> oldValue, LinkedHashMap::new));
		
		logger.debug("orderedByPriceItemsList: {}",orderedByPriceItemsList);

		Float totalAmount = 0f;
		List<String> resultProductsList = new ArrayList<>();

		for (Map.Entry<String, Float> entry : orderedByPriceItemsList.entrySet()) {

			totalAmount += entry.getValue();

			if(totalAmount < amount) {
				resultProductsList.add(entry.getKey());
			}
		}
		resultProductsList.sort(Comparator.naturalOrder());

		String suggestedProductsToBuy = new Gson().toJson(resultProductsList);
		logger.info("suggestedProductsToBuy: {}",suggestedProductsToBuy);

		return resultProductsList;
	}
	
	/**
	 * This method has the same function that getSuggestedItemsList() but in addition 
	 * returns the coupon amount 
	 */
	public String getCouponSuggestedItemsList(Map<String, Float> items, Float amount){

		Map<String, Float> orderedByPriceItemsList = items.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
						(oldValue, newValue) -> oldValue, LinkedHashMap::new));

		logger.debug("orderedByPriceItemsList: {}",orderedByPriceItemsList);

		Float totalAmount = 0f;
		List<String> resultProductsList = new ArrayList<>();
		

		for (Map.Entry<String, Float> entry : orderedByPriceItemsList.entrySet()) {

			totalAmount += entry.getValue();

			if(totalAmount < amount) {
				resultProductsList.add(entry.getKey());
			}else {
				totalAmount -= entry.getValue();
			}
		}
		resultProductsList.sort(Comparator.naturalOrder());
		
		LinkedHashMap<Object, Object> responseMap = new LinkedHashMap<>();
		responseMap.put("item_ids",resultProductsList);
		responseMap.put("total", totalAmount);
		
		String responseJson = new Gson().toJson(responseMap);
		
		logger.info("responseJson: {} ", responseJson);
		
		return responseJson;
	}
}
