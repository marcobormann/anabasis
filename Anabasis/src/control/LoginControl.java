package control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.UserDAO;
import datatools.Account;
import datatools.User;

/**
 * Servlet implementation class SessionControl
 */
@WebServlet("/LoginControl")
public class LoginControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String USER = "user";
	public static final String CONTEXT = "/Anabasis";
	public static final String LOGIN = "/login.jsp";
	public static final String NAME = "name";
	public static final String PASSWORD = "password";
	public static final String MESSAGE = "message";
	public static final String LOGGED = "/UserControl";
	public static final String ERROR =  "/error.jsp";
	public static final String ACCOUNT = "account";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginControl() {
        super();
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendRedirect(CONTEXT+LOGIN);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String name = request.getParameter(NAME).toLowerCase();
		String password = request.getParameter(PASSWORD);
		if(name==null||password==null){
			response.sendRedirect(LOGIN);
			return;
		} 
		Account account = new Account(name,password);
		session.setAttribute(ACCOUNT, account);
		if(name.equals("")){
			request.setAttribute(MESSAGE, "Enter a name please!");
			getServletContext().getRequestDispatcher(LOGIN).forward(request, response);
			return;
		}
		if(password.equals("")){
			request.setAttribute(MESSAGE, "Enter a password please!");
			getServletContext().getRequestDispatcher(LOGIN).forward(request, response);
			return;
		}
		UserDAO dao = new UserDAO();
		try {
			User user = dao.getUser(account);
			if(user==null){
				request.setAttribute(MESSAGE, "Name unknown or password wrong!");
				getServletContext().getRequestDispatcher(LOGIN).forward(request, response);
			} else {
				session.removeAttribute(ACCOUNT);
				session.setAttribute(USER,user);
				session.setAttribute("height","650");
				getServletContext().getRequestDispatcher(LOGGED).forward(request, response);
			}
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
			response.sendRedirect(ERROR);
		}
	}

}
