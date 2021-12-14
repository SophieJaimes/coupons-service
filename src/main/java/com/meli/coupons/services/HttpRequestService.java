package com.meli.coupons.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.meli.coupons.object.GetResponse;

@Service
public class HttpRequestService {

	@Value("${spring.api.uri}")
	private String requestUriApi;

	public static final Logger logger = LoggerFactory.getLogger(HttpRequestService.class);
	
	
	@Cacheable(cacheNames="priceByItemId")
	public Map<String,Float> getPriceByItemId(List<String> itemsList) {

		Map<String,Float> itemsWithPricesMap = new HashMap<>();

		for(String itemId : itemsList) {

			RestTemplate restTemplate = new RestTemplate();
			final String uri = requestUriApi+itemId;
			logger.debug("uri: {}",uri);
			try {
				GetResponse getResponse = restTemplate.getForObject(uri, GetResponse.class);
				if(null != getResponse) {
					itemsWithPricesMap.put(itemId, getResponse.getPrice());
				}	
			} catch (Exception e) {
				logger.error("Error getting price from API");
				e.printStackTrace();
			}
		}
		return itemsWithPricesMap;
	}
	
	@CacheEvict(cacheNames="priceByItemId", allEntries=true)
	public void releasePriceByItemId() { 
		// Intentionally blank
	}
}
