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
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/billstyle.css"/>
</head>

<body>

  <form class="selectform" action="pages/bill">
    <div class="select_form">
	  <select name="language" onchange="this.form.submit()">
	    <option disabled="disabled" selected="selected"><fmt:message key="message.select_language"/></option>
		<option value="ru"><fmt:message key="message.language.ru"/></option>
		<option value="en"><fmt:message key="message.language.en"/></option>
	  </select>
	</div>
  </form>

  <div class="title"><fmt:message key="message.order"/></div>
  
  <c:set var="order" scope="page" value="${sessionScope.order}"/>
  <c:set var="items" scope="page" value="${order.items}"/>
  
  <div class="order">
    <div class="message">
      <fmt:message key="message.orderid"/><c:out value="${order.orderID}"/>
	</div>
	
	<c:forEach var="item" items="${items}">
	  <ul>
	    <c:set var="food" scope="page" value="${item.key}"/>
		<li> <fmt:message key="message.food.name"/> <c:out value="${food.name}"/> </li>
		<li> <fmt:message key="message.food.description"/> <c:out value="${food.description}"/> </li>
		<li> <fmt:message key="message.food.price"/> <c:out value="${food.price}"/> </li>
		<li> <fmt:message key="message.food.amount"/> <c:out value="${item.value}"/> </li>
	  </ul>
	</c:forEach>
	
	<div class="message">
	  <fmt:message key="message.total"/> <c:out value="${order.totalPrice}"/><br>
	</div>
	
	<div class="message">
	  <fmt:message key="message.orderstatus"/> <c:out value="${order.orderStatus}"/><br>
	</div>
	
	<div class="message">
	  <fmt:message key="message.paymentstatus"/> <c:out value="${order.paymentStatus}"/><br>
	</div>
	
	<c:set var="paidBefore" scope="page" value="${sessionScope.paidBefore}" />
	
	<c:if test="${paidBefore eq true}">
	  <div class="message">
	    <fmt:message key="message.paidbefore"/><br>
	  </div>
	</c:if>
	
  </div>
  
  <c:set var="orderStatus" scope="page" value="${order.orderStatus}" />
  <c:set var="paymentStatus" scope="page" value="${order.paymentStatus}" />
  <c:set var="readyStatus" scope="page"> <fmt:message key="status.order.ready"/> </c:set>          
  <c:set var="notReadyStatus" scope="page"> <fmt:message key="status.order.notready"/> </c:set> 
  <c:set var="notPaidStatus" scope="page"> <fmt:message key="status.payment.notpaid"/> </c:set> 
	
  <c:if test="${orderStatus == notReadyStatus}">
	<form method="post" action="pages/bill">
	  <div class="button">
	    <button type="submit"><fmt:message key="button.refresh"/></button>
	  </div>
	</form>
  </c:if>
	
  <c:if test="${orderStatus == readyStatus && paymentStatus == notPaidStatus}">
	<form method="post" action="pages/orderpayment">
	  <div class="button">
		<button type="submit"><fmt:message key="button.pay"/></button>
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