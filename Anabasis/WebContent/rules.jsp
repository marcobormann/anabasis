<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<%@ include file="/styles.jsp" %>
</head>
<body bgcolor="#d3d3d3">
<div align="center">
<img src="<%=request.getContextPath()%>/images/anabasis.jpg" alt="Anabasis">
<h1>The goal of the game</h1>
<p>Anabasis is a game played by four players on a board of 8x8 squares. Each player has two different kinds of pieces, two hoplites and one archon. A player who loses his archon is eliminated from the game. The last player standing is the winner.</p>
<h1>How to get started</h1>
<p>In order to play, you have to log in first or register to log in. Once are logged in you can create a match yourself or join a match created by someone else. You may quit this match at any time unless it has started. If you quit a match you have created, this match will be destroyed. A match starts automatically once it has four players. If there are not enough players available to complete a match, you may chose computer engines and add them to the match. But this can only be done by the player who created the match.</p>
<h1>How to play</h1>
<p>Once the match has started you find your pieces in one corner of the board. You can identify the side you are playing by looking at the playerlist on the right of the screen. The player on top is up to play, with his time running down. A timeout causes a loss. If it’s your turn, you can move by first selecting a piece with a mouseclick and then moving it to any neighbouring square. The landscape of this square will be uncovered and this immediately has an effect on your piece. The effects of the landscapes are as follows:</p>
<h2>Barbarians</h2>
<p>Bad luck, the Barbarians will kill your piece. So don’t move your archon around to uncover squares or you might lose right away. It’s helpful to know that there are only three of those unhospitable squares.</p> 
<h2>Plain</h2>
<p>You only have access to a plain, if there is not already one of your pieces on it. Should there be an opponent piece on this square, your piece will kill it.</p>
<h2>Water</h2>
<p>A piece you move to a water square has to move again right away. Since there are a lot of those squares in the game, you can always attempt a surprise attack on an opponent archon who is close to one of your pieces.</p>
<h2>Grain</h2>
<p>By moving a piece to a grain square, you get the right to move another one of your pieces. But you may also pass the turn by clicking on your colored box in the playerlist on the right. As a piece on a grain square is itself stopped by arriving there, it may get reactivated right away by another piece moving to another grain square.</p>
<h2>Mountain</h2>
<p>A piece moving to a mountain square will get stuck for one turn.</p> 
<h2>Forrest</h2>
<p>A piece can move to a forrest square, even if there is already a piece. And even if it’s one of the opponent’s pieces, your piece will not kill it, but share the spot with it. A third piece arriving would then kill one of the pieces, which is not his own. But this holds only true for hoplites. An archon may share the spot in the forrest with one of his own hoplites, but an arriving opponent piece will kill him. An archon will also kill an opponent hoplite in the forrest.</p>
<h2>Town</h2>
<p>Towns allow you to get supplies. When you move into a town with your archon, you get an extra hoplite, unless you already have the maximum number of seven pieces. But in the next turn one of the two pieces in the town has to move out. A town where you had supplies will also get marked with the color of your side and you cannot get new supplies while it has this color. It will not lose the color, until another archon has conquered the town.</p>
<h1>Gamescore</h1>
<p>When you are eliminated from a match you have to wait for it to end, before getting involved in new match. The games are scored using an elo formula. The winner of a game wins over each of the losers. The maximum number of points he can win or lose in one game are 32. The higher your opponents elo is, the more points you will get in the case of a win and the less points you will risk to lose.</p>
<h1>Historical background</h1>
<p>Ever since the greeks succeeded in defending themselves against the persian invasion in the late sixth century BC, the idea of attacking the vast persian empire grew stronger and stronger in their mind. The only thing that held them back was their unability to unify their forces. The whole fifth century BC saw Sparta and Athens fighting for the military supremacy in greece. The persian kings aware of their own weakness tried to keep those wars going on as good as they could by financing the weaker side. When finally at the end of the fifth century a greek army was lead against the persian empire, it was the persian prince Kyros who initiated the campaign trying to grap the throne from his brother. The historian Xenophon followed this campaign as a mere tourist and wrote a book called “anabasis“ on it. “anabasis“ merely means marching uphill. The greek army had to do this because getting to persia meant crossing the hights of Anatolia first. But for the greeks this term soon became a synonym for the attack against the persian emire. The first anabasis failed when Kyros died on the battlefield of Kunaxa near Babylon. After this it took the greek another eighty years with various wars between Thebes, Sparta and Athens until finally the makedonian king Philippos could unite the greeks. But getting murdered in the wake of his persian campaign it was up to his son Alexandros to reunite the greeks once again and lead them – this time successfully – against the persians. Alexandros’ campaign is described in detail by Arrianos in a book once again called the “anabasis“</p>
<br/>
<a href="/Anabasis/login.jsp">back to login</a>
</div>
</body>
</html>