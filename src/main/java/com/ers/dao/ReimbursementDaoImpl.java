package com.ers.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.ers.model.Reimbursement;
import com.ers.model.ReimbursementStatus;
import com.ers.model.ReimbursementType;

public class ReimbursementDaoImpl implements ReimbursementDao{
	
	private DBConnection dbCon;

	public ReimbursementDaoImpl(DBConnection dbCon) {
		super();
		this.dbCon = dbCon;
	}

	@Override
	public List<Reimbursement> getAll() {
		List<Reimbursement> reimbursementList = new ArrayList<>();
		try(Connection con = dbCon.getDBConnection()){
			String sql = "select * from reimbursements r left outer join reimbursement_status s on r.status_id = s.id left outer join reimbursement_type t on r.type_id = t.id order by r.submitted desc";
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery(sql);
			while(rs.next()) {
				reimbursementList.add(
						new Reimbursement(rs.getInt(1), rs.getFloat(2), rs.getString(3), rs.getDate(4), rs.getDate(5), rs.getInt(6), rs.getInt(7), new ReimbursementStatus(rs.getInt(8), rs.getString(11)), new ReimbursementType(rs.getInt(9), rs.getString(13))));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return reimbursementList;
	}

	@Override
	public Reimbursement getById(int id) {
		try(Connection con = dbCon.getDBConnection()){
			String sql = "select * from reimbursements r left outer join reimbursement_status s on r.status_id = s.id left outer join reimbursement_type t on r.type_id = t.id where r.id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			Reimbursement reimbursement = new Reimbursement();
			while(rs.next())
				reimbursement = new Reimbursement(rs.getInt(1), rs.getFloat(2), rs.getString(3), rs.getDate(4), rs.getDate(5), rs.getInt(6), rs.getInt(7), new ReimbursementStatus(rs.getInt(8), rs.getString(11)), new ReimbursementType(rs.getInt(9), rs.getString(13)));
			return reimbursement;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Reimbursement insert(Reimbursement entity) {
		try(Connection con = dbCon.getDBConnection()){
			String sql = "{? = call insert_reimbursement(?,?,?,?,?,?)}";
			CallableStatement cs = con.prepareCall(sql);
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.setFloat(2, entity.getAmount());
			cs.setString(3, entity.getDescription());
			cs.setDate(4, entity.getSubmitDate());
			cs.setInt(5, entity.getAuthorId());
			cs.setInt(6, entity.getStatus().getId());
			cs.setInt(7, entity.getType().getId());
			cs.execute();
			return entity;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Reimbursement update(Reimbursement entity) {
		try(Connection con = dbCon.getDBConnection()){
			String sql = "{? = call update_reimbursement(?,?,?,?,?,?,?)}";
			CallableStatement cs = con.prepareCall(sql);
			cs.registerOutParameter(1, Types.VARCHAR);
			cs.setInt(2, entity.getId());
			cs.setFloat(3, entity.getAmount());
			cs.setString(4, entity.getDescription());
			cs.setDate(5, entity.getResolvedDate());
			cs.setInt(6, entity.getResolverId());
			cs.setInt(7, entity.getStatus().getId());
			cs.setInt(8, entity.getType().getId());
			cs.execute();
			return entity;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int getStatusId(String status) {
		int id = 0;
		try(Connection con = dbCon.getDBConnection()){
			String sql = "select * from reimbursement_status where status = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, status);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
				id = rs.getInt(1);
			return id;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public int getTypeId(String type) {
		int id = 0;
		try(Connection con = dbCon.getDBConnection()){
			String sql = "select * from reimbursement_type where type = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, type);
			ResultSet rs = ps.executeQuery();
			while(rs.next())
				id = rs.getInt(1);
			return id;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

}
