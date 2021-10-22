package com.ers;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.ers.dao.DBConnection;
import com.ers.dao.ReimbursementDaoImpl;
import com.ers.dao.UserDaoImpl;
import com.ers.model.Reimbursement;
import com.ers.model.ReimbursementStatus;
import com.ers.model.ReimbursementType;
import com.ers.model.User;
import com.ers.service.ReimbursementService;
import com.ers.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ReimbursementController {
	public final Logger log = Logger.getLogger(ReimbursementController.class);
	DBConnection con = new DBConnection();
	ReimbursementDaoImpl rDao = new ReimbursementDaoImpl(con);
	
	public ReimbursementController(ReimbursementDaoImpl rDao) {
		this.rDao = rDao;
	}
	
	public String submitTicket(HttpServletRequest req) {
		if(!req.getMethod().equals("POST")) {
			System.out.println("unsucessful submission");
			log.info("ticket submission was not a POST method");
			return "html/index.html";
		}
		
		HttpSession session = req.getSession(false);
		if (session != null) {
			User user = (User) req.getSession().getAttribute("currentUser");
			System.out.println(user);
			if(user.getUserRole().getRole().equals("Employee")) {
				ReimbursementService rServ = new ReimbursementService(rDao);
				try {
					float amount = Float.parseFloat(req.getParameter("amount"));
					ReimbursementStatus status = new ReimbursementStatus(rServ.getStatusId("Pending"), "Pending");
					ReimbursementType type = new ReimbursementType(rServ.getTypeId(req.getParameter("type")), req.getParameter("type"));
					
					Reimbursement ticket = new Reimbursement(amount, req.getParameter("description"), new Date(System.currentTimeMillis()), user.getId(), status, type);
					rDao.insert(ticket);
				}catch(NullPointerException e) {
					log.error("status/type error");
				}catch(NumberFormatException e) {
					log.error("amount input was not a float");
				}
				log.info("employee "+user.getId()+" submitted ticket");
				return "html/employee.html";
			}
			log.warn("non-employee tried to submit ticket");
			return "html/manager.html";
		}
		log.warn("non-employee tried to submit  ticket");
		return "html/index.html";
	}
	
	public String updateTicket(HttpServletRequest req) {
		if(!req.getMethod().equals("POST")) {
			System.out.println("unsucessful update");
			log.info("ticket update was not a POST method");
			return "html/index.html";
		}
		
		HttpSession session = req.getSession(false);
		if (session != null) {
			User user = (User) req.getSession().getAttribute("currentUser");
			if(user.getUserRole().getRole().equals("Employee")) {
				ReimbursementService rServ = new ReimbursementService(rDao);
				try {
					Reimbursement ticket = rServ.getById(Integer.parseInt(req.getParameter("id")));
					if(!req.getParameter("description").equals("")) {
						ticket.setDescription(req.getParameter("description"));
					}
					if(!req.getParameter("amount").equals("")) {
						float amount = Float.parseFloat(req.getParameter("amount"));
						ticket.setAmount(amount);
					}
					if(!req.getParameter("type").equals("")) {
						ReimbursementType type = new ReimbursementType(rServ.getTypeId(req.getParameter("type")), req.getParameter("type"));
						ticket.setType(type);
					}
					rDao.update(ticket);
				}catch(NullPointerException e) {
					log.error("status/type error");
				}catch(NumberFormatException e) {
					log.error("amount input was not a float");
				}
				log.info("employee "+user.getId()+" updated ticket");
				return "html/employee.html";
			}
			ReimbursementService rServ = new ReimbursementService(rDao);
			if(req.getParameter("Approved") != null) {
				try {
					Reimbursement ticket = rServ.getById(Integer.parseInt(req.getParameter("Approved")));
					ticket.setResolverId(user.getId());
					ticket.setResolvedDate(new Date(System.currentTimeMillis()));
					ticket.setStatus(new ReimbursementStatus(rServ.getStatusId("Approved"), "Approved"));
					rDao.update(ticket);
				}catch(NullPointerException e) {
					e.printStackTrace();
				}
			}
			if(req.getParameter("Denied") != null) {

				try {
					Reimbursement ticket = rServ.getById(Integer.parseInt(req.getParameter("Denied")));
					ticket.setResolverId(user.getId());
					ticket.setResolvedDate(new Date(System.currentTimeMillis()));
					ticket.setStatus(new ReimbursementStatus(rServ.getStatusId("Denied"), "Denied"));
					rDao.update(ticket);
				}catch(NullPointerException e) {
					e.printStackTrace();
				}
			}
			log.info("manager "+user.getId()+" submitted ticket");
			return "html/manager.html";
		}
		log.warn("non-employee tried to submit  ticket");
		return "html/index.html";
	}
	
	public void getSessionTicket(HttpServletRequest req, HttpServletResponse res) throws JsonProcessingException, IOException{
		HttpSession session = req.getSession(false);
		if(session != null) {
			User user = (User) req.getSession().getAttribute("currentUser");
			ReimbursementService rServ = new ReimbursementService(rDao);
			List<Reimbursement> tickets;
			if(user.getUserRole().getRole().equals("Employee"))
				tickets = rServ.getAllByUserId(user.getId());	
			else {
				UserDaoImpl uDao = new UserDaoImpl(con);
				UserService uServ = new UserService(uDao);
				List<User> employees = uServ.getAllEmployee();
				req.getSession().setAttribute("employees", employees);
				UserController.userInfo.put("employees", employees);
				tickets = rDao.getAll();
			}
			req.getSession().setAttribute("tickets", tickets);
			UserController.userInfo.put("tickets", tickets);
			log.info("ticket session requested");
		}
	}
}
