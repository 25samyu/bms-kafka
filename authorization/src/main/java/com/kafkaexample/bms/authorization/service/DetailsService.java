package com.kafkaexample.bms.authorization.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.kafkaexample.bms.authorization.model.UserCredentials;
import com.kafkaexample.bms.authorization.repository.UserCredentialsRepository;

@Service
public class DetailsService implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(UserDetailsService.class);

	@Autowired
	private UserCredentialsRepository userCredentialsRepository;

	@Override
	public UserDetails loadUserByUsername(String uname) {
		logger.info("Start");
		try {
			UserCredentials userCredentials = userCredentialsRepository.findById(uname).orElse(null);
			if (userCredentials != null) {
				logger.info("End - Success");
				return new User(userCredentials.getUsername(), userCredentials.getPassword(), new ArrayList());
			} else {
				logger.info("End - User not found");
				return null;
			}
		} catch (Exception e) {
			logger.error("End - Exception");
			throw e;
		}

	}

	public boolean saveCredentials(UserCredentials userCredentials) {
		logger.info("Start");
		try {
			userCredentialsRepository.save(userCredentials);
			logger.info("End - Success");
			return true;
		} catch (Exception e) {
			logger.error("End - Exception");
			return false;
		}
	}

}
