package com.epam.restaurant.commandsservice;

import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

import com.epam.restaurant.dao.FoodDAO;

public class FoodAvailabilityChanger {
	
	public static void change(HttpServletRequest request, String availabilityStatus)
	{
		Enumeration<String> paramNames = request.getParameterNames();
		String foodName = paramNames.nextElement();
		
		FoodDAO foodDAO = new FoodDAO();
		foodDAO.updateFoodStatus(foodName,availabilityStatus);
	}

}
