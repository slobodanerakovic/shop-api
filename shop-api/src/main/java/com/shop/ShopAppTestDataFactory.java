package com.shop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.shop.db.model.Product;
import com.shop.service.app.OrderService;
import com.shop.service.app.ProductService;
import com.shop.service.app.util.SecurityService;
import com.shop.util.Constants;
import com.shop.ws.ProductAPI;
import com.shop.ws.dto.OrderDTO;
import com.shop.ws.dto.ProductDTO;

/**
 * 
 * @author Slobodan Erakovic This class is only used for initialization of test
 *         data for frontend to be visible
 *
 */
@RestController
@RequestMapping("/init")
public class ShopAppTestDataFactory {
	private static final Logger LOG = LoggerFactory.getLogger(ProductAPI.class);

	@Autowired
	protected ProductService productService;

	@Autowired
	protected OrderService orderService;

	@Autowired
	private SecurityService securityService;

	@GetMapping(value = "/")
	public void createTestData() {
		LOG.info("start factory for creation of dummy data...");
		ProductDTO apple = new ProductDTO();
		apple.setName("Apple");
		apple.setPrice(99.9);

		ProductDTO cherry = new ProductDTO();
		cherry.setName("Cherry");
		cherry.setPrice(55.9);

		ProductDTO plum = new ProductDTO();
		plum.setName("Plum");
		plum.setPrice(15.9);

		Product cherrySaved = productService.createProduct(cherry);
		Product appleSaved = productService.createProduct(apple);
		Product plumSaved = productService.createProduct(plum);

		plum.setId(securityService.encryptDES64(Constants.SECRET_KEY, Constants.IV_KEY, plumSaved.getId().toString()));
		apple.setId(
				securityService.encryptDES64(Constants.SECRET_KEY, Constants.IV_KEY, appleSaved.getId().toString()));
		cherry.setId(
				securityService.encryptDES64(Constants.SECRET_KEY, Constants.IV_KEY, cherrySaved.getId().toString()));

		OrderDTO plumOrder = new OrderDTO();
		plumOrder.setBuyerEmail("plumeater@yahoo.com");

		plumOrder.setProducts(Lists.newArrayList(plum));
		orderService.createOrder(plumOrder);

		OrderDTO cherryAppleOrder = new OrderDTO();
		cherryAppleOrder.setBuyerEmail("cherry_apple@yahoo.com");
		cherryAppleOrder.setProducts(Lists.newArrayList(cherry, apple));
		orderService.createOrder(cherryAppleOrder);
		LOG.info("finished factory for creation of dummy data...");
	}
}
