package com.ers.dao;

import com.ers.model.User;

public interface UserDao extends GenericDao<User>{
	public User getByUsername(String username);
}
