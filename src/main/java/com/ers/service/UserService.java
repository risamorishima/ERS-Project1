package com.ers.service;

import com.ers.dao.UserDaoImpl;
import com.ers.model.User;

public class UserService {
	private UserDaoImpl uDao;

	public UserService(UserDaoImpl uDao) {
		super();
		this.uDao = uDao;
	}
	
	public User getById(int id) {
		User user = uDao.getById(id);
		if(user.getUsername() == null)
			throw new NullPointerException("No user was found by id" + id);
		return user;
	}
	
	public User getByUsername(String name) {
		User user = uDao.getByUsername(name);
		if(user.getUsername() == null)
			throw new NullPointerException("No user was found by username " + name);
		return user;
	}
}
