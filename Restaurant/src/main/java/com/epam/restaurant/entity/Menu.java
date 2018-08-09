package com.epam.restaurant.entity;

import java.util.LinkedList;
import java.util.List;

public class Menu {
	
	private List<Food> foods = new LinkedList<>();
	
	public List<Food> getFoods()
	{
		return this.foods;
	}
	
	public Menu setFoods(List<Food> foods)
	{
		this.foods = foods;
		return this;
	}
	
}
