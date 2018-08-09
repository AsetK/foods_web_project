package com.epam.restaurant.commands;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import com.epam.restaurant.dao.LanguageDAO;
import com.epam.restaurant.dao.OrderDAO;
import com.epam.restaurant.entity.Order;

public class OrdersListCommand implements ActionCommand{
	
	private static final String PAGE = "/OrdersList";
	private static final String LANGUAGE_ATTRIBUTE = "language";
	private static final String ORDERS_ATTRIBUTE = "orders";
	private static final Logger LOGGER = Logger.getLogger(OrdersListCommand.class);
	private static final String LOGGER_TEXT = "Entered the orders list.";

	@Override
	public String execute(HttpServletRequest request) 
	{
		if(request.getParameter(LANGUAGE_ATTRIBUTE) != null) 
		{
			changeLanguage(request);
		}
		
		String language = (String) request.getSession().getAttribute(LANGUAGE_ATTRIBUTE);
		
		LanguageDAO languageDAO = new LanguageDAO();
		int languageID = languageDAO.selectLanguageID(language);
		
		OrderDAO orderDAO = new OrderDAO();
		List<Order> orders = orderDAO.selectAll(languageID);
		
		request.getSession().setAttribute(ORDERS_ATTRIBUTE, orders);
		
		LOGGER.info(LOGGER_TEXT);
		
		return request.getContextPath() + PAGE;
	}
	
	private void changeLanguage(HttpServletRequest request)
	{
		String language = request.getParameter(LANGUAGE_ATTRIBUTE);
		request.getSession().setAttribute(LANGUAGE_ATTRIBUTE, language);
	}

}
