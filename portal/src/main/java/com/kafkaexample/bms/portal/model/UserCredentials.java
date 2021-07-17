package com.kafkaexample.bms.portal.model;

import org.springframework.stereotype.Component;

@Component

public class UserCredentials {

	private String password;
	private String username;
	private String token;

	public String getPassword() {
		return password;
	}

	public void setPassword(String upassword) {
		this.password = upassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String uname) {
		this.username = uname;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserCredentials(String username, String password, String token) {
		super();
		this.password = password;
		this.username = username;
		this.token = token;
	}

	public UserCredentials() {

	}

}
