package com.products.orders.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.web.client.RestTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.products.orders.exception.OrdersException;
import com.products.orders.model.Order;
import com.products.orders.repository.OrdersRepository;
import com.products.orders.resreq.ApiResponse;
import com.products.orders.resreq.DataResponse;

@ExtendWith(SpringExtension.class)
public class OrdersServiceTests {

	private OrdersService ordersService;

	private RestTemplate restTemplate;

	private MockRestServiceServer mockServer;

	private ObjectMapper mapper = new ObjectMapper();

	@MockBean
	private OrdersRepository ordersRepository;

	@BeforeEach
	void setup() {
		restTemplate = new RestTemplate();
		ordersService = new OrdersService(ordersRepository, restTemplate);
		mockServer = MockRestServiceServer.createServer(restTemplate);
	}

	@Test
	void should_dataResponse_data() throws JsonProcessingException {

		ApiResponse apiResponse = setApiResponse();
		List<DataResponse> dataResponseList = new ArrayList<>();
		dataResponseList.add(new DataResponse(1, "alihazma@whatever.com", "Ali", "Hamza", "imguri"));
		dataResponseList.add(new DataResponse(2, "alihamza2.0@whatever.com", "Ali", "Hamza 2.0", "imguri"));
		dataResponseList.add(new DataResponse(3, "alihamza3.0@whatever.com", "Ali", "Hamza 3.0", "imguri"));

		final String responseString = mapper.writeValueAsString(apiResponse);

		mockServer.expect(ExpectedCount.once(), request -> {
			try {
				new URI("https://fakeURI.com");
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(responseString));

		ApiResponse receivedResponse = ordersService.retriveUsersData();
		List<DataResponse> receievedExpectedData = receivedResponse.getData();

		mockServer.verify();
		assertEquals(dataResponseList, receievedExpectedData);
		assertEquals("1", receivedResponse.getPage());
		assertThat(receivedResponse.getData()).isNotNull();
	}

	@Test
	void should_retrieve_listData() throws JsonProcessingException {
		ApiResponse apiResponse = setApiResponse();
		List<DataResponse> dataResponseList = new ArrayList<>();
		dataResponseList.add(new DataResponse(1, "alihazma@whatever.com", "Ali", "Hamza", "imguri"));
		dataResponseList.add(new DataResponse(2, "alihamza2.0@whatever.com", "Ali", "Hamza 2.0", "imguri"));
		dataResponseList.add(new DataResponse(3, "alihamza3.0@whatever.com", "Ali", "Hamza 3.0", "imguri"));

		final String responseString = mapper.writeValueAsString(apiResponse);
//
		mockServer.expect(ExpectedCount.once(), request -> {
			try {
				new URI("https://fakeURI.com");
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(responseString));
		List<DataResponse> receievedExpectedData = ordersService.getApiResponseData();

		mockServer.verify();
		assertEquals(dataResponseList, receievedExpectedData);
		assertThat(receievedExpectedData).isNotNull();
	}

	@Test
	void should_retrieve_email() throws JsonProcessingException {
		ApiResponse apiResponse = setApiResponse();

		final String responseString = mapper.writeValueAsString(apiResponse);
		mockServer.expect(ExpectedCount.manyTimes(), request -> {
			try {
				new URI("https://fakeURI.com");
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(responseString));

		List<DataResponse> receievedExpectedData = ordersService.getApiResponseData();
		String email = "alihazma@whatever.com";
		boolean emailFound = false;
		for (DataResponse e : receievedExpectedData) {
			if (e.getEmail().equals("alihazma@whatever.com")) {
				emailFound = true;
				break;
			}
		}
		emailFound = ordersService.retrieveEmail(email);

		mockServer.verify();
		assertTrue(emailFound);
	}

	@Test
	void should_notRetrieve_email() throws JsonProcessingException {
		ApiResponse apiResponse = setApiResponse();

		final String responseString = mapper.writeValueAsString(apiResponse);
		mockServer.expect(ExpectedCount.manyTimes(), request -> {
			try {
				new URI("https://fakeURI.com");
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(responseString));

		List<DataResponse> receievedExpectedData = ordersService.getApiResponseData();
		String email = "alihazma@whateve";
		boolean emailFound = false;
		emailFound = ordersService.retrieveEmail(email);

		mockServer.verify();
		assertFalse(emailFound);
	}

	@Test
	void should_return_all_orders() throws JsonProcessingException {
		List<Order> myOrder = new ArrayList<>();
		myOrder.add(Order.builder().orderNumber(124).email("charles.morris@reqres.in").firstName("George")
				.lastName("Bluth").productID(4444).build());
		when(ordersRepository.findAll()).thenReturn(myOrder);
		List<Order> orders = ordersService.getAllOrders();
		assertThat(orders).isNotNull();
	}

	@Test
	void should_throw_nullException_orders() throws JsonProcessingException {
		List<Order> myOrder = new ArrayList<>();
		when(ordersRepository.findAll()).thenReturn(myOrder);
		Exception exception = assertThrows(NullPointerException.class, () -> ordersService.getAllOrders());
		assertEquals("No orders found", exception.getMessage());
	}

	@Test
	void should_retrieve_productID() throws JsonProcessingException {
		List<Order> myOrder = new ArrayList<>();
		myOrder.add(Order.builder().orderNumber(124).email("charles.morris@reqres.in").firstName("George")
				.lastName("Bluth").productID(4444).build());
		when(ordersRepository.findAll()).thenReturn(myOrder);
		int productID = 4444;
		boolean isProductFound = false;
		for (Order o : myOrder) {
			if (o.getProductID() == productID) {
				isProductFound = true;
				break;
			}
		}
		isProductFound = ordersService.retrieveProductID(productID);
		assertTrue(isProductFound);
	}

	@Test
	void should_retrieve_no_productID() throws JsonProcessingException {
		List<Order> myOrder = new ArrayList<>();
		myOrder.add(Order.builder().orderNumber(124).email("charles.morris@reqres.in").firstName("George")
				.lastName("Bluth").productID(4444).build());
		when(ordersRepository.findAll()).thenReturn(myOrder);
		int productID = 444;
		boolean isProductFound = false;
		isProductFound = ordersService.retrieveProductID(productID);
		assertFalse(isProductFound);
	}

	@Test
	void should_save_orders() throws JsonProcessingException {
		ApiResponse apiResponse = setApiResponse();

		final String responseString = mapper.writeValueAsString(apiResponse);
		mockServer.expect(ExpectedCount.manyTimes(), request -> {
			try {
				new URI("https://fakeURI.com");
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(responseString));

		List<DataResponse> receievedExpectedData = ordersService.getApiResponseData();
		String email = "alihazma@whatever.com";
		boolean emailFound = false;
		for (DataResponse e : receievedExpectedData) {
			if (e.getEmail().equals("alihazma@whatever.com")) {
				emailFound = true;
				break;
			}
		}
		emailFound = ordersService.retrieveEmail(email);

		List<Order> myOrder = new ArrayList<>();
		myOrder.add(Order.builder().orderNumber(124).email("alihazma@whatever.com").firstName("George")
				.lastName("Bluth").productID(4444).build());
		when(ordersRepository.findAll()).thenReturn(myOrder);
		int productID = 4444;
		boolean isProductFound = false;
		for (Order o : myOrder) {
			if (o.getProductID() == productID) {
				isProductFound = true;
				break;
			}
		}
		isProductFound = ordersService.retrieveProductID(productID);

		Order orderToSave = Order.builder().orderNumber(14).email("alihazma@whatever.com").firstName("George")
				.lastName("Bluth").productID(4424).build();
		when(ordersRepository.save(orderToSave)).thenReturn(orderToSave);
		Order order = ordersService.createProduct(orderToSave);

		mockServer.verify();
		assertTrue(isProductFound);
		assertTrue(emailFound);
		assertThat(order).isNotNull();
	}

	@Test
	void should_throw_ordersException_falseEmail() throws JsonProcessingException {
		ApiResponse apiResponse = setApiResponse();

		final String responseString = mapper.writeValueAsString(apiResponse);
		mockServer.expect(ExpectedCount.manyTimes(), request -> {
			try {
				new URI("https://fakeURI.com");
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}).andExpect(method(HttpMethod.GET))
				.andRespond(withStatus(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(responseString));

		List<DataResponse> receievedExpectedData = ordersService.getApiResponseData();
		String email = "alihazma@whaever.com";
		boolean emailFound = false;
		emailFound = ordersService.retrieveEmail(email);

		List<Order> myOrder = new ArrayList<>();
		myOrder.add(Order.builder().orderNumber(124).email("alihazma@whatever.com").firstName("George")
				.lastName("Bluth").productID(4444).build());
		when(ordersRepository.findAll()).thenReturn(myOrder);
		int productID = 4444;
		boolean isProductFound = false;
		for (Order o : myOrder) {
			if (o.getProductID() == productID) {
				isProductFound = true;
				break;
			}
		}
		isProductFound = ordersService.retrieveProductID(productID);

		Order orderToSave = Order.builder().orderNumber(14).email("alihazmawhatever.com").firstName("George")
				.lastName("Bluth").productID(4424).build();
		when(ordersRepository.save(orderToSave)).thenReturn(orderToSave);
		Exception exception = assertThrows(OrdersException.class, () -> ordersService.createProduct(orderToSave));

		mockServer.verify();
		assertTrue(isProductFound);
		assertFalse(emailFound);
		assertEquals("Email is not correct", exception.getMessage());

	}

	private ApiResponse setApiResponse() {
		ApiResponse apiResponse = new ApiResponse();
		List<DataResponse> dataResponse = new ArrayList<>();
		dataResponse.add(new DataResponse(1, "alihazma@whatever.com", "Ali", "Hamza", "imguri"));
		dataResponse.add(new DataResponse(2, "alihamza2.0@whatever.com", "Ali", "Hamza 2.0", "imguri"));
		dataResponse.add(new DataResponse(3, "alihamza3.0@whatever.com", "Ali", "Hamza 3.0", "imguri"));
		apiResponse.setPage("1");
		apiResponse.setPerPage("4");
		apiResponse.setTotal("5");
		apiResponse.setTotalPages("2");
		apiResponse.setData(dataResponse);
		return apiResponse;
	}

}
