package com.ers.servlet;

import com.ers.UserController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ViewDispatcher {
	
	public String process(HttpServletRequest req) {
		UserController controller = new UserController();
		switch(req.getRequestURI()) {
			case "/proj1/login.view":
				HttpSession session = req.getSession(false);
				if(session == null) {
					System.out.println("logging in user");
					return controller.login(req);
				}
				return "html/index.html";
			default:
				System.out.println("in default");
				return "html/index.html";
		}
	}
	
}
