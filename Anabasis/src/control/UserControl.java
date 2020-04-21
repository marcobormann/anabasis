package control;

import gametools.Game;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import datatools.Match;
import datatools.User;

/**
 * Servlet implementation class UserControl
 */
@WebServlet("/UserControl")
public class UserControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String GAME = "game";
	public static final String DATA = "data";
	public static final String USERS = "users";
	public static final String RUNNING = "running";
	private static final String MODE = "mode";
	private Map<Integer,HttpSession> sessions;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UserControl() {
		super();
	}

	@Override
	public void init() throws ServletException {
		super.init();
		sessions = new HashMap<Integer,HttpSession>();
		UserControlData data = new UserControlData();
		List<User> users = new Vector<User>();
		List<Playing> runningGames = new Vector<Playing>();
		ServletContext context = getServletContext();
		context.setAttribute(DATA, data);
		context.setAttribute(USERS, users);
		context.setAttribute(RUNNING, runningGames);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		updateUserList(session);
		session.setAttribute(LoginControl.MESSAGE, null);
		User user = (User) session.getAttribute(LoginControl.USER);
		if (user == null) {
			getServletContext().getRequestDispatcher("/login.jsp").forward(request,response);
			return;
		}
		UserControlData data = (UserControlData) getServletContext().getAttribute(DATA);
		if(data!=null){
			data.updateMatchEnds();
			data.updateTopList();
			data.updateResultList();
			session.setAttribute(DATA, data);
		}
		removeEndedGames(session);
		new ContextReader(getServletContext()).updateGameEnds();
		saveSelected(request, data);
		String action = request.getParameter("action");
		if(action==null){action="";}
		String error = handleUserActions(action, request);
		if (error == null) {error="";}
		session.setAttribute(LoginControl.MESSAGE, error);
		handleMatchStarts();
		boolean started = addStartedGameAttribute(request);
		if(action.equals("update")&&started){
			startBrowser(response);
			return;
		}
		if((started||(action.equals("watch game")&&error.equals("")))){
			response.sendRedirect(response.encodeURL(request.getContextPath()+ "/game.jsp"));
			return;
		}
		response.sendRedirect(response.encodeURL(request.getContextPath()+ "/logged.jsp"));
	}
	
	private void saveSelected(HttpServletRequest request, UserControlData data) {
		data.setSelectedEngine(request.getParameter("selectedEngine")); 
		data.setSelectedMatch(request.getParameter("selectedMatch")); 
	}

	private void removeEndedGames(HttpSession session) {
		Game game = (Game) session.getAttribute(GAME);
		if(game!=null){
			if(game.getMatch().hasEnded()){session.setAttribute(GAME, null);}
		}
	}

	private void startBrowser(HttpServletResponse response) throws IOException{
		response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.print("start");
        out.close();
	}
	
	private boolean addStartedGameAttribute(HttpServletRequest request){
		@SuppressWarnings("unchecked")
		List<Playing> runningGames = (List<Playing>) getServletContext().getAttribute(RUNNING);
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(LoginControl.USER);
		if(runningGames==null||user==null||session==null){return false;}
		for(Playing playing : runningGames){
			Game game = playing.getGame();
			if(game.getMatch().has(user.getId())){
				session.setAttribute(GAME, game);
				session.setAttribute(MODE, "play");
		        return true;
			}
		}
		return false;
	}

	private void handleMatchStarts() {
		UserControlData data = (UserControlData) getServletContext().getAttribute(DATA);
		@SuppressWarnings("unchecked")
		List<Playing> runningGames = (List<Playing>) getServletContext().getAttribute(RUNNING);
		if(data==null||runningGames==null){return;}
		for(Match match : data.getMatchList()){
			if(match.isComplete()){
				Game game = new Game(match);
				match.start();
				Playing playing = new Playing(game);
				runningGames.add(playing);
				playing.start();
			}
		}
	}

	private String handleUserActions(String action, HttpServletRequest request) {
		UserControlData data = (UserControlData) getServletContext().getAttribute(DATA);
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(LoginControl.USER);
		String gameId = request.getParameter("selected_game");
		int matchId = getInt(gameId);
		String engineIdpara = request.getParameter("selected_engine");
		int engineId = getInt(engineIdpara);
		if(data==null){return "Server problem: no data found!";}
		String error = null;
		if (action.equals("create game")) {
			error = data.createGame(user);
		}
		if (action.equals("join game")) {
			error = data.joinGame(user, matchId);
		}
		if (action.equals("leave game")) {
			error = data.leaveGame(user, matchId);
		}
		if (action.equals("add engine")) {
			error = data.addEngine(user, matchId, engineId);
		}
		if (action.equals("remove engine")) {
			error = data.removeEngine(user, matchId, engineId);
		}
		if(action.equals("update")) {
			String height = request.getParameter("height");
			if(height!=null){session.setAttribute("height",height);}
		}
		if(action.equals("back to menu")){
			session.removeAttribute(GAME);
			session.removeAttribute(MODE);
	    }
		if (action.equals("watch game")) {
			error = executeWatchMatch(session, matchId);
		}
		return error;
	}

	private String executeWatchMatch(HttpSession session, int matchId) {
		if (matchId == 0) {
			return "please select a  match to watch!";
		} else {
			Game game = new ContextReader(getServletContext()).getGame(matchId);
			if(game==null){
				return "the selected match is not running!";
			} else {
				session.setAttribute(GAME, game);
				session.setAttribute(MODE,"watch");
			}
		}
		return null;
	}

	private int getInt(String gameId) {
		int matchId = 0;
		try {
			matchId = Integer.parseInt(gameId);
		} catch (Exception e) {}
		return matchId;
	}

	private void updateUserList(HttpSession session) {
		User currentUser = (User) session.getAttribute(LoginControl.USER);
		sessions.remove(currentUser.getId());
		sessions.put(currentUser.getId(), session);
		List<User> users = new Vector<User>();
		for (Iterator<Integer> iterator = sessions.keySet().iterator(); iterator
				.hasNext();) {
			int index = iterator.next();
			HttpSession s = sessions.get(index);
			if (s == null) {
				iterator.remove();
			} else {
				try {
					User user = (User) s.getAttribute(LoginControl.USER);
					if(!users.contains(user)){
						users.add(user);
					}
				} catch(Exception e){
					iterator.remove();
				}
			}
		}
		getServletContext().setAttribute(USERS, users);
	}

}
