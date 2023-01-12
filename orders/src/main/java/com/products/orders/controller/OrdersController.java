package com.products.orders.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.products.orders.model.Order;
import com.products.orders.service.OrdersService;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {

	@Autowired
	private OrdersService ordersService;
	
	@PostMapping("/createOrder")
	public Order saveOrder(@RequestBody Order order) {
		return ordersService.createProduct(order);
	}
	
	@GetMapping("/retriveOrders")
	public List<Order> retrieveAllOrders(){
		return ordersService.getAllOrders();
	}
}
