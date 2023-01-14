package com.products.orders.resreq;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
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
