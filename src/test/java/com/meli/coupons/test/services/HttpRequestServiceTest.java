package com.meli.coupons.test.services;

import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.meli.coupons.controller.Controller;
import com.meli.coupons.object.RequestBodyLevel2;
import com.meli.coupons.services.HttpRequestService;

@ExtendWith(MockitoExtension.class)
class HttpRequestServiceTest {
	
	@InjectMocks
	Controller controller;
	
	@Mock
	HttpRequestService httpRequestService;
	
	@Test
	void givenBody_executeReceptionRest() {
		
		String url = "https://api.mercadolibre.com/items/";
		
		List<String> itemsList = new ArrayList<>();
		itemsList.add("MLA1");
		itemsList.add("MLA2");
		itemsList.add("MLA3");
		itemsList.add("MLA4");
		itemsList.add("MLA5");
		
		RequestBodyLevel2 rbl = new RequestBodyLevel2();
		rbl.setAmount(500);
		rbl.setItems(itemsList);
		
		ReflectionTestUtils.setField(httpRequestService, "requestUriApi", url);
		
		controller.getSuggestedProductsToBuyWithCouponsLevel2(rbl);
		verify(httpRequestService).getPriceByItemId(itemsList);
	}
	
}
