package com.shop.exceptions;

public class ShopException extends RuntimeException {

	private static final long serialVersionUID = 807238185585245943L;
	private String message;

	public ShopException(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
