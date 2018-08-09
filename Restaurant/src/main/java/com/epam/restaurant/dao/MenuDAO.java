package com.epam.restaurant.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

import com.epam.restaurant.connectionpool.ConnectionPool;
import com.epam.restaurant.entity.Food;
import com.epam.restaurant.entity.Menu;
import com.epam.restaurant.entityservice.MenuFoodAdder;

public class MenuDAO {
	
	private static final Logger LOGGER = Logger.getLogger(MenuDAO.class);
	private static final String SQL_SELECT_MENU = "SELECT foods.id, foods_translation.food_name, foods_translation.food_description, foods.food_price " + 
												  "FROM foods " + 
												  "INNER JOIN foods_translation " + 
												  "ON foods.id = foods_translation.food_id " + 
												  "WHERE foods_translation.language_id = ? AND foods.food_status_id = ?";
	private static final String SQL_SELECT_ALL_FOODS = "SELECT foods.id, foods_translation.food_name, foods_translation.food_description, foods.food_price, food_status_table.description " + 
													   "FROM foods " + 
													   "INNER JOIN foods_translation " + 
													   "ON foods.id = foods_translation.food_id " + 
													   "INNER JOIN food_status_table " + 
													   "ON foods.food_status_id = food_status_table.id " + 
													   "WHERE foods_translation.language_id = ? AND food_status_table.language_id = ? " + 
													   "ORDER BY foods.id ";

	public Menu select(int languageID, int availableStatusID)
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.retrieve();
		
		Menu menu = new Menu();
		
		try (PreparedStatement cmd = conn.prepareStatement(SQL_SELECT_MENU))
		{
			cmd.setInt(1, languageID);
			cmd.setInt(2, availableStatusID);
			ResultSet result =  cmd.executeQuery();
			
			while(result.next())
			{
				int foodID = result.getInt(1);
				String foodName = result.getString(2);
				String foodDescription = result.getString(3);
				Double foodPrice = result.getDouble(4);
			
				Food food = new Food();
				food.setId(foodID).setName(foodName).setDescription(foodDescription).setPrice(foodPrice);
				menu.setFoods(new MenuFoodAdder().addFood(menu, food));
			}
		}
		catch (SQLException exc) 
		{
			LOGGER.error(exc);
		}
		
		pool.putback(conn);
		
		return menu;	
	}
	
	public Menu selectAllFoods(int languageID)
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.retrieve();
		
		Menu menu = new Menu();
		
		try (PreparedStatement cmd = conn.prepareStatement(SQL_SELECT_ALL_FOODS))
		{
			cmd.setInt(1, languageID);
			cmd.setInt(2, languageID);
			ResultSet result =  cmd.executeQuery();
			
			while(result.next())
			{
				int foodID = result.getInt(1);
				String foodName = result.getString(2);
				String foodDescription = result.getString(3);
				Double foodPrice = result.getDouble(4);
				String foodAvailableStatus = result.getString(5);
			
				Food food = new Food();
				food.setId(foodID).setName(foodName).setDescription(foodDescription).setPrice(foodPrice).setAvailableStatus(foodAvailableStatus);
				menu.setFoods(new MenuFoodAdder().addFood(menu, food));
			}
		}
		catch (SQLException exc) 
		{
			LOGGER.error(exc);
		}
		
		pool.putback(conn);
		
		return menu;	
	}
	
}
