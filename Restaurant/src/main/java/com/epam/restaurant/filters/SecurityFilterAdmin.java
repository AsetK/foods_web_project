package com.epam.restaurant.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SecurityFilterAdmin implements Filter{
	
	private static final String STATUS_ATTRIBUTE_NAME = "status"; 
	private static final String ADMIN_STATUS = "admin";
	private static final String AUTH_PAGE = "";
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException, ServletException 
	{
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		final HttpSession session = req.getSession();
		String status = (String) session.getAttribute(STATUS_ATTRIBUTE_NAME);
		
		if(status == null)
		{
			System.out.println("security admin");
			res.sendRedirect(req.getContextPath() + AUTH_PAGE);
		}
		else if(status.equals(ADMIN_STATUS))
		{
			fc.doFilter(request, response);
		}
	}

}
