package com.shop.test;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;
import com.shop.db.model.Product;
import com.shop.db.repository.ProductRepository;
import com.shop.service.app.OrderService;
import com.shop.service.app.ProductService;
import com.shop.service.app.util.SecurityService;
import com.shop.util.Constants;
import com.shop.ws.dto.OrderDTO;
import com.shop.ws.dto.ProductDTO;

public class ServiceParentTest {

	@Autowired
	protected ProductService productService;

	@Autowired
	protected OrderService orderService;

	@Autowired
	protected ProductRepository productRepository;

	@Autowired
	protected SecurityService securityService;

	protected Random r = new Random();

	protected ProductDTO mockDTO(String name, Double price, Long id) {
		ProductDTO dto = new ProductDTO();
		dto.setName(name);
		dto.setPrice(price);

		if (id != null)
			dto.setId(securityService.encryptDES64(Constants.SECRET_KEY, Constants.IV_KEY, id.toString()));

		return dto;
	}

	protected OrderDTO prepareMockOrderDTO(String email) {
		OrderDTO dto = new OrderDTO();
		dto.setBuyerEmail(r.nextInt() + "some@email.com");

		List<ProductDTO> products = Lists.newArrayList();

		String name = "Apple_" + r.nextLong();
		productService.createProduct(mockDTO(name, 134.0, null));
		Product p = productRepository.findByName(name);
		ProductDTO pdto = new ProductDTO();
		pdto.setId(securityService.encryptDES64(Constants.SECRET_KEY, Constants.IV_KEY, p.getId().toString()));
		pdto.setName(name);
		pdto.setPrice(p.getPrice());
		products.add(pdto);

		name = "Cherry_" + r.nextLong();
		productService.createProduct(mockDTO(name, 20.0, null));
		Product p2 = productRepository.findByName(name);
		ProductDTO pdto2 = new ProductDTO();
		pdto2.setId(securityService.encryptDES64(Constants.SECRET_KEY, Constants.IV_KEY, p2.getId().toString()));
		pdto2.setName(name);
		pdto2.setPrice(p.getPrice());
		products.add(pdto2);

		dto.setProducts(products);
		return dto;
	}
}
