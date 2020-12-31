package com.ss.uthopia.entity;

import javax.persistence.*;

@Entity(name = "User")
@Table(name = "tbl_users")
public class User {
	@Id
	@GeneratedValue
	private Long userId;
	
	private String name;
	
	private String username;
	
	private String password;
	
	private int role;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}
}
