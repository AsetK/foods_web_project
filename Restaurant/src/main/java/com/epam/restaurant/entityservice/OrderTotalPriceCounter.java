package com.epam.restaurant.entityservice;

import java.util.Map;
import java.util.Set;

import com.epam.restaurant.entity.Food;
import com.epam.restaurant.entity.Order;

public class OrderTotalPriceCounter {
	
	public double countTotalPrice(Order order)
	{
		double total = 0;
		Set<Map.Entry<Food, Integer>> keysAndValues = order.getItems().entrySet();
		for(Map.Entry<Food, Integer> keyAndValue : keysAndValues)
		{
			total += keyAndValue.getKey().getPrice()*keyAndValue.getValue();
		}
		
		return total;
	}

}
