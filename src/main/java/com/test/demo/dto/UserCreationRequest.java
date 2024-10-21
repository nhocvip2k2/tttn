	package com.test.demo.dto;

import java.util.Set;

import jakarta.validation.constraints.Size;

public class UserCreationRequest {
	private String username;
	@Size(min=8, message = "mat khau toi thieu 8 ki tu")
	private String password;
	private Set < String> roles;
	public UserCreationRequest() {
		super();
		// TODO Auto-generated constructor stub
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
	public Set<String> getRoles() {
		return roles;
	}
	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
		
}
