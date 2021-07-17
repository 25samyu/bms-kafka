package com.kafkaexample.bms.authorization.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "userdata")
public class UserCredentials {

	@Column(name = "password")
	private String password;
	@Id
	@Column(name = "username")
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
