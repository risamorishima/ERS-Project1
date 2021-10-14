package com.ers.dao;

import java.util.List;

public interface GenericDao<T> {
	List<T> getAll();
	T getById(int id);
	void update(T entity);
}
