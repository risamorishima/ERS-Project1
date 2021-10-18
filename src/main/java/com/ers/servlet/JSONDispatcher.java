package com.ers.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.ers.ReimbursementController;
import com.ers.UserController;
import com.ers.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONDispatcher {
	
	public final Logger log = Logger.getLogger(JSONDispatcher.class);
	
	public void process(HttpServletRequest req, HttpServletResponse res) throws IOException{
		UserController uControl = new UserController();
		ReimbursementController rControl = new ReimbursementController();
		switch(req.getRequestURI()) {
			case "/proj1/getsessionuser.json":
				HttpSession session = req.getSession(false);
				if(session != null) {
					System.out.println("get user session");
					res.setHeader("Cache-Control","no-cache,no-store,must-revalidate");
					res.setHeader("Pragma","no-cache");
					res.setDateHeader("Expires", 0);
					uControl.getSessionUser(req, res);
					rControl.getSessionTicket(req, res);
					res.getWriter().write(new ObjectMapper().writeValueAsString(UserController.userInfo));
					log.info("with login successful: user session info response sent");
					break;
				}
				break;
			case "/proj1/wrongcreds.json":
				if(UserController.userInfo.get("wrongcreds") == null) {
					req.getSession().setAttribute("wrongcreds", false);
					UserController.userInfo.put("wrongcreds", false);
				}
				res.getWriter().write(new ObjectMapper().writeValueAsString(UserController.userInfo));
				log.info("with login unsuccessful: user session info response sent");
				break;
			default:
				System.out.println("in JSON default");
				res.getWriter().write(new ObjectMapper().writeValueAsString(new User()));
				break;
		}
	}
}
