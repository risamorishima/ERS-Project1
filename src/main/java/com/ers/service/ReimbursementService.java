package com.ers.service;

import java.util.List;

import com.ers.dao.ReimbursementDao;
import com.ers.model.Reimbursement;

public class ReimbursementService {
	private ReimbursementDao rDao;

	public ReimbursementService(ReimbursementDao rDao) {
		super();
		this.rDao = rDao;
	}
	
	public List<Reimbursement> getAllByUserId(int id){
		List<Reimbursement> tickets = rDao.getAll();
		tickets.removeIf((Reimbursement ticket) -> ticket.getAuthorId() != id);
		return tickets;
	}
	
	public Reimbursement getById(int id) {
		Reimbursement ticket = rDao.getById(id);
		if(ticket.getDescription() == null)
			throw new NullPointerException("Ticket with id "+ id +" does not exist");
		return ticket;
	}
	
	public int getStatusId(String status) {
		int id = rDao.getStatusId(status);
		if(id == 0)
			throw new NullPointerException("Status "+status+" does not exist");
		return id;
	}
	
	public int getTypeId(String type) {
		int id = rDao.getTypeId(type);
		if(id == 0)
			throw new NullPointerException("Type "+type+" does not exist");
		return id;
	}
	
}
