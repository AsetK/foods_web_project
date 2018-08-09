package com.epam.restaurant.commands;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import com.epam.restaurant.dao.LanguageDAO;
import com.epam.restaurant.dao.MenuDAO;
import com.epam.restaurant.entity.Menu;

public class EditMenuCommand implements ActionCommand{
	
	private static final String PAGE = "/EditMenu";
	private static final String LANGUAGE_ATTRIBUTE = "language";
	private static final String FOODS_ATTRIBUTE = "foods";
	private static final String LANGUAGES_ATTRIBUTE = "languages";
	private static final Logger LOGGER = Logger.getLogger(EditMenuCommand.class);
	private static final String LOGGER_TEXT = "Entered the menu editing page.";
	
	@Override
	public String execute(HttpServletRequest request) 
	{
		if(request.getParameter(LANGUAGE_ATTRIBUTE) != null) 
		{
			changeLanguage(request);
		} 
		
		String language = (String) request.getSession().getAttribute(LANGUAGE_ATTRIBUTE);
		
		LanguageDAO languageDAO = new LanguageDAO();
		int languageID = languageDAO.selectLanguageID(language);
		
		MenuDAO menuDAO = new MenuDAO();
    	Menu menu = menuDAO.selectAllFoods(languageID);
		
		languageDAO = new LanguageDAO();
		List<String> languages = languageDAO.selectAll();
		
		request.getSession().setAttribute(FOODS_ATTRIBUTE, menu.getFoods());
		request.getSession().setAttribute(LANGUAGES_ATTRIBUTE, languages);
		
		LOGGER.info(LOGGER_TEXT);
		
		return request.getContextPath() + PAGE;
	}
	
	private void changeLanguage(HttpServletRequest request)
	{
		String language = request.getParameter(LANGUAGE_ATTRIBUTE);
		request.getSession().setAttribute(LANGUAGE_ATTRIBUTE, language);
	}

}
