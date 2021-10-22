package com.ers.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.ers.ReimbursementController;
import com.ers.UserController;
import com.ers.dao.DBConnection;
import com.ers.dao.ReimbursementDaoImpl;
import com.ers.dao.UserDaoImpl;
import com.ers.model.User;
import com.ers.service.UserService;

public class ViewDispatcher {

	public final Logger log = Logger.getLogger(ViewDispatcher.class);
	
	public String process(HttpServletRequest req) {
		DBConnection con = new DBConnection();
		UserDaoImpl uDao = new UserDaoImpl(con);
		UserController uControl = new UserController(uDao);
		ReimbursementDaoImpl rDao = new ReimbursementDaoImpl(con);
		ReimbursementController rControl = new ReimbursementController(rDao);
		switch(req.getRequestURI()) {
			case "/proj1/login.view":
				HttpSession session = req.getSession(false);
				if(session == null || session.getAttribute("currentUser") == null) {
					System.out.println("logging in user");
					return uControl.login(req);
				}else {
					if((boolean) req.getSession().getAttribute("wrongcreds"))
						return "html/index.html";
					User user = (User) req.getSession().getAttribute("currentUser");
					if(reLogin(req,user)) {
						if(user.getUserRole().getRole().equals("Employee")) {
							System.out.println("employee");
							return "html/employee.html";
						}
						else if(user.getUserRole().getRole().equals("Finance Manager")) {
							System.out.println("manager");
							return "html/manager.html";
						}
					}else {
						System.out.println("different login");
						return uControl.login(req);
					}
				}
			case "/proj1/logout.view":
				System.out.println("logging out user");
				return uControl.logout(req);
			case "/proj1/ticket.view":
				System.out.println("ticket submitted");
				return rControl.submitTicket(req);
			case"/proj1/updateTicket.view":
				System.out.println("ticket edited");
				return rControl.updateTicket(req);
			default:
				System.out.println("in default");
				return "html/index.html";
		}
	}
	
	public boolean reLogin(HttpServletRequest req, User currentUser) {
		DBConnection con = new DBConnection();
		UserDaoImpl uDao = new UserDaoImpl(con);
		UserService uServ = new UserService(uDao);
		User user = null;
		try {
			user = uServ.getByUsername(req.getParameter("username"));
			if(user.getUsername().equals(currentUser.getUsername())&&user.getPassword().equals(currentUser.getPassword())) {
				return true;
			}
		}catch(NullPointerException e) {
			log.error("relogin in unsuccessful");
		}
		return false;
	}
	
}
