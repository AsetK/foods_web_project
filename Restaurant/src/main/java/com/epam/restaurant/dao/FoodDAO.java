package com.epam.restaurant.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

import com.epam.restaurant.connectionpool.ConnectionPool;
import com.epam.restaurant.entity.Food;

public class FoodDAO {
	
	private static final int ILLEGAL_FOOD_ID = -1;
	private static final Logger LOGGER = Logger.getLogger(FoodDAO.class);
	private static final String SQL_CREATE_FOOD = "INSERT INTO foods (food_price) " + 
												  "VALUES(?)";
	private static final String SQL_CREATE_FOOD_TRANSLATION = "INSERT INTO foods_translation (food_id, language_id, food_name, food_description) " + 
															  "VALUES(?, ?, ?, ?)";
	private static final String SQL_UPDATE_FOOD_STATUS = "UPDATE foods " +
			 											 "SET food_status_id = ? " + 
														 "WHERE id = ? ";
	private static final String SQL_UPDATE_FOOD_PRICE = "UPDATE foods " +
			 											"SET food_price = ? " + 
														"WHERE id = ? ";
	private static final String SQL_SELECT_FOOD_STATUS_ID = "SELECT id " + 
													   "FROM food_status_table " +
													   "WHERE description like ?";
	private static final String SQL_SELECT_FOOD_ID = "SELECT food_id " + 
													 "FROM foods_translation " +
													 "WHERE food_name like ?";
	private static final String SQL_SELECT_FOOD_TRANSLATION = "SELECT food_name, food_description " + 
			 "FROM foods_translation " +
			 "WHERE food_id = ? AND language_id = ?";
	
	public void createFood(double price, Map<Integer,Food> multilingualFood)
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.retrieve();
		
		Savepoint savepoint = null;
		
		int generatedID = ILLEGAL_FOOD_ID;
		
		try (PreparedStatement cmdFood = conn.prepareStatement(SQL_CREATE_FOOD, Statement.RETURN_GENERATED_KEYS);
				PreparedStatement cmdFoodTranslation = conn.prepareStatement(SQL_CREATE_FOOD_TRANSLATION))
		{
			savepoint = conn.setSavepoint();
			conn.setAutoCommit(false);
			
			cmdFood.setDouble(1, price);
			cmdFood.executeUpdate();
			ResultSet result = cmdFood.getGeneratedKeys();
			
			if(result.next())
			{
				generatedID = result.getInt(1);
			}
			
			
			Set<Map.Entry<Integer, Food>> keysAndValues = multilingualFood.entrySet();
			
			for(Map.Entry<Integer, Food> keyAndValue : keysAndValues)
			{
				int languageID = keyAndValue.getKey();
				Food food = keyAndValue.getValue();
				
				cmdFoodTranslation.setInt(1, generatedID);
				cmdFoodTranslation.setInt(2, languageID);
				cmdFoodTranslation.setString(3, food.getName());
				cmdFoodTranslation.setString(4, food.getDescription());
				cmdFoodTranslation.executeUpdate();
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
	}
	
	public void updateFoodStatus(String foodName, String status)
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.retrieve();
		
		int statusID = this.selectStatusID(status);
		int foodID = this.selectFoodID(foodName);
				
		try (PreparedStatement cmd = conn.prepareStatement(SQL_UPDATE_FOOD_STATUS))
		{
			cmd.setInt(1, statusID);
			cmd.setInt(2, foodID);
			cmd.executeUpdate();
		} 
		catch (SQLException exc) 
		{
			LOGGER.error(exc);
		}
		
		pool.putback(conn);
	}
	
	public void updateFoodPrice(String foodName, double foodNewPrice)
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.retrieve();
		
		int foodID = this.selectFoodID(foodName);
				
		try (PreparedStatement cmd = conn.prepareStatement(SQL_UPDATE_FOOD_PRICE))
		{
			cmd.setDouble(1, foodNewPrice);
			cmd.setInt(2, foodID);
			cmd.executeUpdate();
		} 
		catch (SQLException exc) 
		{
			LOGGER.error(exc);
		}
		
		pool.putback(conn);
	}
	
	public int selectStatusID(String status)
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.retrieve();
		
		int statusID = ILLEGAL_FOOD_ID;
		
		try (PreparedStatement cmd = conn.prepareStatement(SQL_SELECT_FOOD_STATUS_ID))
		{
			cmd.setString(1, status);
			ResultSet result =  cmd.executeQuery();
			result.next();
			statusID = result.getInt(1);
		}
		catch (SQLException exc) 
		{
			LOGGER.error(exc);
		}
		
		pool.putback(conn);
		
		return statusID;
	}
	
	public int selectFoodID(String foodName)
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.retrieve();
		
		int foodID = ILLEGAL_FOOD_ID;
		
		try (PreparedStatement cmd = conn.prepareStatement(SQL_SELECT_FOOD_ID))
		{
			cmd.setString(1, foodName);
			ResultSet result =  cmd.executeQuery();
			if(result.next())
			{
				foodID = result.getInt(1);
			}
		} 
		catch (SQLException exc) 
		{
			LOGGER.error(exc);
		}
		
		pool.putback(conn);
		
		return foodID;
	}
	
	public void selectFoodTranslation(Food food, int languageID)
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.retrieve();
		
		try (PreparedStatement cmd = conn.prepareStatement(SQL_SELECT_FOOD_TRANSLATION))
		{
			cmd.setInt(1, food.getId());
			cmd.setInt(2, languageID);
			ResultSet result =  cmd.executeQuery();
			if(result.next())
			{
				food.setName(result.getString(1));
				food.setDescription(result.getString(2));
			}
		} 
		catch (SQLException exc) 
		{
			LOGGER.error(exc);
		}
		
		pool.putback(conn);
	}

}
