package com.epam.restaurant.entity;

public class Food {
	
	private int id;
	private String name;
	private String description;
	private double price;
	private String availableStatus;

	public int getId() 
	{
		return id;
	}
	
	public Food setId(int id) 
	{
		this.id = id;
		return this;
	}
	
	public String getName() 
	{
		return name;
	}
	
	public Food setName(String name) 
	{
		this.name = name;
		return this;
	}
	
	public String getDescription() 
	{
		return description;
	}
	
	public Food setDescription(String description) 
	{
		this.description = description;
		return this;
	}
	
	public double getPrice() 
	{
		return price;
	}
	
	public Food setPrice(double price) 
	{
		this.price = price;
		return this;
	}

	public String getAvailableStatus() 
	{
		return availableStatus;
	}

	public Food setAvailableStatus(String availableStatus) 
	{
		this.availableStatus = availableStatus;
		return this;
	}

}
