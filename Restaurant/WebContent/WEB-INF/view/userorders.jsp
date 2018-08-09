<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="com.epam.properties.text"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Insert title here</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/ordersliststyle.css"/>
</head>

<body>

  <form class="selectform" action="pages/userorders">
    <div class="select_form">
	  <select name="language" onchange="this.form.submit()">
	    <option disabled="disabled" selected="selected"><fmt:message key="message.select_language"/></option>
		<option value="ru"><fmt:message key="message.language.ru"/></option>
		<option value="en"><fmt:message key="message.language.en"/></option>
	  </select>
	</div>
  </form>

  <form method="post" action="pages/invalidatesession"> 
    <div class="button1">
	  <button type="submit"><fmt:message key="button.logout"/></button>
	</div>
  </form>
	
  <form method="post" action="pages/userorders">
    <div class="button1">
	  <button type="submit" ><fmt:message key="button.refresh"/></button>
	</div>
  </form>
  
  <c:set var="readyStatus" scope="page"> <fmt:message key="status.order.ready"/> </c:set>          
  <c:set var="notReadyStatus" scope="page"> <fmt:message key="status.order.notready"/> </c:set> 
  <c:set var="notPaidStatus" scope="page"> <fmt:message key="status.payment.notpaid"/> </c:set> 
  
  <c:forEach var="order" items="${sessionScope.userOrders}">
	<div class="form">
	  <fmt:message key="message.orderid"/> <c:out value="${order.orderID}"/>
	  <c:set var="items" scope="page" value="${order.items}"/>
	  <c:forEach var="item" items="${items}">
		<ul>
		  <c:set var="food" scope="page" value="${item.key}"/>
		  <li> <fmt:message key="message.food.name"/> <c:out value = "${food.name}"/> </li>
		  <li> <fmt:message key="message.food.description"/> <c:out value = "${food.description}"/> </li>
		  <li> <fmt:message key="message.food.price"/> <c:out value = "${food.price}"/> </li>
		  <li> <fmt:message key="message.food.amount"/> <c:out value = "${item.value}"/> </li>
		</ul>
      </c:forEach>
	  
	  <div class="message">
        <fmt:message key="message.total"/> <c:out value="${order.totalPrice}"/>
      </div>
      <div class="message">
        <fmt:message key="message.orderstatus"/> <c:out value="${order.orderStatus}"/>
      </div>
      <div class="message">
        <fmt:message key="message.paymentstatus"/> <c:out value="${order.paymentStatus}"/>
      </div>
	  
	  <c:set var="orderStatus" scope="page" value="${order.orderStatus}" />
      <c:set var="paymentStatus" scope="page" value="${order.paymentStatus}" />
	  
	  <c:if test="${orderStatus == readyStatus && paymentStatus == notPaidStatus}">
	    <form method="post" action="pages/bill">
	      <div class="button2">
		    <button type="submit" name="orderID" value="${order.orderID}"><fmt:message key="button.pay"/></button>
	      </div>
	    </form>
      </c:if>
      
	</div>
  </c:forEach>

</body>
</html>