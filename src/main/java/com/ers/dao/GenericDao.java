package com.ers.dao;

import java.util.List;

public interface GenericDao<T> {
	List<T> getAll();
	T getById(int id);
	T update(T entity);
}
