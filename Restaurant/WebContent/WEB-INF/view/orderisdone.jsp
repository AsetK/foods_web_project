<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="languageParameter" scope="page" value="${param.language}"/>
<c:if test="${not empty languageParameter}">
  <c:set var="language"  value="${param.language}" scope="session"/>
</c:if>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="com.epam.properties.text"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Insert title here</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/orderisdonestyle.css"/>
</head>

<body>

  <form class="selectform">
    <div class="select_form">
	  <select name="language" onchange="this.form.submit()">
	    <option disabled="disabled" selected="selected"><fmt:message key="message.select_language"/></option>
		<option value="ru"><fmt:message key="message.language.ru"/></option>
		<option value="en"><fmt:message key="message.language.en"/></option>
	  </select>
	</div>
  </form>
  
  <c:set var="alreadySubmitted" scope="page" value="${sessionScope.alreadySubmitted}"/>
  <c:if test="${alreadySubmitted eq false}">
    <div class="title"><fmt:message key="message.orderisdone"/></div>
  </c:if>
  
  <c:if test="${alreadySubmitted eq true}">
    <div class="title"><fmt:message key="message.avtivorder"/></div>
  </c:if>
  
  <c:set var="orderID" scope="page" value="${sessionScope.orderID}"/>
  <c:if test="${not empty orderID}">
    <form method="post" action="pages/bill" >
      <div class="button">
	    <button type="submit"><fmt:message key="button.receiveabill"/></button>
	  </div>
    </form>
  </c:if>
  
   <form method="post" action="pages/invalidatesession"> 
    <div class="button">
	  <button type="submit"><fmt:message key="button.logout"/></button>
	</div>
  </form>
	
</body>
</html>