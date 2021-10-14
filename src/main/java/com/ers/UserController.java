package com.ers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ers.dao.DBConnection;
import com.ers.dao.UserDaoImpl;
import com.ers.model.User;
import com.ers.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserController {
	
	public String login(HttpServletRequest req) {
		if(!req.getMethod().equals("POST")) {
			System.out.println("unsucessful login");
			return "html/index.html";
		}
		DBConnection con = new DBConnection();
		UserDaoImpl uDao = new UserDaoImpl(con);
		UserService uServ = new UserService(uDao);
		User user = uServ.getByUsername(req.getParameter("username"));
				
		if(user == null)
			return "wrongcreds.view";
		else if(user.getPassword().equals(req.getParameter("password"))){
			req.getSession().setAttribute("currentUser", user);
			if(user.getRole().getRole().equals("Employee"))
				return "html/employee.html";
			return "html/manager.html";
		}
		return "html/index.html";
	}
	
	public String logout(HttpServletRequest req) {
		if(!req.getMethod().equals("POST")) {
			System.out.println("unsucessful logout");
			return "html/index.html";
		}
		
		HttpSession session = req.getSession(false);
		if (session != null) {
		    session.invalidate();
		}
		return "html/index.html";
	}
	
	public void getSessionUser(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException, IOException {
		User user = (User) req.getSession().getAttribute("currentUser");
		res.getWriter().write(new ObjectMapper().writeValueAsString(user));
	}
}
