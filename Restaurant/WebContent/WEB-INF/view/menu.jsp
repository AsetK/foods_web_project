<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="com.epam.properties.text"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Insert title here</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/menustyle.css"/>
</head>
	
<body>

  <form class="selectform" action="pages/menu">
    <div class="select_form">
	  <select name="language" onchange="this.form.submit()">
	    <option disabled="disabled" selected="selected"><fmt:message key="message.select_language"/></option>
		<option value="ru"><fmt:message key="message.language.ru"/></option>
		<option value="en"><fmt:message key="message.language.en"/></option>
	  </select>
	</div>
  </form>
  
  <c:set var="alreadyloggedin" scope="page" value="${sessionScope.alreadyLoggedIn}" />
	
  <c:if test="${alreadyloggedin eq true}">
	  <div class="message">
	    <fmt:message key="message.alreadyloggedin"/><br>
	  </div>
  </c:if>
  
  <form method="post" action="pages/userorders"> 
    <div class="button">
	  <button type="submit"><fmt:message key="button.ordershistory"/></button>
	</div>
  </form>
		
  <div class ="title"><fmt:message key="message.menu"/></div>
		 
  <form method="post" action="pages/order"> 
	<c:forEach var="food" items="${sessionScope.foods}">
	  <div class="form">
		<ul>
		  <li> <fmt:message key="message.food.name"/> <c:out value="${food.name}"/> </li>
		  <li> <fmt:message key="message.food.description"/> <c:out value="${food.description}"/> </li>
		  <li> <fmt:message key="message.food.price"/> <c:out value="${food.price}"/> </li>
		  <label><fmt:message key="message.food.amount"/> <input type="number" name="${food.name}" min="0" max="10" value="0"></label> 
		</ul>
	  </div>
    </c:forEach>
	
	<div class="button">
	  <button type="submit"><fmt:message key="button.ordernow"/></button>
	</div>
  </form>
  
  <form method="post" action="pages/invalidatesession"> 
    <div class="button">
	  <button type="submit"><fmt:message key="button.logout"/></button>
	</div>
  </form>
		
</body>
</html>