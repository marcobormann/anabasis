<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Anabasis login</title>
<%@ include file="/styles.jsp" %>
</head>
<body bgcolor="#d3d3d3">
	<div align="center">
		<img src="<%=request.getContextPath()%>/images/anabasis.jpg" alt="Anabasis">
		<br/>
		<form action="/Anabasis/LoginControl" method="post">
			<table>
			<tr><td>name</td>
				<td><input type="text" name="name" value='<c:out value="${account.name}"></c:out>'/></td>
			</tr>
			<tr><td>password</td>
			<td><input type="password" name="password" value='<c:out value="${account.password}"></c:out>'/></td>
			</tr>
			<tr><td/><td><input type="submit" value="login"/></td></tr>
			</table>
		</form>
		<br/>
		<a href="/Anabasis/register.jsp">register a new account</a><p/>
		<a href="/Anabasis/rules.jsp">about anabasis</a><p/>
		<h2 class="error"><c:out value="${message}"/></h2>
	</div>
</body>
</html>