<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Anabasis platform</title>
<%@ include file="/styles.jsp"%>
<script type="text/javascript">
	function update() {
		var xmlhttp = new XMLHttpRequest();
		var id = '${user.id}';
		var height = (window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight);
		height = height-40;
		var engines = document.getElementById("engines");
		var selectedEngine = engines.options[engines.selectedIndex].value;
		var matches = document.getElementById("matches");
		var selectedMatch = matches.options[matches.selectedIndex].value;
		var params = "action=update&id=" + id + "&height=" + height + "&selectedEngine=" + selectedEngine + "&selectedMatch=" + selectedMatch;
		xmlhttp.open("POST", "/Anabasis/UserControl", true);
		xmlhttp.setRequestHeader("Content-type",
				"application/x-www-form-urlencoded");
		xmlhttp.setRequestHeader("Content-length", params.length);
 		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
				var answer = xmlhttp.responseText;
				if(answer=="start"){
					window.location.href = "/Anabasis/game.jsp";
				} else {
					window.location.href = "/Anabasis/logged.jsp";
 				}
			}
		};
		xmlhttp.send(params);
	}
	setTimeout(update, 10000);
</script>
</head>
<body bgcolor="#d3d3d3">
	<div align="center">
		<img src="<%=request.getContextPath()%>/images/anabasis.jpg"
			alt="Anabasis">
		<form action="/Anabasis/UserControl" method="post">
			<table cellpadding="15pt">
				<tr>
					<td>
						<p>
							Name:
							<c:out value="${user.name}" />
							<br />Elo:
							<c:out value="${user.rating}" />
						</p> 
						<input class="menue" type="submit" name="action" value="create game" /><br /> 
						<input class="menue" type="submit" name="action" value="join game" /><br /> 
						<input class="menue" type="submit" name="action" value="leave game" /><br /> 
						<input class="menue" type="submit" name="action" value="watch game" /><br />
						<input class="menue" type="submit" name="action" value="add engine" /><br /> 
						<input class="menue" type="submit" name="action" value="remove engine" /><br />
					</td>
					<td><p>
							players<br /> <select name="active users" size="10">
								<c:forEach var="user" items="${users}">
									<option value='<c:out value="${user.id}"/>' disabled="disabled">
										<c:out value="${user.name}" /> (<c:out value="${user.rating}"/>)
								</c:forEach>
							</select>
						</p></td>
					<td><p>
							engines<br /> <select id="engines" name="selected_engine" size="10">
								<c:forEach var="engine" items="${data.engineList}">
									<option value='<c:out value="${engine.id}"/>'
										<c:if test="${engine.id==data.selectedEngine}">
											selected
										</c:if>
									><c:out value="${engine.name}" /> (<c:out value="${engine.rating}" />)
								</c:forEach>
							</select>
						<p></td>
					<td><p>top 10
						<table class="list">
							<c:forEach var="user" items="${data.topList}">
								<tr>
									<td class="list"><c:out value="${user.rating}" /> <c:out
											value="${user.name}" /></td>
								</tr>
							</c:forEach>
						</table>
						<p></td>
				</tr>
			</table>
			<p>
				active games<br /> <select id="matches" class="games" name="selected_game" size="3">
					<c:if test="${data.matchList.size()==0}">
						<option disabled="disabled">please create a game</option>
					</c:if>
					<c:forEach var="match" items="${data.matchList}">
						<option value='<c:out value="${match.id}"/>'
							<c:if test="${match.id==data.selectedMatch}">
								selected
							</c:if>
						><c:out value="${match.toString()}" />
					</c:forEach>
				</select>
			</p>
			<p>
				latest results<br /><select class="games" name="latest_results" size="5">
					<c:forEach var="result" items="${data.resultList}">
						<option disabled="disabled">
							<c:out value="${result}" />
					</c:forEach>
				</select>
			</p>
		</form>
		<h2 class="error">
			<c:out value="${message}" />
		</h2>
	</div>
</body>
</html>