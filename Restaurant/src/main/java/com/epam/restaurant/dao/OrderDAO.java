package com.epam.restaurant.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

import com.epam.restaurant.connectionpool.ConnectionPool;
import com.epam.restaurant.entity.Food;
import com.epam.restaurant.entity.Order;
import com.epam.restaurant.entityservice.OrderItemAdder;

public class OrderDAO extends AbstractOrderDAO{
	
	private static final int ILLEGAL_ORDER_ID = -1;
	private static final Logger LOGGER = Logger.getLogger(OrderDAO.class);
	private static final String SQL_CREATE_ORDER = "INSERT INTO orders (order_total_price, user_id) " + 
												   "VALUES(?,?)";  
	private static final String SQL_CREATE_ORDER_FOODS = "INSERT INTO orders_foods (order_id, food_id, food_amount) " + 
														  "VALUES(?,?,?)";
	private static final String SQL_SELECT_ORDER = "SELECT " + 
			"orders.id, foods.id, foods_translation.food_name, foods_translation.food_description, foods.food_price, orders_foods.food_amount, " + 
			"orders.order_total_price, " +
			"order_status_table.description, " +
			"payment_status_table.description " + 
			"FROM orders " + 
			"	INNER JOIN orders_foods " + 
			"	ON orders.id = orders_foods.order_id " + 
			"		INNER JOIN foods " + 
			"		ON orders_foods.food_id = foods.id " + 
			"			INNER JOIN foods_translation " + 
			"			ON orders_foods.food_id = foods_translation.food_id " + 
			"				INNER JOIN order_status_table " + 
			"				ON orders.order_status_id = order_status_table.id " + 
			"					INNER JOIN payment_status_table " + 
			"					ON orders.payment_status_id = payment_status_table.id " + 
			"						INNER JOIN languages " + 
			"                        ON foods_translation.language_id = languages.id " + 
			"WHERE foods_translation.language_id = ? AND order_status_table.language_id = ? AND payment_status_table.language_id = ? " + 
			"AND orders.id = ? AND orders.user_id  = ? " +
			"ORDER BY foods.food_price DESC";
	private static final String SQL_SELECT_ALL = "SELECT " + 
			"orders.id, foods.id, foods_translation.food_name, foods_translation.food_description, foods.food_price, orders_foods.food_amount, " + 
			"orders.order_total_price, " +
			"order_status_table.description, " +
			"payment_status_table.description " + 
			"FROM orders " + 
			"	INNER JOIN orders_foods " + 
			"	ON orders.id = orders_foods.order_id " + 
			"		INNER JOIN foods " + 
			"		ON orders_foods.food_id = foods.id " + 
			"			INNER JOIN foods_translation " + 
			"			ON orders_foods.food_id = foods_translation.food_id " + 
			"				INNER JOIN order_status_table " + 
			"				ON orders.order_status_id = order_status_table.id " + 
			"					INNER JOIN payment_status_table " + 
			"					ON orders.payment_status_id = payment_status_table.id " + 
			"						INNER JOIN languages " + 
			"                        ON foods_translation.language_id = languages.id " + 
			"WHERE foods_translation.language_id = ? AND order_status_table.language_id = ? AND payment_status_table.language_id = ? " + 
			"ORDER BY orders.id, foods.food_price DESC";
	private static final String SQL_SELECT_USER_ORDERS = "SELECT " + 
			"orders.id, foods.id, foods_translation.food_name, foods_translation.food_description, foods.food_price, orders_foods.food_amount, " + 
			"orders.order_total_price, " +
			"order_status_table.description, " +
			"payment_status_table.description " + 
			"FROM orders " + 
			"	INNER JOIN orders_foods " + 
			"	ON orders.id = orders_foods.order_id " + 
			"		INNER JOIN foods " + 
			"		ON orders_foods.food_id = foods.id " + 
			"			INNER JOIN foods_translation " + 
			"			ON orders_foods.food_id = foods_translation.food_id " + 
			"				INNER JOIN order_status_table " + 
			"				ON orders.order_status_id = order_status_table.id " + 
			"					INNER JOIN payment_status_table " + 
			"					ON orders.payment_status_id = payment_status_table.id " + 
			"						INNER JOIN languages " + 
			"                        ON foods_translation.language_id = languages.id " + 
			"WHERE foods_translation.language_id = ? AND order_status_table.language_id = ? AND payment_status_table.language_id = ? AND orders.user_id = ? " + 
			"ORDER BY orders.id, foods.food_price DESC";
	private static final String SQL_UPDATE_ORDER_STATUS = "UPDATE orders " +
			 											  "SET order_status_id = ? " + 
														  "WHERE id = ?";
	private static final String SQL_UPDATE_PAYMENT_STATUS = "UPDATE orders " +
			 												"SET payment_status_id = ? " + 
															"WHERE id = ?";
	private static final String SQL_SELECT_PAID_STATUS_ID = "SELECT id " + 
															"FROM payment_status_table " +
															"WHERE description like ?";
	private static final String SQL_SELECT_EXECUTED_STATUS_ID = "SELECT id " + 
																"FROM order_status_table " +
																"WHERE description like ?";
	private static final String SQL_SELECT_PAYMENT_STATUS= "SELECT payment_status_id " + 
														   "FROM orders " +
														   "WHERE id = ? AND user_id = ?";
	private static final String SQL_SELECT_EXECUTION_STATUS= "SELECT order_status_id " + 
														   "FROM orders " +
														   "WHERE id = ?";
	
