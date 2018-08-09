package com.epam.restaurant.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.epam.restaurant.commands.ActionCommand;
import com.epam.restaurant.commands.ActionFactory;


public class RestaurantController extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		processRequest(request, response);
	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String page = null;
		
		ActionFactory factory = new ActionFactory();
		ActionCommand command = factory.defineCommand(request);
		
		page = command.execute(request);
		
		if(page!=null)
		{
			response.sendRedirect(page);
		}
	}

}
