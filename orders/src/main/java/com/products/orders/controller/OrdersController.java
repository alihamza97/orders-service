package com.products.orders.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

	@PostMapping("/createOrder")
	public int saveOrder(@RequestBody Order order) {
		ordersService.createProduct(order);
		log.info("Order created");
		return order.getOrderNumber();
	}

	@GetMapping("/retriveOrders")
	public List<Order> retrieveAllOrders() throws NullPointerException {
		log.info("Retrieving all order");
		List<Order> ordersList = ordersService.getAllOrders();
		log.info("Orders List[{}]", ordersList);
		return ordersList;
	}

//	For api testing 
	@GetMapping("/fetchResponseData")
	public List<DataResponse> fetchResponseData() {
		return ordersService.getApiResponseData();
	}
}
