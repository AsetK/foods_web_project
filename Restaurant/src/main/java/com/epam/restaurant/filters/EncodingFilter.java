package com.epam.restaurant.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class EncodingFilter implements Filter{
	
	private static final String CODE = "UTF-8";

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException, ServletException 
	{
		request.setCharacterEncoding(CODE);
		response.setCharacterEncoding(CODE);
		fc.doFilter(request, response);
	}

}
