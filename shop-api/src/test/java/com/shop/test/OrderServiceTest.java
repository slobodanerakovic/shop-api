package com.shop.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest extends ServiceParentTest {

	@Test
	public void testCreateOrder() {
		Assert.assertTrue("Order successfully created",
				orderService.createOrder(prepareMockOrderDTO("some@email.com")));
	}

	@Test
	public void testGetForGivenRange() {

		String from = "2018-08-20 00:00:00";
		String to = "2018-12-20 00:00:00";

		Assert.assertTrue("Must be one order at least", orderService.getAllForGivenPeriod(from, to).size() >= 1);

		from = "2018-08-20 00:00:00";
		to = "2018-09-20 00:00:00";

		Assert.assertTrue("Must be zero for given period", orderService.getAllForGivenPeriod(from, to).size() == 0);
	}

	@Test
	public void testTotalOrderAmount() {
		orderService.createOrder(prepareMockOrderDTO("some@email.com"));
		Double orderAmount = orderService.calculateTotalOrderAmount();
		Assert.assertTrue("Must be higher than 100Meeting", orderAmount > 100.0);
	}
}
