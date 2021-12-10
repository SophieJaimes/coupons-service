package com.meli.coupons.services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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


	public List<String> calculate(Map<String, Float> items, Float amount){
		
		logger.info("Initial data - items: {} amount: {}",items,amount);

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
		
		String jsonSuggestedProducts = new Gson().toJson(resultProductsList);
		logger.info("jsonSuggestedProducts: {}",jsonSuggestedProducts);
		
		return resultProductsList;
	}

	public static void main(String[] args) {

		Map<String, Float> map = new HashMap<>();
		map.put("MLA1", 100F);
		map.put("MLA2", 210f);
		map.put("MLA3", 260f);
		map.put("MLA4", 80f);
		map.put("MLA5", 90f);

		Float amount = 500f;

		CalculateService calculateService = new CalculateService();
		calculateService.calculate(map, amount);
	}
}
