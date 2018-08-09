package com.epam.restaurant.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

import com.epam.restaurant.connectionpool.ConnectionPool;
import com.epam.restaurant.entity.User;

public class UserDAO {
	private static final int ILLEGAL_USER_ID = -1;
	private static final Logger LOGGER = Logger.getLogger(OrderDAO.class);
	private static final String SQL_SELECT_USER = "SELECT * " +
			 									  "FROM users " +
			 									  "WHERE user_login LIKE ? AND user_password LIKE ?";
	private static final String SQL_SELECT_USER_STATUS = "SELECT user_status " +
			 											 "FROM users " +
			 											 "WHERE user_login LIKE ?";
	private static final String SQL_SELECT_USER_ID = "SELECT id " +
			 										 "FROM users " +
			 										 "WHERE user_login LIKE ?";
	
	public boolean select(User user)
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.retrieve();
		
		boolean isExist = false;
	
		try (PreparedStatement cmd = conn.prepareStatement(SQL_SELECT_USER))
		{
			cmd.setString(1, user.getLogin());
			cmd.setString(2, user.getPassword());
			ResultSet result = cmd.executeQuery();
			result.last();
			int rowNumber = result.getRow();
		
			if(rowNumber == 1)
			{
				isExist = true;
			}
			else
			{
				isExist = false;
			}
		} 
		catch (SQLException exc) 
		{
			LOGGER.error(exc);
		}
		
		pool.putback(conn);
		
		return isExist;
	}
	
	public String selectStatus(User user)
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.retrieve();
		
		String status = null;
		
		try (PreparedStatement cmd = conn.prepareStatement(SQL_SELECT_USER_STATUS))
		{
			cmd.setString(1, user.getLogin());
			ResultSet result = cmd.executeQuery();
			
			result.last();
			int rowNumber = result.getRow();
			
			if(rowNumber == 1)
			{
				status = result.getString(1);
			}
		} 
		catch (SQLException exc) 
		{
			LOGGER.error(exc);
		}
		
		pool.putback(conn);
		
		return status;
	}
	
	public int selectID(User user)
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.retrieve();
		
		int id = ILLEGAL_USER_ID;
		
		try (PreparedStatement cmd = conn.prepareStatement(SQL_SELECT_USER_ID))
		{
			cmd.setString(1, user.getLogin());
			ResultSet result = cmd.executeQuery();
			
			result.last();
			int rowNumber = result.getRow();
			
			if(rowNumber == 1)
			{
				id = result.getInt(1);
			}
		} 
		catch (SQLException exc) 
		{
			LOGGER.error(exc);
		}
		
		pool.putback(conn);
		
		return id;
	}

}
