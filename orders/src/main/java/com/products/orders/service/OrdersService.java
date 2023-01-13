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
	public void createProduct(Order order) {
		log.info("Creating Order");
		log.info("Value of the email " + retrieveEmail(order.getEmail()));
		log.info("value of the productID " + retrieveProductID(order.getProductID()));

//		&&retrieveEmail(order.getEmail())
//		if (!(retrieveProductID(order.getProductID())) && (retrieveEmail(order.getEmail())))
//		if (retrieveProductID(order.getProductID()) && retrieveEmail(order.getEmail())){
////			log.info("Order is valid");
////			ordersRepository.save(order);
//			log.info("Order is invalid");
//
//
//		} else if ((retrieveProductID(order.getProductID())) && !(retrieveEmail(order.getEmail()))) {
//			log.info("Order is invalid");
//
////			return null;
//
//		} else  {
//			log.info("Order is valid");
//			ordersRepository.save(order);
////				return null;
//
//		}
		if(retrieveEmail(order.getEmail())&&!retrieveProductID(order.getProductID())) {
			ordersRepository.save(order);
		}else {
			log.info("order is invalid");
		}
//		return retrieveEmail(order.getEmail())&& !(retrieveProductID(Integer.toString(order.getProductID())))? ordersRepository.save(order) : null;
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

	public boolean retrieveEmail(String email) {
		boolean emailFound = false;
		List<DataResponse> dataResponseList = getApiResponseData();
		for (DataResponse data : dataResponseList) {
			if (!(email.equals(data.getEmail()))) {
				emailFound = false;
				log.info(data.getEmail() + " false email");
				break;
			} else {
				emailFound = true;
				log.info(email + "true email");
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
			if (order.getProductID()!=productID) {
				productExist = false;
				log.info(order.getProductID() + " false ID");
//				break;
			} else {
				productExist = true;
				log.info(order.getProductID() + " true ID");
//				continue;
				break;

			}
		}
		log.info("product Found " + productExist);

		return productExist;
	}
}
