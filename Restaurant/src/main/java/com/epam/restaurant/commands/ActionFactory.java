package com.epam.restaurant.commands;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class ActionFactory {
	
	private static final int SUBSTRING = 1;
	private static final Map<String,ActionCommand> commands = new HashMap<String,ActionCommand>()
			{{
				put("login",new LoginCommand());
				put("menu",new MenuCommand());
				put("userorders",new UserOrdersCommand());
				put("order",new OrderCommand());
				put("submitorder",new SubmitOrderCommand());
				put("bill",new BillCommand());
				put("orderpayment",new OrderPaymentCommand());
				put("orderslist",new OrdersListCommand());
				put("executeorder",new ExecuteOrderCommand());
				put("editmenu",new EditMenuCommand());
				put("addfood",new AddFoodCommand());
				put("deletefood",new DeleteFoodCommand());
				put("restorefood",new RestoreFoodCommand());
				put("changefoodprice",new ChangeFoodPriceCommand());
				put("invalidatesession",new InvalidateSessionCommand());
			}};
	
	public ActionCommand defineCommand(HttpServletRequest request)
	{
		String actionParameter = request.getPathInfo().substring(SUBSTRING);
		
		return commands.get(actionParameter);
	}

}
