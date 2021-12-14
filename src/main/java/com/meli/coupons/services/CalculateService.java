package com.meli.coupons.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

@Service
public class CalculateService {

	public static final Logger logger = LoggerFactory.getLogger(CalculateService.class);

	public List<String> calculate(Map<String, Float> items, Float amount){

		//Ordena de menor a mayor por amount y elimina los duplicados
		
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

	public JSONObject calculateLevel2(Map<String, Float> items, Float amount){

		Map<String, Float> orderedByPriceItemsList = items.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
						(oldValue, newValue) -> oldValue, LinkedHashMap::new));

		logger.debug("orderedByPriceItemsList: {}",orderedByPriceItemsList);

		Float totalAmount = 0f;
		List<String> resultProductsList = new ArrayList<>();
		JSONObject myObject = new JSONObject();

		for (Map.Entry<String, Float> entry : orderedByPriceItemsList.entrySet()) {

			totalAmount += entry.getValue();

			if(totalAmount < amount) {
				resultProductsList.add(entry.getKey());
				
			}
		}
		resultProductsList.sort(Comparator.naturalOrder());
		String suggestedProductsToBuy = new Gson().toJson(resultProductsList);
		
		myObject.put("item_ids",suggestedProductsToBuy);
		myObject.put("amount", String.valueOf(totalAmount));
		logger.info("suggestedProductsToBuy: {} amount: {}",suggestedProductsToBuy,totalAmount);

		return myObject;
	}
}
