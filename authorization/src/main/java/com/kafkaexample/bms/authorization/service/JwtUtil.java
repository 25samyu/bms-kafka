package com.kafkaexample.bms.authorization.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

	private String secretkey = "${jwt.secret}";

	public String extractUsername(String token) {
		logger.info("Start");
		logger.info("End - Success");
		return extractClaim(token, Claims::getSubject);

	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		logger.info("Start");
		final Claims claims = extractAllClaims(token);
		logger.info("End - Success");
		return claimsResolver.apply(claims);

	}

	private Claims extractAllClaims(String token) {
		logger.info("Start");
		logger.info("End - Success");
		return Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token).getBody();

	}

	public String generateToken(UserDetails userDetails) {
		logger.info("Start");
		logger.info("End - Success");
		return createToken(new HashMap<>(), userDetails.getUsername());
	}

	private String createToken(Map<String, Object> claims, String subject) {
		logger.info("Start");
		logger.info("End - Success");
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1800000))
				.signWith(SignatureAlgorithm.HS256, secretkey).compact();

	}

	public Boolean validateToken(String token) {
		logger.info("Start");
		try {
			Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token).getBody();
			logger.info("End - Success");
			return true;
		} catch (Exception e) {
			logger.error("End - Exception");
			return false;
		}

	}
}