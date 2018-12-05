package com.shop.ws;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.shop.exceptions.ShopException;
import com.shop.service.app.ProductService;
import com.shop.service.app.util.Preconditions;
import com.shop.ws.dto.ProductDTO;
import com.shop.ws.dto.Status;

@RestController
@RequestMapping("/api/products")
public class ProductAPI {
	private static final Logger LOG = LoggerFactory.getLogger(ProductAPI.class);

	@Autowired
	private ProductService productService;

	@PostMapping(value = "/create", produces = { "application/json" }, consumes = { "application/json" })
	@ResponseStatus(HttpStatus.CREATED)
	public Status createProduct(@RequestBody ProductDTO dto) {

		LOG.info("Create new product");
		Preconditions.validatePropertyForNull(dto.getName(), "Product's Name cannot be NULL");
		Preconditions.validatePropertyForNull(dto.getPrice(), "Product's Price cannot be NULL");

		boolean status = productService.createProduct(dto) != null ? true : false;

		return new Status(status);
	}

	@PutMapping(value = "/update", produces = { "application/json" }, consumes = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public Status updateProduct(@RequestBody ProductDTO dto) {
		LOG.info("Update existing product");

		boolean status = productService.updateProduct(dto);

		return new Status(status);
	}

	@DeleteMapping(value = "/delete/{id}", produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public Status deleteProduct(@PathVariable String id) {
		LOG.info("Delete existing product");

		Preconditions.validatePropertyForNull(id, "Product's Id cannot be NULL");

		boolean status = productService.deleteProduct(id);

		return new Status(status);
	}

	@GetMapping(value = "/getall", produces = { "application/json" })
	@ResponseStatus(HttpStatus.OK)
	public List<ProductDTO> getAll() {
		LOG.info("Retrieve all products");

		List<ProductDTO> products = productService.getAllProducts();

		return products;
	}

	/**
	 * Generic ProductException which thrown from any part of the code, will be
	 * handled here, returning Status object as a response, with details about what
	 * goes wrong
	 */
	@ExceptionHandler(ShopException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Status productDtoProblem(ShopException e) {

		return new Status(false, e.getMessage());
	}
}
