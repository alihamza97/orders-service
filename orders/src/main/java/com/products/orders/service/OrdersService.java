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

import com.products.orders.model.Order;
import com.products.orders.repository.OrdersRepository;

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

	@Transactional
	public Order createProduct(Order order) {
		return ordersRepository.save(order);
	}

	@Transactional
	public List<Order> getAllOrders() {
		System.out.println(retriveUsersData());
		return ordersRepository.findAll();
	}

	public String retriveUsersData() {
		ResponseEntity<String> responseEntity = restTemplate.exchange(apiResource,
				HttpMethod.GET,null, String.class);
		String listOfString = responseEntity.getBody();
		log.info(listOfString.toString());
		return listOfString;
	}
}
