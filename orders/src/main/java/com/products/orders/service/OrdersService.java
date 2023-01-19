package com.products.orders.service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.products.orders.exception.OrdersException;
import com.products.orders.model.Order;
import com.products.orders.repository.OrdersRepository;
import com.products.orders.resreq.ApiResponse;
import com.products.orders.resreq.DataResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrdersService {

	private final OrdersRepository ordersRepository;

	private final RestTemplate restTemplate;

	@Value("${api.resource}")
	private String apiResource;

	private ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
			false);

	@Transactional
	public Order createProduct(Order order) {
		log.info("Creating Order");
		if (retrieveEmail(order.getEmail()) && !retrieveProductID(order.getProductID())) {
			log.info("Saving order...");
			return ordersRepository.save(order);
		} else {
			log.info("order is invalid");
			if (retrieveProductID(order.getProductID()) && !retrieveEmail(order.getEmail())) {
				throw new OrdersException("Invalid order with wrong email or duplicate product ID");
			} else if (!retrieveEmail(order.getEmail())) {
				throw new OrdersException("Email is not correct");
			} else if (retrieveProductID(order.getProductID())) {
				throw new OrdersException("Product already exists with the simillar product ID");
			} else {
				throw new OrdersException("Order is Invalid");
			}
		}
	}

	@Transactional
	public List<Order> getAllOrders() {
		log.info("Retrieving list of orders");
		List<Order> ordersList = ordersRepository.findAll();
		if (ordersList.isEmpty() || ordersList == null) {
			throw new NullPointerException("No orders found");
		} else {
			return ordersRepository.findAll();
		}
	}

	public ApiResponse retriveUsersData() {
		ApiResponse apiResponse = null;
		ResponseEntity<String> responseEntity = restTemplate.exchange(apiResource, HttpMethod.GET, null, String.class);
		String retrievedData = responseEntity.getBody();
		log.info("Response message from data source [{}]", retrievedData);
		try {
			apiResponse = objectMapper.readValue(retrievedData, ApiResponse.class);
			log.info("Response after mapping [{}]", retrievedData);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return apiResponse;
	}

	public List<DataResponse> getApiResponseData() {
		ApiResponse apiResponse = retriveUsersData();
		log.info("Retrieving api data{}", apiResponse.getData());
		return apiResponse.getData();
	}

	public boolean retrieveEmail(String email) {
		boolean emailFound = false;
		List<DataResponse> dataResponseList = getApiResponseData();
		for (DataResponse data : dataResponseList) {
			if (!(email.equals(data.getEmail()))) {
				emailFound = false;
			} else {
				emailFound = true;
				break;
			}
		}
		log.info("Email Found " + emailFound);
		return emailFound;
	}

	public boolean retrieveProductID(int productID) {
		boolean productExist = false;
		List<Order> ordersList = ordersRepository.findAll();
		for (Order order : ordersList) {
			if (order.getProductID() != productID) {
				productExist = false;
			} else {
				productExist = true;
				break;

			}
			for(int i=0;i<ordersList.size();i++) {
				
			}
		}
		log.info("Product found " + productExist);

		return productExist;
	}
}
