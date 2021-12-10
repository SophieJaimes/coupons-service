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

@Service
public class CalculateService {
	
	public static final Logger logger = LoggerFactory.getLogger(CalculateService.class);


	public List<String> calculate(Map<String, Float> items, Float amount){

		Map<String, Float> result = items.entrySet().stream()
				.sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
						(oldValue, newValue) -> oldValue, LinkedHashMap::new));

		logger.info("result: {}",result);

		Float totalAmount = 0f;
		List<String> productList = null;

		for (Map.Entry<String, Float> entry : result.entrySet()) {

			productList = new ArrayList<>();
			totalAmount += entry.getValue();

			if(totalAmount < amount) {
				productList.add(entry.getKey());
				
			}
			productList.sort(Comparator.naturalOrder());
			for(String pl : productList) {
				
				logger.info("pl item: {}",pl);
			}
		}

		return productList;
	}

	public static void main(String[] args) {

		Map<String, Float> map = new HashMap<>();
		map.put("MLA2", 210.99F);
		map.put("MLA3", 260f);
		map.put("MLA4", 80f);
		map.put("MLA1", 100f);
		map.put("MLA5", 90f);
		map.put("MLA6", 10f);
		map.put("MLA7", 20f);
		map.put("MLA8", 20f);
		map.put("MLA9", 30f);

		Float amount = 600f;

		CalculateService cs = new CalculateService();
		cs.calculate(map, amount);
		

	}

}
