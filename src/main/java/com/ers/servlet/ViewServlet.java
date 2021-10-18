package com.ers.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@MultipartConfig
public class ViewServlet extends HttpServlet{
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{
		ViewDispatcher dispatcher = new ViewDispatcher();
		req.getRequestDispatcher(dispatcher.process(req)).forward(req,res);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{
		ViewDispatcher dispatcher = new ViewDispatcher();
		req.getRequestDispatcher(dispatcher.process(req)).forward(req,res);
	}
}