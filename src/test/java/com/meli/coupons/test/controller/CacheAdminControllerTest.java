package com.meli.coupons.test.controller;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.meli.coupons.controller.CacheAdminController;
import com.meli.coupons.services.HttpRequestService;

@ExtendWith(MockitoExtension.class)
class CacheAdminControllerTest {
	
	@InjectMocks
	CacheAdminController cacheAdminController;
	
	@Mock
	HttpRequestService httpRequestService;
	
	@Test
    void cleanCacheAll(){
		cacheAdminController.cleanCache();
		
		verify(httpRequestService, times(1)).releasePriceByItemId();
		verifyNoMoreInteractions(httpRequestService);
	}
	
}
