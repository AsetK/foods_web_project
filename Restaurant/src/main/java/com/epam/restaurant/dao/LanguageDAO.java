package com.epam.restaurant.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.epam.restaurant.connectionpool.ConnectionPool;

public class LanguageDAO {
	
	private static final int ILLEGAL_LANGUAGE_ID = -1;
	private static final Logger LOGGER = Logger.getLogger(LanguageDAO.class);
	private static final String SQL_SELECT_ALL =  "SELECT language_name " + 
												  "FROM languages";
	private static final String SQL_SELECT_LANGUAGE_ID = "SELECT languages.id " + 
														 "FROM languages " +
														 "WHERE language_name like ?";
	
	public List<String> selectAll()
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.retrieve();
		
		List<String> languages = new ArrayList<>();
	
		try (PreparedStatement cmd = conn.prepareStatement(SQL_SELECT_ALL))
		{
			ResultSet result =  cmd.executeQuery();
			
			while(result.next())
			{
				languages.add(result.getString(1));
			}
		}
		catch (SQLException exc) 
		{
			LOGGER.error(exc);
		}
		
		pool.putback(conn);
		
		return languages;
	}
	
	public int selectLanguageID(String language)
	{
		ConnectionPool pool = ConnectionPool.getInstance();
		Connection conn = pool.retrieve();
		
		int languageID = ILLEGAL_LANGUAGE_ID;
		
		try (PreparedStatement cmd = conn.prepareStatement(SQL_SELECT_LANGUAGE_ID))
		{
			cmd.setString(1, language);
			ResultSet result =  cmd.executeQuery();
			result.next();
			languageID = result.getInt(1);
		}
		catch (SQLException exc) 
		{
			LOGGER.error(exc);
		}
		
		pool.putback(conn);
		
		return languageID;
	}

}
