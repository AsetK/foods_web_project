package com.epam.restaurant.commands;

import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import com.epam.restaurant.dao.FoodDAO;
import com.epam.restaurant.dao.LanguageDAO;
import com.epam.restaurant.dao.MenuDAO;
import com.epam.restaurant.entity.Menu;

public class MenuCommand implements ActionCommand{
	
	private static final String PAGE = "/Menu";
	private static final String LANGUAGE_ATTRIBUTE = "language";
	private static final String FOODS_ATTRIBUTE = "foods";
	private static final String AVAILABLE_STATUS = "Available";
	private static final Logger LOGGER = Logger.getLogger(MenuCommand.class);
	private static final String LOGGER_TEXT = "Entered the menu.";

	@Override
	public String execute(HttpServletRequest request) 
	{
		if(request.getParameter(LANGUAGE_ATTRIBUTE) != null) 
		{
			changeLanguage(request);
		} 
		
		String language = (String) request.getSession().getAttribute(LANGUAGE_ATTRIBUTE);
		
		FoodDAO foodDAO = new FoodDAO();
		int availableStatusID = foodDAO.selectStatusID(AVAILABLE_STATUS);
		
		LanguageDAO languageDAO = new LanguageDAO();
		int languageID = languageDAO.selectLanguageID(language);
		
		MenuDAO menuDAO = new MenuDAO();
    	Menu menu = menuDAO.select(languageID, availableStatusID);
    	
    	request.getSession().setAttribute(FOODS_ATTRIBUTE, menu.getFoods());
    	
    	LOGGER.info(LOGGER_TEXT);
    	
		return request.getContextPath() + PAGE;
	}
	
	private void changeLanguage(HttpServletRequest request)
	{
		String language = request.getParameter(LANGUAGE_ATTRIBUTE);
		request.getSession().setAttribute(LANGUAGE_ATTRIBUTE, language);
	}
	
}
