package com.shop.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shop.db.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	Product findByName(String name);

	List<Product> findAllByActiveOrderByIdDesc(Boolean active);
}
