package com.epam.restaurant.commands;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import com.epam.restaurant.commandsservice.FoodAvailabilityChanger;

public class DeleteFoodCommand implements ActionCommand{
	
	private static final String PAGE = "./editmenu";
	private static final String NOT_AVAILABLE_STATUS = "Not available";
	private static final Logger LOGGER = Logger.getLogger(DeleteFoodCommand.class);
	private static final String LOGGER_TEXT = "Food has been deleted.";

	@Override
	public String execute(HttpServletRequest request) 
	{
		FoodAvailabilityChanger.change(request, NOT_AVAILABLE_STATUS);
		
		LOGGER.info(LOGGER_TEXT);
		
		return PAGE;
	}

}
