package com.shop.service.app;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.DoubleAdder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.shop.db.model.Order;
import com.shop.db.model.Product;
import com.shop.db.repository.OrderRepository;
import com.shop.db.repository.ProductRepository;
import com.shop.service.app.util.SecurityService;
import com.shop.util.Constants;
import com.shop.ws.dto.OrderDTO;
import com.shop.ws.dto.ProductDTO;

@Service
public class OrderService {
	private static final Logger LOG = LoggerFactory.getLogger(OrderService.class);

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private SecurityService securityService;

	public boolean createOrder(OrderDTO dto) {

		LOG.info("Creating order for {} products", dto.getProducts().size());
		Order order = new Order();
		order.setBuyerEmail(dto.getBuyerEmail());
		Set<Product> products = Sets.newHashSet();

		DoubleAdder orderAmount = new DoubleAdder();
		dto.getProducts().forEach(pdto -> {
			Long id = Long
					.parseLong(securityService.decryptDES64(Constants.SECRET_KEY, Constants.IV_KEY, pdto.getId()));

			Optional<Product> optProduct = productRepository.findById(id);

			if (optProduct.isPresent()) {

				Product product = optProduct.get();
				orderAmount.add(product.getPrice());
				products.add(product);
			}
		});

		order.setOrderAmount(orderAmount.doubleValue());
		order.setProducts(products);
		orderRepository.save(order);

		return true;
	}

	@Transactional
	public List<OrderDTO> getAllForGivenPeriod(String from, String to) {
		Date fromDate = null;
		Date toDate = null;
		try {
			fromDate = Constants.FORMATTER_DATETIME.parse(from);
			toDate = Constants.FORMATTER_DATETIME.parse(to);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<Order> orders = orderRepository.findAllForGivenPeriod(fromDate, toDate);
		List<OrderDTO> dtos = Lists.newArrayList();

		orders.forEach(o -> {

			OrderDTO dto = new OrderDTO();
			dto.setBuyerEmail(o.getBuyerEmail());
			List<ProductDTO> orderProducts = Lists.newArrayList();

			Set<Product> products = o.getProducts();

			products.forEach(p -> {
				ProductDTO pdto = new ProductDTO();
				pdto.setName(p.getName());
				pdto.setPrice(p.getPrice());
				pdto.setId(securityService.encryptDES64(Constants.SECRET_KEY, Constants.IV_KEY, p.getId().toString()));

				orderProducts.add(pdto);
			});
			dto.setProducts(orderProducts);

			dtos.add(dto);
		});
		return dtos;
	}

	public Double calculateTotalOrderAmount() {
		Double total = orderRepository.findTotalOrderAmount();
		LOG.info("Total order amount calculated={}", total);

		return total;
	}

}
