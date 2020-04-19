package control;

import gametools.Card;
import gametools.Piece;
import gametools.Game;
import gametools.Player;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import datatools.User;

/**
 * Servlet implementation class GameControl
 */
@WebServlet("/GameControl")
public class GameControl extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GameControl() {
		super();
	}

	@Override
	public void init() throws ServletException {
		super.init();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(LoginControl.USER);
		Game game = (Game) session.getAttribute(UserControl.GAME);
		if (game == null) {
			response.sendRedirect(response.encodeURL(request.getContextPath()+ "/logged.jsp"));
			return;
		}
		setHeight(request, session);
		String action = request.getParameter("action");
		if (action != null) {
			if (action.equals("move")) {
				String error = executeMove(request, user, game);
				session.setAttribute(LoginControl.MESSAGE, error);
			}
		}
		new ContextReader(getServletContext()).updateGameEnds();
		response.sendRedirect(response.encodeURL(request.getContextPath()+ "/game.jsp"));
	}

	private void setHeight(HttpServletRequest request, HttpSession session) {
		String height = request.getParameter("height");
		if (height == null) {
			height = (String) session.getAttribute("height");
			if (height == null) {
				session.setAttribute("height", "650");
			}
		} else {
			session.setAttribute("height", height);
		}
	}

	private String executeMove(HttpServletRequest request, User user, Game game) {
		String move = request.getParameter("move");
		if (game == null) {
			return "Game not found!";
		}
		Player player = game.getPlayer(0);
		if (player.getUser().getId() != user.getId()) {
			return "it's not your turn!";
		}
		if (move.equals("pass")) {
			if (player.getCanPass()) {
				player.setMoveAgain(false);
				game.next();
				new ContextReader(getServletContext()).getPlaying(user).move();
			}
		} else {
			return executeUserMove(user, game, move);
		}
		return "";
	}

	private String executeUserMove(User user, Game game, String move) {
		try {
			String[] coordinates = move.split("c");
			String pnb = coordinates[2];
			int tx = Integer.parseInt(coordinates[3]);
			int ty = Integer.parseInt(coordinates[4]);
			int dx = Integer.parseInt(coordinates[0]);
			int dy = Integer.parseInt(coordinates[1]);
			if (Math.abs(tx - dx) > 1 || Math.abs(ty - dy) > 1) {
				return "you can only move to neighbor squares!";
			}
			if(tx==dx&&ty==dy){return "you cannot move to the same square!";}
			Piece piece;
			Card depart = game.getCard(dx, dy);
			if (pnb.equals("1")) {
				piece = depart.getPiece();
			} else {
				piece = depart.getPieceTwo();
			}
			if (piece != null) {
				if (piece.blocked()) {
					return "this piece is blocked!";
				}
				if (piece.anotherMustMove()) {
					return "another piece has to move!";
				}
				boolean possible = piece.move(tx, ty);
				if (possible) {
					game.next();
					new ContextReader(getServletContext()).getPlaying(user).move();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

}
