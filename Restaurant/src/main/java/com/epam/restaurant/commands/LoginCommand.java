package com.epam.restaurant.commands;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import com.epam.restaurant.dao.UserDAO;
import com.epam.restaurant.entity.User;

public class LoginCommand implements ActionCommand{
	
	private static final String LANGUAGE_ATTRIBUTE = "language";
	private static final String LOGIN_PARAMETER = "login";
	private static final String PASSWORD_PARAMETER = "password";
	private static final String STATUS_ATTRIBUTE = "status"; 
	private static final String AUTH_PAGE = "";
	private static final String USER_PAGE = "./menu";
	private static final String ADMIN_PAGE = "./orderslist";
	private static final String USER_STATUS = "user";
	private static final String ADMIN_STATUS = "admin";
	private static final String USER_ID_ATTRIBUTE = "userID";
	private static final String HASH_ALGORITHM = "MD5";
	private static final Logger LOGGER = Logger.getLogger(LoginCommand.class);
	private static final String LOGGER_TEXT = "Logged in as ";

	@Override
	public String execute(HttpServletRequest request) 
	{	
		String page = null;
		
		String language = (String) request.getSession().getAttribute(LANGUAGE_ATTRIBUTE);
		
		if(language == null)
		{
			language = Locale.getDefault().getLanguage();
			request.getSession().setAttribute(LANGUAGE_ATTRIBUTE, language);
		}
			
		String login = (String) request.getParameter(LOGIN_PARAMETER);
		String password = (String) request.getParameter(PASSWORD_PARAMETER);
		
		String md5Password = this.hashPassword(password);
		
		User user = new User();
		user.setLogin(login);
		user.setPassword(md5Password);
		
		UserDAO dao = new UserDAO();
		boolean isExist = dao.select(user);
		
		if(isExist)
		{
			String status = dao.selectStatus(user);
			request.getSession().setAttribute(STATUS_ATTRIBUTE, status);
			
			if(status.equals(USER_STATUS))
			{
				int userID = dao.selectID(user);					 
				request.getSession().setAttribute(USER_ID_ATTRIBUTE, userID); 
				page = USER_PAGE;
				
			}
			else if(status.equals(ADMIN_STATUS))
			{
				page = ADMIN_PAGE;
			}
			
			LOGGER.info(LOGGER_TEXT + status);
		}
		else
		{
			page = request.getContextPath() + AUTH_PAGE;
		}
		
		return page;
	}
	
	private String hashPassword(String password)
	{
		MessageDigest md5 = null;
		
		try 
		{
			md5 = MessageDigest.getInstance(HASH_ALGORITHM);
		} 
		catch (NoSuchAlgorithmException exc) 
		{
			LOGGER.error(exc);
		}
		
		byte[] bytes = md5.digest(password.getBytes());
		StringBuilder sb = new StringBuilder(bytes.length);
		
		for(byte b: bytes)
		{
			sb.append(String.format("%02x", b));
		}
		
		return sb.toString();
	}

}
