package com.products.orders.resreq;

import java.io.Serializable;
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
public class DataResponse implements Serializable {

	private int id;
	private String email;
	@JsonAlias({ "first_name" })
	private String firstName;
	@JsonAlias({ "last_name" })
	private String lastName;
	private String avatar;

}
