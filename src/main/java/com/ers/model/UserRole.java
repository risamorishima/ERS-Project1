package com.ers.model;

public class UserRole {
	private int id;
	private String role;
	
	public UserRole() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public UserRole(int id, String role) {
		super();
		this.id = id;
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public String getRole() {
		return role;
	}

	@Override
	public String toString() {
		return "UserRole [id=" + id + ", role=" + role + "]";
	}
}
