package com.shop.test;

import org.assertj.core.util.Lists;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.shop.ws.dto.OrderDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OrderApiTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testFailedProductCreation() throws JSONException {

		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setBuyerEmail("test@yahoo.com");
		orderDTO.setProducts(Lists.newArrayList());

		HttpEntity<OrderDTO> requestEntity = new HttpEntity<OrderDTO>(orderDTO);
		ResponseEntity<String> entity = this.restTemplate.exchange("/api/orders/create", HttpMethod.POST, requestEntity,
				String.class);
		JSONObject status = new JSONObject(entity.getBody());

		Assert.assertFalse("Must be false", status.getBoolean("success"));
		Assert.assertTrue("Must be created status code", entity.getStatusCodeValue() == 400);
	}

	@Test
	public void testGetForGivenRange() throws JSONException {

		int from = 0;

		OrderDTO orderDTO = new OrderDTO();
		orderDTO.setBuyerEmail("test@yahoo.com");
		HttpEntity<OrderDTO> requestEntity = new HttpEntity<OrderDTO>(orderDTO);
		String url = "/api/orders/getall/" + from;

		ResponseEntity<String> entity = this.restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
		JSONObject status = new JSONObject(entity.getBody());

		Assert.assertTrue("Must be created status code", entity.getStatusCodeValue() == 404);
	}
}