	public int create(Order order, int userID) 
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.retrieve();
		
		Savepoint savepoint = null;
		
		int generatedID = ILLEGAL_ORDER_ID;
		
		try (PreparedStatement cmdOrder = conn.prepareStatement(SQL_CREATE_ORDER, Statement.RETURN_GENERATED_KEYS);
			 PreparedStatement cmdOrderFoods = conn.prepareStatement(SQL_CREATE_ORDER_FOODS))
		{
			savepoint = conn.setSavepoint();
			conn.setAutoCommit(false);
			
			cmdOrder.setDouble(1, order.getTotalPrice());
			cmdOrder.setInt(2, userID);
			cmdOrder.executeUpdate();
			
			ResultSet result = cmdOrder.getGeneratedKeys();
			if(result.next())
			{
				generatedID = result.getInt(1);
				order.setOrderID(generatedID);
			}
			
			Map<Food, Integer> items = order.getItems();
			Set<Map.Entry<Food, Integer>> keysAndValues = items.entrySet();
			
			for(Map.Entry<Food, Integer> keyAndValue : keysAndValues)
			{
				int foodID = keyAndValue.getKey().getId();
				int foodAmmount = keyAndValue.getValue();
				
				cmdOrderFoods.setInt(1, order.getOrderID());
				cmdOrderFoods.setInt(2,foodID);
				cmdOrderFoods.setInt(3,foodAmmount);
				cmdOrderFoods.executeUpdate();
			}
			
			conn.commit();
			conn.setAutoCommit(true);
		} 
		catch (SQLException exc) 
		{
			LOGGER.error(exc);
			try 
			{
				conn.rollback(savepoint);
			} 
			catch (SQLException e) 
			{
				LOGGER.error(exc);
			}
		}
		
		pool.putback(conn);
		
