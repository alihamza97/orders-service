package com.products.orders.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.boot.jaxb.mapping.spi.JaxbConvert;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.products.orders.model.Order;
import com.products.orders.resreq.DataResponse;
import com.products.orders.service.OrdersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/orders")
@Slf4j
@RequiredArgsConstructor
public class OrdersController {

	private final OrdersService ordersService;

	@PostMapping("/create-order")
	public Map<String, Integer> getGreeting(@RequestBody Order order) {
		final Map<String, Integer> orderMap = new HashMap<>();
		ordersService.createProduct(order);
		int orderNumber = order.getOrderNumber();
		orderMap.put("orderNumber", orderNumber);
		return orderMap;
	}

	@GetMapping("/retrive-orders")
	public List<Order> retrieveAllOrders() throws NullPointerException {
		log.info("Retrieving all order");
		List<Order> ordersList = ordersService.getAllOrders();
		log.info("Orders List[{}]", ordersList);
		return ordersList;
	}

//	For api testing 
	@GetMapping("/fetch-response-data")
	public List<DataResponse> fetchResponseData() {
		return ordersService.getApiResponseData();
	}
}
