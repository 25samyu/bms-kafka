package com.kafkaexample.bms.authorization.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.kafkaexample.bms.authorization.controller.AuthorizationController;
import com.kafkaexample.bms.authorization.model.UserCredentials;
import com.kafkaexample.bms.authorization.repository.UserCredentialsRepository;

@Service
public class DetailsService implements UserDetailsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationController.class);

	@Autowired
	private UserCredentialsRepository userCredentialsRepository;

	@Override
	public UserDetails loadUserByUsername(String uname) {
		LOGGER.info("Start - loadUserByUsername");
		try {
			UserCredentials userCredentials = userCredentialsRepository.findById(uname).orElse(null);
			if (userCredentials != null) {
				return new User(userCredentials.getUsername(), userCredentials.getPassword(), new ArrayList());
			} else {
				LOGGER.info("End - loadUserByUsername - Username Not Found");

				return null;
			}
		} catch (Exception e) {
			LOGGER.info("Exception - loadUserByUsername - InternalServerError");

			throw e;
		}

	}

	public boolean saveCredentials(UserCredentials userCredentials) {
		try {
			userCredentialsRepository.save(userCredentials);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
