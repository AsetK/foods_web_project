package com.epam.restaurant.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthFilter implements Filter{
	
	private static final String STATUS_ATTRIBUTE_NAME = "status";
	private static final String USER_STATUS = "user";
	private static final String ADMIN_STATUS = "admin";
	private static final String USER_PAGE = "/Menu";
	private static final String ADMIN_PAGE = "/OrdersList";
	private static final String ALREADY_LOGGED_IN= "alreadyLoggedIn";
	

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException, ServletException 
	{
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		String status = (String) req.getSession().getAttribute(STATUS_ATTRIBUTE_NAME);
		
		if(status == null)
		{
			fc.doFilter(request, response);
		}
		else
		{
			req.getSession().setAttribute(ALREADY_LOGGED_IN, true);
			if(status.equals(USER_STATUS))
			{
				res.sendRedirect(req.getContextPath() + USER_PAGE);
			}
			else if(status.equals(ADMIN_STATUS))
			{
				res.sendRedirect(req.getContextPath() + ADMIN_PAGE);
			}
		}

	}

}
