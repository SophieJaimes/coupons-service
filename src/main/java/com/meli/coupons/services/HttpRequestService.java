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

import com.meli.coupons.object.MeliGetResponse;

@Service
public class HttpRequestService {

	@Value("${spring.api.uri}")
	private String requestUriApi;

	public static final Logger logger = LoggerFactory.getLogger(HttpRequestService.class);
	
	/**
	 * This method request the Mercado Libre REST to get the prices from a list of items
	 * It has a cacheable function for performance
	 * @param itemsList
	 * @return map with a list of items with their respective prices
	 */
	@Cacheable(cacheNames="priceByItemId")
	public Map<String,Float> getPriceByItemId(List<String> itemsList) {

		Map<String,Float> itemsWithPricesMap = new HashMap<>();

		for(String itemId : itemsList) {
			RestTemplate restTemplate = new RestTemplate();
			final String uri = requestUriApi+itemId;
			logger.info("Requesting REST GET uri: {}",uri);
			try {
				MeliGetResponse getResponse = restTemplate.getForObject(uri, MeliGetResponse.class);
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
	/**
	 * This method helps the CacheAdminController to release the cache
	 */
	@CacheEvict(cacheNames="priceByItemId", allEntries=true)
	public void releasePriceByItemId() { 
		// Intentionally blank
	}
}
