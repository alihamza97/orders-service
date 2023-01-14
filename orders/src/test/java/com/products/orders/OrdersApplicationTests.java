package com.products.orders;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.products.orders.controller.OrdersController;
import com.products.orders.exception.OrdersException;
import com.products.orders.model.Order;
import com.products.orders.service.OrdersService;

@WebMvcTest(OrdersController.class)
class OrdersApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	private OrdersService ordersService;

	@Autowired
	ObjectMapper mapper;

	@Test
	void should_getAllOrders() throws Exception {

		Order order = Order.builder().orderNumber(123).email("george.bluth@reqres.in").firstName("George")
				.lastName("Bluth").productID(3333).build();

		Order myOrder = Order.builder().orderNumber(124).email("charles.morris@reqres.in").firstName("George")
				.lastName("Bluth").productID(4444).build();

		List<Order> records = new ArrayList<Order>(Arrays.asList(order, myOrder));
		// chcek if the status is 200
		// check the list size
		// check if product exists
		Mockito.when(ordersService.getAllOrders()).thenReturn(records);
		mockMvc.perform(MockMvcRequestBuilders.get("/api/orders/retriveOrders").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[1].email", is("charles.morris@reqres.in")));

		Mockito.verify(ordersService, Mockito.times(1)).getAllOrders();

	}

	@Test
	void should_throw_exception() throws NullPointerException {
		Mockito.when(ordersService.getAllOrders()).thenThrow(new NullPointerException("No orders found"));
		NullPointerException exception = assertThrows(NullPointerException.class, () -> ordersService.getAllOrders());
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains("No orders found"));
	}
	
	

	@Test
	void should_create_order() throws Exception {

		Order order = new Order(123, "george.bluth@reqres.in", "George", "Bluth", 3333);

		String mapContent = new ObjectMapper().writeValueAsString(order);

		Mockito.when(ordersService.createProduct(order)).thenReturn(order);

		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/orders/createOrder")
				.accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON).content(mapContent);
		MvcResult mvcRes = mockMvc.perform(requestBuilder).andReturn();

		int actual = mvcRes.getResponse().getStatus();
		int expected = HttpStatus.OK.value();

		assertEquals(expected, actual);

		Mockito.verify(ordersService).createProduct(order);

	}
	
	@Test
	void should_throw_orderException() {
		Order order = Order.builder().orderNumber(123).email("george.bluth@reqres.in").firstName("George")
				.lastName("Bluth").productID(3333).build();
		Mockito.when(ordersService.createProduct(order)).thenThrow(new OrdersException("Order is invalid"));
		OrdersException exception = assertThrows(OrdersException.class, () -> ordersService.createProduct(order));
		String actualMessage = exception.getMessage();
		assertTrue(actualMessage.contains("Order is invalid"));
	}
	

}
