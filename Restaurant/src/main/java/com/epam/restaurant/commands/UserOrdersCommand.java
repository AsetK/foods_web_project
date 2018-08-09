package com.epam.restaurant.commands;

import java.util.List;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.epam.restaurant.dao.LanguageDAO;
import com.epam.restaurant.dao.OrderDAO;
import com.epam.restaurant.entity.Order;

public class UserOrdersCommand implements ActionCommand{
	
	private static final String PAGE = "/UserOrders";
	private static final String USER_ID_ATTRIBUTE = "userID";
	private static final String LANGUAGE_ATTRIBUTE = "language";
	private static final String USER_ORDERS_ATTRIBUTE = "userOrders";
	private static final Logger LOGGER = Logger.getLogger(UserOrdersCommand.class);
	private static final String LOGGER_TEXT = "Entered the user orders.";

	@Override
	public String execute(HttpServletRequest request) 
	{
		if(request.getParameter(LANGUAGE_ATTRIBUTE) != null) 
		{
			changeLanguage(request);
		} 
		
		String language = (String) request.getSession().getAttribute(LANGUAGE_ATTRIBUTE);
		int userID = (int) request.getSession().getAttribute(USER_ID_ATTRIBUTE);
		
		LanguageDAO languageDAO = new LanguageDAO();
		int languageID = languageDAO.selectLanguageID(language);
		
		OrderDAO orderDAO = new OrderDAO();
		List<Order> userOrders = orderDAO.selectUserOrders(languageID, userID);
		
		request.getSession().setAttribute(USER_ORDERS_ATTRIBUTE, userOrders);
		
		LOGGER.info(LOGGER_TEXT);
		
		return request.getContextPath() + PAGE;
	}
	
	private void changeLanguage(HttpServletRequest request)
	{
		String language = request.getParameter(LANGUAGE_ATTRIBUTE);
		request.getSession().setAttribute(LANGUAGE_ATTRIBUTE, language);
	}

}
