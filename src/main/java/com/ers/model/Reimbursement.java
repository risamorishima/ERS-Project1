package com.ers.model;

import java.sql.Date;

public class Reimbursement {
	private int id;
	private float amount;
	private String description;
	private Date submitDate;
	private Date resolvedDate;
	private int authorId;
	private int resolverId;
	private ReimbursementStatus status;
	private ReimbursementType type;
	
	
	public Reimbursement() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Reimbursement(float amount, String description, Date submitDate, int authorId, ReimbursementStatus status,
			ReimbursementType type) {
		super();
		this.amount = amount;
		this.description = description;
		this.submitDate = submitDate;
		this.authorId = authorId;
		this.status = status;
		this.type = type;
	}

	public Reimbursement(int id, float amount, String description, Date submitDate, Date resolvedDate, int authorId,
			int resolverId, ReimbursementStatus status, ReimbursementType type) {
		super();
		this.id = id;
		this.amount = amount;
		this.description = description;
		this.submitDate = submitDate;
		this.resolvedDate = resolvedDate;
		this.authorId = authorId;
		this.resolverId = resolverId;
		this.status = status;
		this.type = type;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	public Date getResolvedDate() {
		return resolvedDate;
	}

	public void setResolvedDate(Date resolvedDate) {
		this.resolvedDate = resolvedDate;
	}

	public int getAuthorId() {
		return authorId;
	}

	public int getResolverId() {
		return resolverId;
	}

	public void setResolverId(int resolverId) {
		this.resolverId = resolverId;
	}

	public ReimbursementStatus getStatus() {
		return status;
	}

	public void setStatus(ReimbursementStatus status) {
		this.status = status;
	}

	public ReimbursementType getType() {
		return type;
	}

	public void setType(ReimbursementType type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Reimbursement [id=" + id + ", amount=" + amount + ", description=" + description + ", submitDate="
				+ submitDate + ", resolvedDate=" + resolvedDate + ", authorId=" + authorId + ", resolverId="
				+ resolverId + ", status=" + status + ", type=" + type + "]";
	}
}
