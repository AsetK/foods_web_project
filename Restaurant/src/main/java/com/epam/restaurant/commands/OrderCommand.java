package com.epam.restaurant.commands;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

import com.epam.restaurant.dao.FoodDAO;
import com.epam.restaurant.dao.LanguageDAO;
import com.epam.restaurant.entity.Food;
import com.epam.restaurant.entity.Order;
import com.epam.restaurant.entityservice.OrderItemAdder;
import com.epam.restaurant.entityservice.OrderTotalPriceCounter;

public class OrderCommand implements ActionCommand{
	
	private static final String PAGE_ORDER = "/Order";
	private static final String PAGE_MENU = "/Menu";
	private static final String LANGUAGE_ATTRIBUTE = "language";
	private static final String FOODS_ATTRIBUTE = "foods";
	private static final String DEMO_ORDER_ATTRIBUTE = "demoOrder";
	private static final Logger LOGGER = Logger.getLogger(OrderCommand.class);
	private static final String LOGGER_TEXT = "Demo order created.";

	@Override
	public String execute(HttpServletRequest request) 
	{
		if(request.getParameter(LANGUAGE_ATTRIBUTE) != null) 
		{
			return changeLanguage(request);
		} 
		
		List<Food> foods = (LinkedList<Food>) request.getSession().getAttribute(FOODS_ATTRIBUTE);
		
		Order demoOrder = new Order();
		
		for(Food food : foods)
		{
		   	String parameter = request.getParameter(food.getName());
			
			int amount = Integer.valueOf(parameter);
			
			if(amount > 0)
			{
				demoOrder.setItems(new OrderItemAdder().addItem(demoOrder, food, amount));
			}	
		}
		int itemsAmount = demoOrder.getItems().size();
		
		if(itemsAmount > 0)
		{
			demoOrder.setTotalPrice(new OrderTotalPriceCounter().countTotalPrice(demoOrder));
			
			HttpSession session = request.getSession();
			
			session.setAttribute(DEMO_ORDER_ATTRIBUTE, demoOrder);
			
			LOGGER.info(LOGGER_TEXT);
			
			return request.getContextPath() + PAGE_ORDER;
		}
		else
		{
			return request.getContextPath() + PAGE_MENU;
		}
	}
	
	private String changeLanguage(HttpServletRequest request)
	{
		
		String language = request.getParameter(LANGUAGE_ATTRIBUTE);
		request.getSession().setAttribute(LANGUAGE_ATTRIBUTE, language);
		
		Order demoOrder = (Order) request.getSession().getAttribute(DEMO_ORDER_ATTRIBUTE);
		
		if(demoOrder == null) // no data to translate
		{
			return request.getContextPath() + PAGE_ORDER;
		}
		
		Set<Food> foods = demoOrder.getItems().keySet();
		
		LanguageDAO languageDAO = new LanguageDAO();
		int languageID = languageDAO.selectLanguageID(language);
		
		for(Food food : foods)
		{
			FoodDAO dao = new FoodDAO();
			dao.selectFoodTranslation(food, languageID);
		}
		
		return request.getContextPath() + PAGE_ORDER;
	}

}
