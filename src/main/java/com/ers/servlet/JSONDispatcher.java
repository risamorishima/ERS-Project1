package com.ers.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ers.UserController;
import com.ers.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONDispatcher {
	public void process(HttpServletRequest req, HttpServletResponse res) throws IOException{
		UserController controller = new UserController();
		switch(req.getRequestURI()) {
			case "/proj1/getsessionuser.json":
				HttpSession session = req.getSession(false);
				if(session != null) {
					System.out.println("get user session");
					res.setHeader("Cache-Control","no-cache,no-store,must-revalidate");
					res.setHeader("Pragma","no-cache");
					res.setDateHeader("Expires", 0);
					controller.getSessionUser(req, res);
					break;
				}
				break;
			case "/proj1/logout.json":
				System.out.println("logging out user");
				res.sendRedirect("http://localhost:8080/proj1/"+controller.logout(req));
				break;
			default:
				System.out.println("in JSON default");
				res.getWriter().write(new ObjectMapper().writeValueAsString(new User()));
				break;
		}
}
}
