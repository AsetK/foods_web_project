package com.epam.restaurant.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

import com.epam.restaurant.dao.OrderDAO;
import com.epam.restaurant.entity.Order;

public class SubmitOrderCommand implements ActionCommand{
	
	private static final String PAGE = "/OrderIsDone";
	private static final String DEMO_ORDER_ATTRIBUTE = "demoOrder";
	private static final String ORDER_ID_ATTRIBUTE = "orderID";
	private static final String USER_ID_ATTRIBUTE = "userID";
	private static final String ALREADY_SUBMITTED_ATTRIBUTE = "alreadySubmitted";
	private static final Logger LOGGER = Logger.getLogger(SubmitOrderCommand.class);
	private static final String LOGGER_TEXT = "Order created.";

	@Override
	public String execute(HttpServletRequest request) 
	{	
		final HttpSession session = request.getSession();
		if(session.getAttribute(ORDER_ID_ATTRIBUTE) == null)
		{
			Order demoOrder = (Order) session.getAttribute(DEMO_ORDER_ATTRIBUTE);
			
			OrderDAO orderDAO = new OrderDAO();
			int userID = (int) session.getAttribute(USER_ID_ATTRIBUTE);
			int orderID = orderDAO.create(demoOrder, userID);
	    	
	    	session.setAttribute(ORDER_ID_ATTRIBUTE, orderID);
	    	
	    	boolean alreadySubmitted = false;
	    	session.setAttribute(ALREADY_SUBMITTED_ATTRIBUTE, alreadySubmitted);
	    	
	    	LOGGER.info(LOGGER_TEXT);
			
			return request.getContextPath() + PAGE;
		}
		else
		{
			boolean alreadySubmitted = true;
	    	session.setAttribute(ALREADY_SUBMITTED_ATTRIBUTE, alreadySubmitted);
	    	
			return request.getContextPath() + PAGE;
		}
	}

}
