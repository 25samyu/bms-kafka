package com.kafkaexample.bms.authorization.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.kafkaexample.bms.authorization.model.UserCredentials;
import com.kafkaexample.bms.authorization.repository.UserCredentialsRepository;

@Service
public class DetailsService implements UserDetailsService {

	@Autowired
	private UserCredentialsRepository userCredentialsRepository;

	@Override
	public UserDetails loadUserByUsername(String uname) {
		try {
			UserCredentials userCredentials = userCredentialsRepository.findById(uname).orElse(null);
			if (userCredentials != null) {
				return new User(userCredentials.getUsername(), userCredentials.getPassword(), new ArrayList());
			} else {

				return null;
			}
		} catch (Exception e) {

			throw e;
		}

	}

	public boolean saveCredentials(UserCredentials userCredentials) {
		try {
			userCredentialsRepository.save(userCredentials);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
