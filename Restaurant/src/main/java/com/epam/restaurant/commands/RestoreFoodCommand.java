package com.epam.restaurant.commands;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import com.epam.restaurant.commandsservice.FoodAvailabilityChanger;

public class RestoreFoodCommand implements ActionCommand{
	
	private static final String PAGE = "./editmenu";
	private static final String AVAILABLE_STATUS = "Available";
	private static final Logger LOGGER = Logger.getLogger(RestoreFoodCommand.class);
	private static final String LOGGER_TEXT = "Food has been restored.";

	@Override
	public String execute(HttpServletRequest request) 
	{
		FoodAvailabilityChanger.change(request, AVAILABLE_STATUS);
		
		LOGGER.info(LOGGER_TEXT);
		
		return PAGE;
	}

}
