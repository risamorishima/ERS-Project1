package com.ers.service;

import java.util.List;

import com.ers.AES;
import com.ers.UserController;
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
		return decryptPassword(user);
	}
	
	public User getByUsername(String name) {
		User user = uDao.getByUsername(name);
		if(user.getUsername() == null)
			throw new NullPointerException("No user was found by username " + name);
		return decryptPassword(user);
	}
	
	public List<User> getAllEmployee(){
		List<User> users = uDao.getAll();
		users.removeIf((User user) -> !user.getUserRole().getRole().equals("Employee"));
		for(int i = 0; i < users.size(); i++) {
			users.set(i, decryptPassword(users.get(i)));
		}
		return users;
	}
	
	public User decryptPassword(User user) {
		AES aes = new AES();
		User updatedUser = user;
		updatedUser.setPassword(aes.decrypt(user.getPassword(), UserController.SECRETKEY));
		return updatedUser;
	}
}
