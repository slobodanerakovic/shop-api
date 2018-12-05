package com.shop.service.app.util;

import java.util.List;

import com.shop.exceptions.ShopException;

public class Preconditions {

	public static void validatePropertyForNull(Object property, String message) {
		if (property == null)
			throw new ShopException(message);
	}

	public static void isEmpty(List<? extends Object> list, String message) {
		// TODO Auto-generated method stub
		if (list.size() == 0)
			throw new ShopException(message);
	}
}
