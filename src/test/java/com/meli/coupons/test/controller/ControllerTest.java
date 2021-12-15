package com.meli.coupons.test.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.meli.coupons.controller.Controller;
import com.meli.coupons.object.RequestBodyLevel1;
import com.meli.coupons.object.RequestBodyLevel2;
import com.meli.coupons.services.CalculateService;
import com.meli.coupons.services.HttpRequestService;

@ExtendWith(MockitoExtension.class)
class ControllerTest {
	
	@InjectMocks
	Controller controller;

	@Mock
	HttpRequestService httpRequestService;
	
	@Mock
	CalculateService calculateService;
	
	Map<String, Float> map = new HashMap<>();
	Float amount = 0f;
	List<String> suggestedItems = new ArrayList<>();
	RequestBodyLevel1 requestBodyLevel1 = new RequestBodyLevel1();
	
	
	@BeforeEach
	void initialize() {
		
		map = new HashMap<>();
		map.put("MLA1", 100F);
		map.put("MLA2", 210f);
		map.put("MLA3", 260f);
		map.put("MLA4", 80f);
		map.put("MLA5", 90f);
		
	}
	
	@Test
	void givenValidRequestBodyThenReturnsSuggestedItemListToBuyChallenge1() {
		
		requestBodyLevel1.setAmount(500);
		requestBodyLevel1.setItems(map);
		
		amount = 500f;
		
		List<String> suggestedItems = new ArrayList<>();
		suggestedItems.add("MLA1");
		suggestedItems.add("MLA2");
		suggestedItems.add("MLA4");
		suggestedItems.add("MLA5");
		
		when(calculateService.calculate(map, amount)).thenReturn(suggestedItems);
		assertEquals(suggestedItems.toString(),controller.getSuggestedProductsToBuyWithCouponsLevel1(requestBodyLevel1));
	}
	
	@Test
	void givenInvalidRequestBodyThenReturnsMessageChallenge1() {
		
		requestBodyLevel1.setAmount(0);
		requestBodyLevel1.setItems(map);
		
		List<String> suggestedItems = new ArrayList<>();
		
		amount = 0f;
		
		when(calculateService.calculate(map, amount)).thenReturn(suggestedItems);
		assertEquals("There aren't suggested products to buy",controller.getSuggestedProductsToBuyWithCouponsLevel1(requestBodyLevel1));
	}
	
	@Test
	void givenValidRequestBodyThenReturnsMessageCoupon() {
		
		List<String> suggestedItems = new ArrayList<>();
		suggestedItems.add("MLA1");
		suggestedItems.add("MLA2");
		suggestedItems.add("MLA4");
		suggestedItems.add("MLA5");
		
		Map<String,String> suggestedItemsMap = new HashMap<>();
		suggestedItemsMap.put("total", "480.0");
		suggestedItemsMap.put("item_ids",suggestedItems.toString());
		
		String url = "https://api.mercadolibre.com/items/";
		ReflectionTestUtils.setField(httpRequestService, "requestUriApi", url);
		
		List<String> items = new ArrayList<>();
		items.add("MLA1");
		items.add("MLA2");
		items.add("MLA3");
		items.add("MLA4");
		items.add("MLA5");
		
		when(httpRequestService.getPriceByItemId(items)).thenReturn(map);
		when(calculateService.calculateLevel2(map, 500f)).thenReturn(suggestedItemsMap);
		
		RequestBodyLevel2 requestBodyLevel2 = new RequestBodyLevel2();
		requestBodyLevel2.setAmount(500f);
		requestBodyLevel2.setItems(items);
		
		assertEquals(suggestedItemsMap.toString(),controller.getSuggestedProductsToBuyWithCouponsLevel2(requestBodyLevel2));
	}
	
	@Test
	void givenInValidRequestBodyThenReturns404Coupon() {
		
		String url = "https://api.mercadolibre.com/items/";
		ReflectionTestUtils.setField(httpRequestService, "requestUriApi", url);
		
		List<String> items = new ArrayList<>();
		items.add("MLA1");
		items.add("MLA2");
		items.add("MLA3");
		items.add("MLA4");
		items.add("MLA5");		
		
		when(httpRequestService.getPriceByItemId(items)).thenReturn(map);
		when(calculateService.calculateLevel2(map, 10f)).thenReturn(null);
		
		RequestBodyLevel2 requestBodyLevel2 = new RequestBodyLevel2();		
		requestBodyLevel2.setAmount(10f);
		requestBodyLevel2.setItems(items);
		
		assertEquals("404-NOT_FOUND",controller.getSuggestedProductsToBuyWithCouponsLevel2(requestBodyLevel2));
	}
}
