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
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/editmenustyle.css"/>
</head>

<body>

  <form class="selectform" action="pages/editmenu">
    <div class="select_form">
	  <select name="language" onchange="this.form.submit()">
	    <option disabled="disabled" selected="selected"><fmt:message key="message.select_language"/></option>
		<option value="ru"><fmt:message key="message.language.ru"/></option>
		<option value="en"><fmt:message key="message.language.en"/></option>
	  </select>
	</div>
  </form>

  <div class="title"><fmt:message key="message.menuedition"/></div>
  
  <form method="post" action="pages/invalidatesession"> 
    <div class="button2">
	  <button type="submit"><fmt:message key="button.logout"/></button>
	</div>
  </form>
	
  <c:forEach var="food" items="${sessionScope.foods}">
    <div class="form">
	  <ul>
		<li> <fmt:message key="message.food.name"/> <c:out value="${food.name}"/> </li>
		<li> <fmt:message key="message.food.description"/> <c:out value="${food.description}"/> </li>
		<li> <fmt:message key="message.food.price"/> <c:out value="${food.price}"/> </li> 
		<li> <fmt:message key="message.food.availableStatus"/> <c:out value="${food.availableStatus}"/> </li> 
	  </ul>
	  
	  <c:set var="foodStatus" scope="page" value="${food.availableStatus}" />
	  <c:set var="availableStatus" scope="page"> <fmt:message key="status.food.available"/> </c:set>          
	  <c:set var="notAvailableStatus" scope="page"> <fmt:message key="status.food.notavailable"/> </c:set> 
		
	  <form method="post" action="pages/changefoodprice">
	    <div class= "message">
		  <label><fmt:message key="message.food.newprice"/> <input type="number" name="${food.name}" step="0.1"  min="1" max="100000" required></label>
		</div>
		<div class="button"> 
		  <button type="submit" name="${food.name}"><fmt:message key="button.changeprice"/></button>
		</div>
	  </form>
		
	  <c:if test="${foodStatus == availableStatus}">
		<form method="post" action="pages/deletefood">
	      <div class="button">
		    <button type="submit" name="${food.name}"><fmt:message key="button.delete"/></button>
		  </div>
		</form>
      </c:if>
		
	  <c:if test="${foodStatus == notAvailableStatus}">
		<form method="post" action="pages/restorefood">
		  <div class="button">
			<button type="submit" name="${food.name}"><fmt:message key="button.restore"/></button>
		  </div>
		</form>
	  </c:if>
    </div>
  </c:forEach>

  <div class="form">
    <form method="post" action="pages/addfood"> 
      <c:forEach var="language" items="${sessionScope.languages}">
	    <fmt:setLocale value="${language}"/>
	    <fmt:setBundle basename="com.epam.properties.text"/>
	  
	    <ul>
		  <li> <label> <fmt:message key="message.food.name"/> <input type="text" name=<fmt:message key="message.food.name"/> pattern=<fmt:message key="pattern.newfood.name"/> maxlength="50" required></label></li>
		  <li> <label> <fmt:message key="message.food.description"/> <input type ="text" name=<fmt:message key="message.food.description"/> pattern=<fmt:message key="pattern.newfood.name"/> maxlength="100" required></label></li>
		</ul>
	  </c:forEach>
	
	  <fmt:setLocale value="${sessionScope.language}"/>
	  <fmt:setBundle basename="com.epam.properties.text"/>
	
	  <label> <fmt:message key="message.food.price"/> <input type="number" name=<fmt:message key="message.food.price"/> step="0.1" min="1" max="100000" required></label>
	  <div class="button">
	    <button type="submit"><fmt:message key="button.addfood"/></button>
	  </div>
    </form>
  </div>
  
</body>
</html>