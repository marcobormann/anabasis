<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Anabasis game</title>
<%@ include file="/styles.jsp"%>
<script type="text/javascript">
	function update() {
		var xmlhttp = new XMLHttpRequest();
		var height = (window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight);
		height = height-40;
		var params = "action=update&height=" + height;
		xmlhttp.open("POST", "/Anabasis/GameControl", true);
		xmlhttp.setRequestHeader("Content-type",
				"application/x-www-form-urlencoded");
		xmlhttp.setRequestHeader("Content-length", params.length);
 		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
				window.location.href = "/Anabasis/game.jsp";
			}
		};
		xmlhttp.send(params);
	}

	function moveTo(move) {
		var xmlhttp = new XMLHttpRequest();
		var height = (window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight);
		height = height-40;
		var params = "action=move&height="+height+"&move=" + move;
		xmlhttp.open("POST", "/Anabasis/GameControl", true);
		xmlhttp.setRequestHeader("Content-type",
				"application/x-www-form-urlencoded");
		xmlhttp.setRequestHeader("Content-length", params.length);
		xmlhttp.onreadystatechange = function() {
			if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
				window.location.href = "/Anabasis/game.jsp";
			}
		};
		xmlhttp.send(params);
	}

	function setTarget(move, cx, cy) {
		var h = ${height};
		cx = Math.floor(cx / (h / 8));
		cy = Math.floor(cy / (h / 8));
		move = move + "c" + cx + "c" + cy;
		moveTo(move);
	}

	//IE9 workaround
	function fixPageXY(e) {
		if (e.pageX == null && e.clientX != null) {
			var html = document.documentElement
			var body = document.body
			e.pageX = e.clientX + (html.scrollLeft || body && body.scrollLeft || 0)
			e.pageX -= html.clientLeft || 0
			e.pageY = e.clientY + (html.scrollTop || body && body.scrollTop || 0)
			e.pageY -= html.clientTop || 0
		}
	}
	
	var count=${game.time};
	var counter=setInterval(timer, 1000); 
	function timer(){
		count=count-1;
	  	if (count <= 0) {
	     	clearInterval(counter);
	     	return;
	  	}
	 	document.getElementById("timer").innerHTML=count;
	}
</script>
<c:if test="${game.getPlayer(0).user.id!=user.id&&game.match.isRunning()}">
	<script>
		setTimeout(update, 1000);
	</script>
</c:if>
</head>
<body class="lock-screen" bgcolor="#d3d3d3">
	<c:set var="screen" value="${height}" />
	<div align="center">
		<table>
			<tr>
				<td>
					<table class="map">
						<c:forEach var="y" begin="0" end="7">
							<tr class="map">
								<c:forEach var="x" begin="0" end="7">
									<td class="map">
										<div class=img-container>
											<c:set var="card" value="${game.getCard(x,y)}" />
											<c:set var="file" value="empty" />
											<c:if test="${card.isTurned()}">
												<c:set var="file" value="${card.name}" />
												<c:if test="${card.getOwner()!=null}">
													<c:set var="file" value="${card.name}${card.getOwner().type}" />
												</c:if>
											</c:if>
											<img id="${x}${y}" class="back"
												src="<%=request.getContextPath()%>/images/<c:out value='${file}'/>.jpg"
												height="<c:out value='${screen/8}'/>"
												width="<c:out value='${screen/8}'/>"
												onmousedown="return false;" />
											<c:if test="${card.piece!=null}">
												<c:set var="left" value="0"/>
												<c:if test="${card.getPieceTwo()!=null}">
													<c:set var="piece" value="${card.getPieceTwo()}" />
													<c:set var="fraction" value="${piece.owner.type}" />
													<c:set var="rank" value="hoplite" />
													<c:set var="number" value="2"/>
													<c:if test="${piece.isArchon()}">
														<c:set var="rank" value="archon" />
													</c:if>
													<c:set var="left" value="${screen/32}px"/>
													<img id="${x}c${y}c${number}" class="top"
														src="<%=request.getContextPath()%>/images/<c:out value='${rank}'/>_<c:out value='${fraction}'/>.gif"
														height="<c:out value='${screen/8}'/>"
														width="<c:out value='${screen/8}'/>" 
														style="left:${left}"/>
													<c:if test="${game.getPlayer(0).user.id==user.id&&game.getPlayer(0)==piece.owner}">
														<c:import url="/move.jsp">
															<c:param name="piece_id" value="${x}c${y}c${number}"/>
															<c:param name="height" value="${screen/16}"/>
														</c:import>
													</c:if>
													<c:set var="left" value="-${screen/32}px"/>
												</c:if>
												<c:set var="piece" value="${card.piece}" />
												<c:set var="fraction" value="${piece.owner.type}" />
												<c:set var="rank" value="hoplite" />
												<c:set var="number" value="1"/>
												<c:if test="${piece.isArchon()}">
													<c:set var="rank" value="archon" />
												</c:if>
												<img id="${x}c${y}c${number}" class="top"
													src="<%=request.getContextPath()%>/images/<c:out value='${rank}'/>_<c:out value='${fraction}'/>.gif"
													height="<c:out value='${screen/8}'/>"
													width="<c:out value='${screen/8}'/>" 
													style="left:${left}"/>
												<c:if test="${game.getPlayer(0).user.id==user.id&&game.getPlayer(0)==piece.owner}">
													<c:import url="/move.jsp">
														<c:param name="piece_id" value="${x}c${y}c${number}"/>
														<c:param name="height" value="${screen/16}"/>
													</c:import>
												</c:if>
											</c:if>
										</div>
									</td>
								</c:forEach>
							</tr>
						</c:forEach>
					</table>
				</td>
				<td>
					<table>
						<c:forEach items="${game.players}" var="player" varStatus="status">
							<c:set var="color" value="#800080" />
							<c:if test="${player.type=='Makedonian'}">
								<c:set var="color" value="#00000FF" />
							</c:if>
							<c:if test="${player.type=='Theban'}">
								<c:set var="color" value="#0008000" />
							</c:if>
							<c:if test="${player.type=='Spartan'}">
								<c:set var="color" value="#FF00000" />
							</c:if>
							<tr>
								<td bgcolor="${color}">
									<p class="player">${player.type}<br /> Name:
										${player.user.name}<br /> Rating: ${player.user.rating}
									</p> <c:if test="${status.first&&game.numberOfPlayers()>1}">
										<p class="player">Time: <span id="timer"></span></p>
										<c:if test="${player.getCanPass()&&player.user.id==user.id}">
											<p>
												<button onclick="moveTo('pass')">pass your turn</button>
											</p>
										</c:if>
									</c:if>
								</td>
							</tr>
						</c:forEach>
						<c:if test="${game.match.hasEnded()||mode=='watch'}">
							<tr>
								<td>
									<br />
									<form action="/Anabasis/UserControl" method="post">
										<input class="menue" type="submit" name="action" value="back to menu" />
									</form>
								</td>
							</tr>
						</c:if>
					</table>
				</td>
			</tr>
		</table>
		<c:if test="${game.match.hasEnded()}">
			<p class="message">${game.match.toString()}</p>
		</c:if>
		<c:if test="${message!=''&&game.numberOfPlayers()>1}">
			<p class="message">${message}</p>
		</c:if>
	</div>
</body>
</html>