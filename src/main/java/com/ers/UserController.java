package com.ers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.ers.dao.DBConnection;
import com.ers.dao.UserDaoImpl;
import com.ers.model.User;
import com.ers.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;

public class UserController {
	public final Logger log = Logger.getLogger(UserController.class);
    public static final String SECRETKEY = "somesecretkey";
	public static Map<String, Object> userInfo = new HashMap<>();
	DBConnection con = new DBConnection();
    AES aes = new AES();
	
	public String login(HttpServletRequest req) {
		if(!req.getMethod().equals("POST")) {
			log.info("login submission was not a POST method");
			System.out.println("unsucessful login");
			return "html/index.html";
		}
		UserDaoImpl uDao = new UserDaoImpl(con);
		UserService uServ = new UserService(uDao);
		User user = null;
		try {
			user = uServ.getByUsername(req.getParameter("username"));
		}catch(NullPointerException e) {
			log.error("wrong credentials inputted");
			System.out.println("wrong credentials");
			return "html/index.html";
		}
		if(user == null)
			return "html/index.html";
		else if(user.getPassword().equals(req.getParameter("password"))){
			req.getSession().setAttribute("currentUser", user);
			userInfo.put("user", user);
			req.getSession().setAttribute("wrongcreds", false);
			userInfo.put("wrongcreds", false);
			if(user.getUserRole().getRole().equals("Employee")) {
				log.info("employee "+user.getId()+" logged in");
				return "html/employee.html";
			}
			log.info("manager "+user.getId()+" logged in");
			return "html/manager.html";
		}
		log.error("wrong credentials inputted");
		System.out.println("wrong credentials");
		req.getSession().setAttribute("wrongcreds", true);
		userInfo.put("wrongcreds", true);
		return "html/index.html";
	}
	
	public String logout(HttpServletRequest req) {
		if(!req.getMethod().equals("POST")) {
			log.info("logout submission was not a POST method");
			System.out.println("unsucessful logout");
			User user = (User) req.getSession().getAttribute("currentUser");
			if(user.getUserRole().getRole().equals("Employee"))
				return "html/employee.html";
			return "html/manager.html";
		}
		
		HttpSession session = req.getSession(false);
		if (session != null) {
			userInfo = new HashMap<>();
		    session.invalidate();
			log.info("session invalidated");
		}
		log.info("logged out");
		return "html/index.html";
	}
	
	public void getSessionUser(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException, IOException {
		User user = (User) req.getSession().getAttribute("currentUser");
		userInfo.put("user", user);
		log.info("user session requested");
	}
}
