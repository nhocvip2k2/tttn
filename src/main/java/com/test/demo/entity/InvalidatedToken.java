package com.test.demo.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class InvalidatedToken {

	@Id
	private String id;
	private Date expiryTime;

	// Constructors
	public InvalidatedToken() {}

	public InvalidatedToken(String id, Date expiryTime) {
		this.id = id;
		this.expiryTime = expiryTime;
	}

	// Getters and Setters
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime(Date expiryTime) {
		this.expiryTime = expiryTime;
	}
}

