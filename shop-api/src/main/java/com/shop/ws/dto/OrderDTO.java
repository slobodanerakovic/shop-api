package com.shop.ws.dto;

import java.util.List;

public class OrderDTO {

	private List<ProductDTO> products;
	private String buyerEmail;

	public List<ProductDTO> getProducts() {
		return products;
	}

	public void setProducts(List<ProductDTO> products) {
		this.products = products;
	}

	public String getBuyerEmail() {
		return buyerEmail;
	}

	public void setBuyerEmail(String buyerEmail) {
		this.buyerEmail = buyerEmail;
	}

	@Override
	public String toString() {
		return "OrderDTO [products=" + products + ", buyerEmail=" + buyerEmail + "]";
	}

}
