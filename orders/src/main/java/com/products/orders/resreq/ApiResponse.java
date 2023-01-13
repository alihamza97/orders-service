package com.products.orders.resreq;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Component
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {
	private String page;
	@JsonAlias({ "per_page" })
	private String perPage;
	private String total;
	@JsonAlias({ "total_pages" })
	private String totalPages;
	private List<DataResponse> data;
}
