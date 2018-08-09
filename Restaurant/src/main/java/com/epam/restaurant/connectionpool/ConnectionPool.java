package com.epam.restaurant.connectionpool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import com.epam.restaurant.dao.FoodDAO;

public final class ConnectionPool {
	
	private static volatile ConnectionPool instance = null;
	private static final int CONNECTIONS_AMMOUNT= 5;
	private static final String CONNECTION_PROPERTY = "com.epam.properties.connection";
	private static final String CONNECTION_DRIVER_KEY = "connection.driver";
	private static final String CONNECTION_CONFIG_KEY = "connection.config";
	private static final String CONNECTION_LOGIN_KEY = "connection.login";
	private static final String CONNECTION_PASSWORD_KEY = "connection.password";
	private static final Logger LOGGER = Logger.getLogger(FoodDAO.class);
	private String connectionDriver;
	private String connectionConfig;
	private String connectionLogin;
	private String connectionPassword;
	private int connectionAmount;
	private BlockingQueue<Connection> availableConnections;
	
	private ConnectionPool()
	{
		ResourceBundle rb = ResourceBundle.getBundle(CONNECTION_PROPERTY);
		connectionDriver = rb.getString(CONNECTION_DRIVER_KEY);
		connectionConfig = rb.getString(CONNECTION_CONFIG_KEY);
		connectionLogin = rb.getString(CONNECTION_LOGIN_KEY);
		connectionPassword = rb.getString(CONNECTION_PASSWORD_KEY);
		
		try 
		{
			Class.forName(connectionDriver);
		} 
		catch (ClassNotFoundException exc) 
		{
			LOGGER.error(exc);
		}
		this.connectionAmount = CONNECTIONS_AMMOUNT;
		
		availableConnections = new ArrayBlockingQueue<>(CONNECTIONS_AMMOUNT);
		
		for(int i = 0; i < this.connectionAmount; i++)
			availableConnections.add(getConnection());
	}
	
	public static ConnectionPool getInstance()
	{
		if(instance == null)
		{
			synchronized(ConnectionPool.class)
			{
				if(instance == null)
				{
					instance = new ConnectionPool();
				}
			}
		}
		return instance;
	}
	
	private Connection getConnection()
	{
		Connection conn = null;
		try 
		{
			conn = DriverManager.getConnection(connectionConfig,connectionLogin,connectionPassword);
		} 
		catch (Exception exc) 
		{
			LOGGER.error(exc);
		}
		return conn;
	}
	
	public Connection retrieve() {
		
		Connection newConn = null;
		
		try 
		{
			newConn =  this.availableConnections.take();
		} 
		catch (InterruptedException exc) 
		{
			LOGGER.error(exc);
		}
		
		return newConn;
	}
	
	public void putback(Connection conn) 
	{
		if(conn != null)
		{
			try 
			{
				this.availableConnections.put(conn);
			} 
			catch (InterruptedException exc) 
			{
				LOGGER.error(exc);
			}
		}
	}
	
}
