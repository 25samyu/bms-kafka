package com.kafkaexample.bms.authorization.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.kafkaexample.bms.authorization.model.UserCredentials;
import com.kafkaexample.bms.authorization.repository.UserCredentialsRepository;

import jdk.internal.org.jline.utils.Log;

@Service
public class DetailsService implements UserDetailsService {

	@Autowired
	private UserCredentialsRepository userCredentialsRepository;

	@Override
	public UserDetails loadUserByUsername(String uname) {
		Log.info("Start");
		try {
			UserCredentials userCredentials = userCredentialsRepository.findById(uname).orElse(null);
			if (userCredentials != null) {
				Log.info("End - Success");
				return new User(userCredentials.getUsername(), userCredentials.getPassword(), new ArrayList());
			} else {
				Log.info("End - User not found");
				return null;
			}
		} catch (Exception e) {
			Log.error("End - Exception");
			throw e;
		}

	}

	public boolean saveCredentials(UserCredentials userCredentials) {
		Log.info("Start");
		try {
			userCredentialsRepository.save(userCredentials);
			Log.info("End - Success");
			return true;
		} catch (Exception e) {
			Log.error("End - Exception");
			return false;
		}
	}

}
