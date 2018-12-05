package com.shop.test;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.shop.db.model.Product;
import com.shop.util.Constants;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest extends ServiceParentTest {

	@Test
	public void testCreateProduct() {
		String name = "Plum_" + r.nextLong();
		Assume.assumeTrue("Product creation should be true",
				productService.createProduct(mockDTO(name, 77.7, null)) != null);
		Product p = productRepository.findByName(name);

		Assert.assertTrue("Product should not be null", p != null);
		Assert.assertTrue("Product price should be 77.7", p.getPrice() == 77.7);
	}

	@Test
	public void testDeleteProductById() {
		String name = "Apple_" + r.nextLong();
		productService.createProduct(mockDTO(name, 10.0, null));
		Product p = productRepository.findByName(name);

		Assert.assertTrue("Product should not be null", p != null);
		Assert.assertTrue("Product should be", p.getActive());
		Assert.assertTrue("Product deletation should be true", productService.deleteProduct(
				securityService.encryptDES64(Constants.SECRET_KEY, Constants.IV_KEY, p.getId().toString())));
		p = productRepository.findByName(name);

		Assert.assertFalse("Product should NOT be", p.getActive());
	}

	@Test
	public void testUpdateProduct() {
		String name = "Cherry_" + r.nextLong();

		Assert.assertTrue("Product creation should be true",
				productService.createProduct(mockDTO(name, 77.7, null)) != null);

		Product p = productRepository.findByName(name);
		Assert.assertTrue("Product price should be 77.7", p.getPrice() == 77.7);

		Assert.assertTrue("Product update should be true",
				productService.updateProduct(mockDTO(p.getName(), 99.99, p.getId())));
		p = productRepository.findByName(name);
		Assert.assertTrue("Product price should be 99.99", p.getPrice() == 99.99);
		Assert.assertTrue("Product modification date shoulf not be null", p.getModificationDate() != null);
	}

}
