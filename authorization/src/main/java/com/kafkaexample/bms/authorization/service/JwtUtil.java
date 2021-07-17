package com.kafkaexample.bms.authorization.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.kafkaexample.bms.authorization.controller.AuthorizationController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationController.class);

	private String secretkey = "${jwt.secret}";

	public String extractUsername(String token) {
		LOGGER.info("Start - extractUsername");
		LOGGER.info("End - extractUsername - Successful");

		return extractClaim(token, Claims::getSubject);

	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		LOGGER.info("Start - extractClaim");

		final Claims claims = extractAllClaims(token);
		LOGGER.info("End - extractClaim - Successful");

		return claimsResolver.apply(claims);

	}

	private Claims extractAllClaims(String token) {
		LOGGER.info("Start - extractAllClaims");
		LOGGER.info("End - extractAllClaims - Successful");

		return Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token).getBody();

	}

	public String generateToken(UserDetails userDetails) {

		return createToken(new HashMap<>(), userDetails.getUsername());
	}

	private String createToken(Map<String, Object> claims, String subject) {
		LOGGER.info("Start - createToken");

		String compact = Jwts.builder().setClaims(claims).setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1800000))
				.signWith(SignatureAlgorithm.HS256, secretkey).compact();
		LOGGER.info("End - createToken - Successful");

		return compact;
	}

	public Boolean validateToken(String token) {
		LOGGER.info("Start - validateToken");

		try {
			Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token).getBody();
			LOGGER.info("Start - validateToken - Successful");

			return true;
		} catch (Exception e) {
			LOGGER.info("Start - validateToken - Invalid token");

			return false;
		}

	}
}