package com.epam.restaurant.entity;

import java.util.LinkedHashMap;
import java.util.Map;

public class Order {
	
	private int orderID;
	private Map<Food, Integer> items = new LinkedHashMap<Food, Integer>();
	private String orderStatus;
	private String paymentStatus;
	private double totalPrice;
	
	public int getOrderID() 
	{
		return orderID;
	}

	public Order setOrderID(int orderID) 
	{
		this.orderID = orderID;
		return this;
	}
	
	public Map<Food, Integer> getItems()
	{
		return this.items;
	}
	
	public Order setItems(Map<Food, Integer> items)
	{
		this.items = items;
		return this;
	}
	
	public String getOrderStatus() 
	{
		return orderStatus;
	}

	public Order setOrderStatus(String orderStatus) 
	{
		this.orderStatus = orderStatus;
		return this;
	}

	public String getPaymentStatus() 
	{
		return paymentStatus;
	}

	public Order setPaymentStatus(String paymentStatus) 
	{
		this.paymentStatus = paymentStatus;
		return this;
	}

	
	public double getTotalPrice() 
	{
		return totalPrice;
	}

	public Order setTotalPrice(double totalPrice) 
	{
		this.totalPrice = totalPrice;
		return this;
	}
	
}
