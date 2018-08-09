package com.epam.restaurant.commands;

import javax.servlet.http.HttpServletRequest;

public class InvalidateSessionCommand implements ActionCommand{
	
	private static final String AUTH_PAGE = "";

	@Override
	public String execute(HttpServletRequest request) 
	{
		request.getSession().invalidate();
		
		return request.getContextPath() + AUTH_PAGE;
	}

}
