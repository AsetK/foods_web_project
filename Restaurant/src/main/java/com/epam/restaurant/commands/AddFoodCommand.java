package com.epam.restaurant.commands;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import com.epam.restaurant.dao.FoodDAO;
import com.epam.restaurant.dao.LanguageDAO;
import com.epam.restaurant.entity.Food;

public class AddFoodCommand implements ActionCommand{
	
	private static final String PAGE = "./editmenu";
	private static final String CONNECTION_PROPERTY = "com.epam.properties.text";
	private static final String FOOD_PRICE_PROPERTY_KEY = "message.food.price";
	private static final String FOOD_NAME_PROPERTY_KEY = "message.food.name";
	private static final String FOOD_DESCRIPTION_PROPERTY_KEY = "message.food.description";
	private static final String LANGUAGE_ATTRIBUTE = "language";
	private static final Logger LOGGER = Logger.getLogger(AddFoodCommand.class);
	private static final String LOGGER_TEXT = "New food has been added.";
	
	@Override
	public String execute(HttpServletRequest request) 
	{
		String sessionLanguage = (String) request.getSession().getAttribute(LANGUAGE_ATTRIBUTE);
		
		ResourceBundle rbLanguage = ResourceBundle.getBundle(CONNECTION_PROPERTY,new Locale(sessionLanguage));
		String foodPriceParameter = rbLanguage.getString(FOOD_PRICE_PROPERTY_KEY);
		
		String foodPriceString = request.getParameter(foodPriceParameter);
		double foodPrice = Double.valueOf(foodPriceString);
		
		LanguageDAO languageDAO = new LanguageDAO();
		List<String> languages = languageDAO.selectAll();
		
		Map<Integer,Food> multilingualFood = new LinkedHashMap<>();
    	
		for(String language:languages)
		{
			ResourceBundle rbFood = ResourceBundle.getBundle(CONNECTION_PROPERTY,new Locale(language));
			String foodNameParameter = rbFood.getString(FOOD_NAME_PROPERTY_KEY);
			String foodDescriptionParameter = rbFood.getString(FOOD_DESCRIPTION_PROPERTY_KEY);
			
			String foodName = request.getParameter(foodNameParameter);
			String foodDescription = request.getParameter(foodDescriptionParameter);
			
			Food food = new Food();
			food.setName(foodName).setDescription(foodDescription);
			
			int languageID = languageDAO.selectLanguageID(language);
			
			multilingualFood.put(languageID, food);
		}
		
		FoodDAO foodDAO = new FoodDAO();
		foodDAO.createFood(foodPrice, multilingualFood);
		
		LOGGER.info(LOGGER_TEXT);
		
		return PAGE;
	}

}
