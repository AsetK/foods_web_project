package com.epam.restaurant.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.epam.restaurant.entity.Food;
import com.epam.restaurant.entity.Order;
import com.epam.restaurant.entityservice.OrderItemAdder;

public abstract class AbstractOrderDAO {
	
	private static final int ILLEGAL_ORDER_ID = -1;
	
	public void addFoodsInOrders(ResultSet result, List<Order> orders) throws SQLException
	{
		int currentOrderID = ILLEGAL_ORDER_ID;
		Order order = null;
		while(result.next())
		{
			if(currentOrderID != result.getInt(1))
			{
				order = new Order();
				orders.add(order);
				currentOrderID = result.getInt(1);
			}
			int order_id = result.getInt(1);
			int foodID = result.getInt(2);
			String foodName = result.getString(3);
			String foodDescription = result.getString(4);
			double foodPrice = result.getDouble(5);
			int foodAmount = result.getInt(6);
			double totalPrice = result.getDouble(7);
			String orderStatus = result.getString(8);
			String paymentStatus = result.getString(9);
			
			Food food = new Food();
			food.setId(foodID).setName(foodName).setDescription(foodDescription).setPrice(foodPrice);
			order.setItems(new OrderItemAdder().addItem(order, food, foodAmount));
			order.setOrderID(order_id).setOrderStatus(orderStatus).setPaymentStatus(paymentStatus).setTotalPrice(totalPrice);
		}
	}
	
	public void serveUpdateStatusMethods(PreparedStatement cmd, int statusID, int orderID) throws SQLException
	{
		cmd.setInt(1, statusID);
		cmd.setInt(2, orderID);
		cmd.executeUpdate();
	}
	
	public int serveSelectStatusIDMethods(PreparedStatement cmd, String status) throws SQLException
	{
		cmd.setString(1, status);
		ResultSet result =  cmd.executeQuery();
		result.next();
		
		return result.getInt(1);
	}

}
