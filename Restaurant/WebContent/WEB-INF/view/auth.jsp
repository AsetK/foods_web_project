<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix ="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="language"  value="${param.language}" scope="session"/>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="com.epam.properties.text"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Insert title here</title>
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/authstyle.css"/>
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
	
  <div class="title"><fmt:message key="message.welcome"/></div>
	
  <div class="form">
    <form method="post" action="pages/login"> 
      <label><input type="text" name="login" pattern=<fmt:message key="pattern.login"/> maxlength="8" required placeholder=<fmt:message key="message.login"/>></label><br>
      <label><input type="password" name="password" pattern=<fmt:message key="pattern.login"/> maxlength="8" required placeholder=<fmt:message key="message.password"/>></label><br>
      <button type="submit"><fmt:message key="button.enter"/></button>
	</form>
  </div>
	
</body>
</html>