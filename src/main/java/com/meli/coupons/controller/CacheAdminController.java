package com.meli.coupons.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.meli.coupons.services.HttpRequestService;
@RestController
public class CacheAdminController {
	
	@Autowired
	HttpRequestService httpRequestService;
	
	@GetMapping(value = "/cacheClean")
	public String cleanCache() {
		httpRequestService.releasePriceByItemId();
		return "Cache cleaned";
	}
}
