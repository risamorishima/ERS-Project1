package com.ers.model;

public class ReimbursementStatus {
	private int id;
	private String status;
	
	public ReimbursementStatus() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ReimbursementStatus(int id, String status) {
		super();
		this.id = id;
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "ReimbursementStatus [id=" + id + ", status=" + status + "]";
	}
}
