package com.shop.db.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shop.db.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query("SELECT o FROM Order o WHERE o.creationDate BETWEEN :from AND :to")
	List<Order> findAllForGivenPeriod(@Param("from") Date from, @Param("to") Date to);

	@Query("SELECT SUM(o.orderAmount) FROM Order o")
	Double findTotalOrderAmount();
}
