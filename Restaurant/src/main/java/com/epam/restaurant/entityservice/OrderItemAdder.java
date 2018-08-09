package com.epam.restaurant.entityservice;

import java.util.Map;

import com.epam.restaurant.entity.Food;
import com.epam.restaurant.entity.Order;

public class OrderItemAdder {
	
	public Map<Food, Integer> addItem(Order order, Food food, int amount)
	{
		Map<Food, Integer> items = order.getItems();
		items.put(food, amount);
		return items;
	}

}
