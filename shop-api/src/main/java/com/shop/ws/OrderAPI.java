package com.shop.ws;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.shop.exceptions.ShopException;
import com.shop.service.app.OrderService;
import com.shop.service.app.util.Preconditions;
import com.shop.ws.dto.OrderDTO;
import com.shop.ws.dto.Status;

@RestController
@RequestMapping("/api/orders")
public class OrderAPI {
	private static final Logger LOG = LoggerFactory.getLogger(OrderAPI.class);

	@Autowired
	private OrderService orderService;

	@PostMapping(value = "/create", produces = { "application/json" }, consumes = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public Status createOrder(@RequestBody OrderDTO dto) {

		LOG.info("Create new order");
		Preconditions.validatePropertyForNull(dto.getProducts(), "Product's Id cannot be NULL");
		Preconditions.isEmpty(dto.getProducts(), "Order's products cannot be empty");

		boolean status = orderService.createOrder(dto);

		return new Status(status);
	}

	@GetMapping(value = "getall/{from}/{to}", produces = { "application/json" })
	public List<OrderDTO> getAllOrdersForGivenPeriod(@PathVariable(required = true) String from,
			@PathVariable(required = true) String to) {

		return orderService.getAllForGivenPeriod(from, to);
	}

	@GetMapping(value = "totalOrderAmount")
	public Double calculateTotalOrderAmount() {

		return orderService.calculateTotalOrderAmount();
	}

	/**
	 * Generic OrderException which thrown from any part of the code, will be
	 * handled here, returning Status object as a response, with details about what
	 * goes wrong
	 */
	@ExceptionHandler(ShopException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Status orderDtoProblem(ShopException e) {
		return new Status(false, e.getMessage());
	}
}
