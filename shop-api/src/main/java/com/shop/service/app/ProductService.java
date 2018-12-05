package com.shop.service.app;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.shop.db.model.Product;
import com.shop.db.repository.ProductRepository;
import com.shop.service.app.util.SecurityService;
import com.shop.util.Constants;
import com.shop.ws.dto.ProductDTO;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	/**
	 * SECURITY NOTE <br />
	 * <br />
	 * I do not like to expose externally any sensitive data. Here, however, id of
	 * the products are kind a very sensible since one might missuse it, knowing for
	 * example pattern of products Id. For this purpose I set encryption of id
	 * before sending to client (regardless is it java script or some another
	 * application calling this web service). Just for proof of concept, I did
	 * 64/bit DES encryption of sensitive data.
	 */
	@Autowired
	private SecurityService securityService;

	public Product createProduct(ProductDTO dto) {
		Product product = new Product();
		product.setName(dto.getName());
		product.setPrice(dto.getPrice());
		product.setActive(true);

		Product saved = productRepository.save(product);

		return saved;
	}

	@Transactional
	public boolean updateProduct(ProductDTO dto) {
		Long id = Long.parseLong(securityService.decryptDES64(Constants.SECRET_KEY, Constants.IV_KEY, dto.getId()));

		Optional<Product> optProduct = productRepository.findById(id);

		if (optProduct.isPresent()) {

			Product product = optProduct.get();
			product.setModificationDate(new Date());

			if (!Strings.isNullOrEmpty(dto.getName()) && !dto.getName().equals(product.getName())) {
				product.setName(dto.getName());
			}

			if (dto.getPrice() != null && !dto.getPrice().equals(product.getPrice())) {
				product.setPrice(dto.getPrice());
			}
			return true;
		} else {
			return false;
		}
	}

	@Transactional
	public boolean deleteProduct(String encId) {
		Long id = Long.parseLong(securityService.decryptDES64(Constants.SECRET_KEY, Constants.IV_KEY, encId));

		Optional<Product> optProduct = productRepository.findById(id);

		if (optProduct.isPresent()) {
			Product product = optProduct.get();
			product.setActive(false);
			product.setModificationDate(new Date());

			return true;
		} else {
			return false;
		}
	}

	public List<ProductDTO> getAllProducts() {

		List<ProductDTO> dtos = Lists.newArrayList();
		List<Product> allActiveProducts = productRepository.findAllByActiveOrderByIdDesc(true);
		allActiveProducts.forEach(p -> {

			ProductDTO dto = new ProductDTO();
			dto.setName(p.getName());
			dto.setPrice(p.getPrice());
			dto.setId(securityService.encryptDES64(Constants.SECRET_KEY, Constants.IV_KEY, p.getId().toString()));
			dtos.add(dto);
		});

		return dtos;
	}

}