package com.epam.restaurant.entity;

public class User {
	
	private String login;
	private String password;
	private String status;

	public String getLogin() 
	{
		return login;
	}
	
	public User setLogin(String login) 
	{
		this.login = login;
		return this;
	}
	
	public String getPassword() 
	{
		return password;
	}
	
	public User setPassword(String password) 
	{
		this.password = password;
		return this;
	}
	
	public String getStatus() 
	{
		return status;
	}
	
	public User setStatus(String status) 
	{
		this.status = status;
		return this;
	}
	
}
