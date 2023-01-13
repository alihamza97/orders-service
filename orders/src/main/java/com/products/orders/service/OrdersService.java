package com.products.orders.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.products.orders.model.Order;
import com.products.orders.repository.OrdersRepository;
import com.products.orders.resreq.ApiResponse;
import com.products.orders.resreq.DataResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrdersService {

	@Autowired
	private OrdersRepository ordersRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${api.resource}")
	private String apiResource;

	private ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
			false);

	@Transactional
	public Order createProduct(Order order) {
		log.info("Creating Order");
		if (!(retrieveEmail(order.getEmail())) && (retrieveProductID(order.getProductID()))) {
			log.info("Order is invalid");
			return null;

		} else {
			log.info("Order is valid");
			return ordersRepository.save(order);
		}
//		return retrieveEmail(order.getEmail()) ? ordersRepository.save(order) : null;
	}

	public boolean retrieveEmail(String email) {
		boolean emailFound = false;
		List<DataResponse> dataResponseList = getApiResponseData();
		for (DataResponse data : dataResponseList) {
			if (!email.equals(data.getEmail())) {
				emailFound = false;
				log.info("Email does not exist");
			} else {
				emailFound = true;
				log.info("Email does not exist");
			}
		}
		return emailFound;
	}

	public boolean retrieveProductID(int productID) {
		boolean productExist = false;
		List<Order> ordersList = getAllOrders();
		for (Order order : ordersList) {
			if (productID != order.getProductID()) {
				productExist = false;
				log.info("Product does not exist");
			} else {
				productExist = true;
				log.info("Product already exists");
			}
		}
		return productExist;
	}

	@Transactional
	public List<Order> getAllOrders() {
		log.info("Retrieving list of orders");
		return ordersRepository.findAll();
	}

	public ApiResponse retriveUsersData() {
		ApiResponse apiResponse = null;
		DataResponse dataResponse = null;
		ResponseEntity<String> responseEntity = restTemplate.exchange(apiResource, HttpMethod.GET, null, String.class);
		String retrievedData = responseEntity.getBody();
		log.info("Response message from data source [{}]", retrievedData.toString());
		try {
			apiResponse = objectMapper.readValue(retrievedData, ApiResponse.class);
			for (DataResponse response : apiResponse.getData()) {
				dataResponse = response;
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		log.info("Response after mapping [{}]", retrievedData);

		return apiResponse;
	}

	public List<DataResponse> getApiResponseData() {
		ApiResponse apiResponse = retriveUsersData();
		log.info("Retrieve api data");
		return apiResponse.getData();
	}
}
