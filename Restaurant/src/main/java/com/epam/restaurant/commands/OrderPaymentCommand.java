package com.epam.restaurant.commands;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import com.epam.restaurant.dao.OrderDAO;


public class OrderPaymentCommand implements ActionCommand{
	
	private static final String PAGE = "./bill";
	private static final String USER_ID_ATTRIBUTE = "userID";
	private static final String ORDER_ID_ATTRIBUTE = "orderID";
	private static final String PAID_STATUS = "Paid";
	private static final String PAID_BEFORE_ATTRIBUTE = "paidBefore";
	private static final Logger LOGGER = Logger.getLogger(OrderPaymentCommand.class);
	private static final String LOGGER_TEXT = "Order has been paid";

	@Override
	public String execute(HttpServletRequest request) 
	{
		int userID = (int) request.getSession().getAttribute(USER_ID_ATTRIBUTE);
		int orderID = (int) request.getSession().getAttribute(ORDER_ID_ATTRIBUTE);
		
		if(!orderIsPaid(userID,orderID))
		{
			OrderDAO orderDAO = new OrderDAO();
			orderDAO.updatePaymentStatus(orderID, PAID_STATUS);
	    	
	    	LOGGER.info(LOGGER_TEXT);
			
			return PAGE;
		}
		else
		{
			request.getSession().setAttribute(PAID_BEFORE_ATTRIBUTE, true);
			
			return PAGE;
		}
	}
	
	private boolean orderIsPaid(int userID, int orderID)
	{
		OrderDAO orderDAO = new OrderDAO();
		int paidStatusID = orderDAO.selectPaidStatusID(PAID_STATUS);
		int orderPaymnetStatusID = orderDAO.selectPaymentStatus(orderID, userID);
		
		return paidStatusID == orderPaymnetStatusID;
	}

}
