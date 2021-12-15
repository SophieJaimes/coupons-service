package com.meli.coupons.test.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.meli.coupons.services.HttpRequestService;

@ExtendWith(MockitoExtension.class)
class HttpRequestServiceTest {
	
	@InjectMocks
	HttpRequestService httpRequestService;
	
	@Test
	void givenValidItemListThenReturnsMapItemsWithPrices() {
		
		List<String> items = new ArrayList<>();
		items.add("MLA1");
		items.add("MLA2");
		items.add("MLA3");
		items.add("MLA4");
		items.add("MLA5");
		
		Map<String, Float> map = new HashMap<>();
		map.put("MLA1", 100F);
		map.put("MLA2", 210f);
		map.put("MLA3", 260f);
		map.put("MLA4", 80f);
		map.put("MLA5", 90f);
		
		String url = "https://api.mercadolibre.com/items/";
		ReflectionTestUtils.setField(httpRequestService, "requestUriApi", url);
		
		when(httpRequestService.getPriceByItemId(items)).thenReturn(map);
		
		assertEquals(map,httpRequestService.getPriceByItemId(items));
	}

}
