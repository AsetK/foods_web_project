package com.epam.restaurant.commands;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import com.epam.restaurant.dao.FoodDAO;

public class ChangeFoodPriceCommand implements ActionCommand{
	
	private static final String PAGE = "./editmenu";
	private static final Logger LOGGER = Logger.getLogger(RestoreFoodCommand.class);
	private static final String LOGGER_TEXT = "Food price has been changed.";

	@Override
	public String execute(HttpServletRequest request) 
	{
		String foodName = request.getParameterNames().nextElement();
		String foodNewPriceString = request.getParameter(foodName);
		
		double foodNewPrice = Double.valueOf(foodNewPriceString);
		
		FoodDAO foodDAO = new FoodDAO();
		foodDAO.updateFoodPrice(foodName, foodNewPrice);
		
		LOGGER.info(LOGGER_TEXT);
		
		return PAGE;
	}

}