		return generatedID;
	}
	
	public Order select(int languageID, int orderID, int userID)
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.retrieve();
		
		Order order = new Order();
		
		try (PreparedStatement cmd = conn.prepareStatement(SQL_SELECT_ORDER))
		{
			cmd.setInt(1, languageID);
			cmd.setInt(2, languageID);
			cmd.setInt(3, languageID);
			cmd.setInt(4, orderID);
			cmd.setInt(5, userID);
			ResultSet result =  cmd.executeQuery();
			
			while(result.next())
			{
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
		catch (SQLException exc) 
		{
			LOGGER.error(exc);
		}
		
		pool.putback(conn);
		
		return order;
	}
	
	public List<Order> selectAll(int languageID)
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.retrieve();
		
		List<Order> orders = new ArrayList<>();
		
		try (PreparedStatement cmd = conn.prepareStatement(SQL_SELECT_ALL))
		{
			cmd.setInt(1, languageID);
			cmd.setInt(2, languageID);
			cmd.setInt(3, languageID);
			ResultSet result =  cmd.executeQuery();
			
			addFoodsInOrders(result, orders);
			
		}
		catch (SQLException exc) 
		{
			LOGGER.error(exc);
		}
		
		pool.putback(conn);
		
		return orders;
	}
	
	public List<Order> selectUserOrders(int languageID, int userID)
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.retrieve();
		
		List<Order> orders = new ArrayList<>();
		
		try (PreparedStatement cmd = conn.prepareStatement(SQL_SELECT_USER_ORDERS))
		{
			cmd.setInt(1, languageID);
			cmd.setInt(2, languageID);
			cmd.setInt(3, languageID);
			cmd.setInt(4, userID);
			ResultSet result =  cmd.executeQuery();
			
			addFoodsInOrders(result, orders);
		}
		catch (SQLException exc) 
		{
			LOGGER.error(exc);
		}
		
		pool.putback(conn);
		
		return orders;
	}
	
	public void updateOrderStatus(int orderID, String executedStatus)
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.retrieve();
		
		int executedStatusID = this.selectExecutedStatusID(executedStatus);
		
		try (PreparedStatement cmd = conn.prepareStatement(SQL_UPDATE_ORDER_STATUS))
		{
			serveUpdateStatusMethods(cmd, executedStatusID, orderID);
		}
		catch (SQLException exc) 
		{
			LOGGER.error(exc);
		}
		
		pool.putback(conn);
	}
	
	public void updatePaymentStatus(int orderID, String paidStatus)
	{
		
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.retrieve();
		
		int paidStatusID = this.selectPaidStatusID(paidStatus);
		
		try (PreparedStatement cmd = conn.prepareStatement(SQL_UPDATE_PAYMENT_STATUS))
		{
			serveUpdateStatusMethods(cmd, paidStatusID, orderID);
		}
		catch (SQLException exc) 
		{
			LOGGER.error(exc);
		}
		
		pool.putback(conn);
	}
	
	public int selectPaidStatusID(String paidStatus)
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.retrieve();
		
		int paidStatusID = ILLEGAL_ORDER_ID;
		
		try (PreparedStatement cmd = conn.prepareStatement(SQL_SELECT_PAID_STATUS_ID))
		{
			paidStatusID = serveSelectStatusIDMethods(cmd, paidStatus);
		}
		catch (SQLException exc) 
		{
			LOGGER.error(exc);
		}
		
		pool.putback(conn);
		
		return paidStatusID;
	}
	
	public int selectExecutedStatusID(String executedStatus)
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.retrieve();
		
		int executedStatusID = ILLEGAL_ORDER_ID;
		
		try (PreparedStatement cmd = conn.prepareStatement(SQL_SELECT_EXECUTED_STATUS_ID))
		{
			executedStatusID = serveSelectStatusIDMethods(cmd, executedStatus);
		}
		catch (SQLException exc) 
		{
			LOGGER.error(exc);
		}
		
		pool.putback(conn);
		
		return executedStatusID;
	}
	
	public int selectPaymentStatus(int orderID, int userID)
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.retrieve();
		
		int paymentStatusID = ILLEGAL_ORDER_ID;
		
		try (PreparedStatement cmd = conn.prepareStatement(SQL_SELECT_PAYMENT_STATUS))
		{
			cmd.setInt(1, orderID);
			cmd.setInt(2, userID);
			ResultSet result =  cmd.executeQuery();
			result.next();
			paymentStatusID = result.getInt(1);
		}
		catch (SQLException exc) 
		{
			LOGGER.error(exc);
		}
		
		pool.putback(conn);
		
		return paymentStatusID;
	}
	
	public int selectExecutionStatus(int orderID)
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.retrieve();
		
		int executionStatusID = ILLEGAL_ORDER_ID;
		
		try (PreparedStatement cmd = conn.prepareStatement(SQL_SELECT_EXECUTION_STATUS))
		{
			cmd.setInt(1, orderID);
			ResultSet result =  cmd.executeQuery();
			result.next();
			executionStatusID = result.getInt(1);
		}
		catch (SQLException exc) 
		{
			LOGGER.error(exc);
		}
		
		pool.putback(conn);
		
		return executionStatusID;
	}

}
