package com.epam.restaurant.commands;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import com.epam.restaurant.dao.OrderDAO;

public class ExecuteOrderCommand implements ActionCommand{
	
	private static final String PAGE = "./orderslist";
	private static final String READY_ORDER_ID_PARAMETER = "readyOrderID";
	private static final String EXECUTED_STATUS = "Ready";
	private static final Logger LOGGER = Logger.getLogger(ExecuteOrderCommand.class);
	private static final String LOGGER_TEXT = "Order has been executed.";

	@Override
	public String execute(HttpServletRequest request) 
	{
		String parameter = request.getParameter(READY_ORDER_ID_PARAMETER);
		int orderID = Integer.valueOf(parameter);
		
		if(!orderIsExecuted(orderID))
		{
			OrderDAO orderDAO = new OrderDAO();
			orderDAO.updateOrderStatus(orderID, EXECUTED_STATUS);
			
			LOGGER.info(LOGGER_TEXT);
			
			return PAGE;
		}
		else 
		{
			return PAGE;
		}
	}
	
	private boolean orderIsExecuted(int orderID)
	{
		OrderDAO orderDAO = new OrderDAO();
		int executedStatusID = orderDAO.selectExecutedStatusID(EXECUTED_STATUS);
		int orderExecutionStatusID = orderDAO.selectExecutionStatus(orderID);
		
		return executedStatusID == orderExecutionStatusID;
	}

}
