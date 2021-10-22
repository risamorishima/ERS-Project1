package com.ers.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.ers.AES;
import com.ers.model.User;
import com.ers.model.UserRole;

public class UserDaoImpl implements UserDao{
	private DBConnection dbCon;
	AES aes = new AES();
	public UserDaoImpl(DBConnection dbCon) {
		super();
		this.dbCon = dbCon;
	}

	@Override
	public List<User> getAll() {
		List<User> userList = new ArrayList<>();
		try(Connection con = dbCon.getDBConnection()){
			String sql = "select * from users u left outer join user_roles r on u.role_id = r.id";
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery(sql);
			while(rs.next()) {
				userList.add(
						new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), new UserRole(rs.getInt(8), rs.getString(9))));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return userList;
	}

	@Override
	public User getById(int id) {
		try(Connection con = dbCon.getDBConnection()){
			String sql = "select * from users u left outer join user_roles r on u.role_id = r.id where u.id = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			User user = new User();
			while(rs.next())
				user = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), new UserRole(rs.getInt(8), rs.getString(9)));
			return user;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public User getByUsername(String username) {
		try(Connection con = dbCon.getDBConnection()){
			String sql = "select * from users u left outer join user_roles r on u.role_id = r.id where u.username = ?";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			User user = new User();
			while(rs.next())
				user = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), new UserRole(rs.getInt(8), rs.getString(9)));
			return user;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void update(User entity) {
		// TODO Auto-generated method stub
		
	}
}
