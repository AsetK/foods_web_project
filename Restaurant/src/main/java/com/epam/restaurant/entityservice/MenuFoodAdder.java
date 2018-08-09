package com.epam.restaurant.entityservice;

import java.util.List;

import com.epam.restaurant.entity.Food;
import com.epam.restaurant.entity.Menu;

public class MenuFoodAdder {
	
	public List<Food> addFood(Menu menu, Food food)
	{
		List<Food> foods = menu.getFoods();
		foods.add(food);
		return foods;
	}

}
