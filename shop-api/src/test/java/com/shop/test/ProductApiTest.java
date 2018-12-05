package com.shop.test;

import java.util.Random;

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

import com.shop.ws.dto.ProductDTO;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProductApiTest {

	@Autowired
	private TestRestTemplate restTemplate;
	private Random r = new Random();

	@Test
	public void testFailedProductCreation_MissingName() throws JSONException {

		ProductDTO dto = new ProductDTO();
		dto.setPrice(34.23);
		HttpEntity<ProductDTO> requestEntity = new HttpEntity<ProductDTO>(dto);
		ResponseEntity<String> entity = this.restTemplate.exchange("/api/products/create", HttpMethod.POST,
				requestEntity, String.class);
		JSONObject status = new JSONObject(entity.getBody());

		Assert.assertFalse("Must be false", status.getBoolean("success"));
		Assert.assertTrue("Must be bad request status code", entity.getStatusCodeValue() == 400);
	}

	@Test
	public void testFailedProductCreation_MissingPrice() throws JSONException {
		String name = "Plum_" + r.nextLong();
		ProductDTO dto = new ProductDTO();
		dto.setName(name);

		HttpEntity<ProductDTO> requestEntity = new HttpEntity<ProductDTO>(dto);
		ResponseEntity<String> entity = this.restTemplate.exchange("/api/products/create", HttpMethod.POST,
				requestEntity, String.class);
		JSONObject status = new JSONObject(entity.getBody());

		Assert.assertTrue("Must be bad request status code", entity.getStatusCodeValue() == 400);
		Assert.assertFalse("Must be false", status.getBoolean("success"));

	}
}
