package com.ers.dao;

import com.ers.model.Reimbursement;

public interface ReimbursementDao extends GenericDao<Reimbursement>{
	public void insert(Reimbursement entity);
	public int getStatusId(String status);
	public int getTypeId(String type);
}
