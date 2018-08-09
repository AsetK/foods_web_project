package com.epam.restaurant.commands;

import javax.servlet.http.HttpServletRequest;

public interface ActionCommand {
	
	String execute(HttpServletRequest request);

}
