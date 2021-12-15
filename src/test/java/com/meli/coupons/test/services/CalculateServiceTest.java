package com.meli.coupons.test.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.gson.Gson;
import com.meli.coupons.services.CalculateService;

@ExtendWith(MockitoExtension.class)
class CalculateServiceTest {
	
	@InjectMocks
	CalculateService calculateService;
	
	@Test
	void givenValidItemListThenReturnsSuggestedProductList() {
		
		List<String> suggestedItems = new ArrayList<>();
		suggestedItems.add("MLA1");
		suggestedItems.add("MLA2");
		suggestedItems.add("MLA4");
		suggestedItems.add("MLA5");
		
		Map<String, Float> map = new HashMap<>();
		map.put("MLA1", 100F);
		map.put("MLA2", 210f);
		map.put("MLA3", 260f);
		map.put("MLA4", 80f);
		map.put("MLA5", 90f);
		
		assertEquals(suggestedItems,calculateService.getSuggestedItemsList(map, 500f));
	}
	
	@Test
	void givenValidItemListThenReturnsMapItemsAndAmount() {
		
		List<String> suggestedItems = new ArrayList<>();
		suggestedItems.add("MLA1");
		suggestedItems.add("MLA2");
		suggestedItems.add("MLA4");
		suggestedItems.add("MLA5");
		
		Map<String, Float> map = new HashMap<>();
		map.put("MLA1", 100F);
		map.put("MLA2", 210f);
		map.put("MLA3", 260f);
		map.put("MLA4", 80f);
		map.put("MLA5", 90f);
		
		LinkedHashMap<Object, Object> responseMap = new LinkedHashMap<>();
		responseMap.put("item_ids",suggestedItems);
		responseMap.put("total", 480f);
		
		String responseJson = new Gson().toJson(responseMap);
		
		assertEquals(responseJson,calculateService.getCouponSuggestedItemsList(map, 500f));
	}

}
