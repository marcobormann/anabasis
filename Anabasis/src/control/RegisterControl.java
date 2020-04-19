package control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import datatools.Human;
import database.UserDAO;
import datatools.Account;

/**
 * Servlet implementation class RegisterControl
 */
@WebServlet("/RegisterControl")
public class RegisterControl extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String REGISTER = "/register.jsp";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterControl() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String name = request.getParameter(LoginControl.NAME).toLowerCase();
		String password = request.getParameter(LoginControl.PASSWORD);
		if(name==null||password==null){
			response.sendRedirect(REGISTER);
			return;
		} 
		Account account = new Account(name,password);
		session.setAttribute(LoginControl.ACCOUNT, account);
		UserDAO dao = new UserDAO();
		if(name.length()<4||name.length()>12){
			request.setAttribute(LoginControl.MESSAGE, "Your name has to have between 4 and 12 characters!");
			getServletContext().getRequestDispatcher(REGISTER).forward(request, response);
			return;
		}
		try {
			if(dao.getId(name)>0){
				request.setAttribute(LoginControl.MESSAGE, "This name already exists!");
				getServletContext().getRequestDispatcher(REGISTER).forward(request, response);
				return;
			}
		} catch (Exception e) {
			response.sendRedirect(LoginControl.ERROR);
			return;
		}
		if(password.length()<2||password.length()>8){
			request.setAttribute(LoginControl.MESSAGE, "Your password has to have between 2 and 8 characters!");
			request.getServletContext().getRequestDispatcher(REGISTER).forward(request, response);
			return;
		}
		Human human = new Human(0,name,1500);
		int id;
		try {
			id = dao.createUser(human, password);
		} catch (Exception e) {
			response.sendRedirect(LoginControl.ERROR);
			return;
		}
		human.setId(id);
		session.removeAttribute(LoginControl.ACCOUNT);
		session.setAttribute(LoginControl.USER, human);
		getServletContext().getRequestDispatcher(LoginControl.LOGGED).forward(request, response);
	}

}
