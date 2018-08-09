package com.epam.restaurant.commands;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import com.epam.restaurant.dao.LanguageDAO;
import com.epam.restaurant.dao.OrderDAO;
import com.epam.restaurant.entity.Order;

public class BillCommand implements ActionCommand{
	
	private static final String PAGE = "/Bill";
	private static final String USER_ID_ATTRIBUTE = "userID";
	private static final String ORDER_ID_ATTRIBUTE = "orderID";
	private static final String LANGUAGE_ATTRIBUTE = "language";
	private static final String ORDER_ATTRIBUTE = "order";
	private static final Logger LOGGER = Logger.getLogger(BillCommand.class);
	private static final String LOGGER_TEXT = "Bill prepared.";

	@Override
	public String execute(HttpServletRequest request) 
	{	
		if(request.getParameter(LANGUAGE_ATTRIBUTE) != null) 
		{
			changeLanguage(request);
		}
		
		if(request.getParameter(ORDER_ID_ATTRIBUTE) != null) // from userorders.jsp
		{
			setCurrentOrder(request);
		}
		
		if(request.getSession().getAttribute(ORDER_ID_ATTRIBUTE) == null) // no data to translate
		{
			return request.getContextPath() + PAGE;
		}
		
		int userID = (int) request.getSession().getAttribute(USER_ID_ATTRIBUTE);
		int orderID = (int) request.getSession().getAttribute(ORDER_ID_ATTRIBUTE);
		
		String language = (String) request.getSession().getAttribute(LANGUAGE_ATTRIBUTE);
		
		LanguageDAO languageDAO = new LanguageDAO();
		int languageID = languageDAO.selectLanguageID(language);
		
		OrderDAO orderDAO = new OrderDAO();
		Order order = orderDAO.select(languageID, orderID, userID);
    	
    	request.getSession().setAttribute(ORDER_ATTRIBUTE, order);
    	
    	LOGGER.info(LOGGER_TEXT);
		
		return request.getContextPath() + PAGE;
	}
	
	private void changeLanguage(HttpServletRequest request)
	{
		String language = request.getParameter(LANGUAGE_ATTRIBUTE);
		request.getSession().setAttribute(LANGUAGE_ATTRIBUTE, language);
	}
	
	private void setCurrentOrder(HttpServletRequest request)
	{
		String paramter = request.getParameter(ORDER_ID_ATTRIBUTE);
		int orderID = Integer.valueOf(paramter);
		request.getSession().setAttribute(ORDER_ID_ATTRIBUTE, orderID);
	}
	
}
