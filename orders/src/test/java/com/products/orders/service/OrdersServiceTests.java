package com.products.orders.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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
//
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
