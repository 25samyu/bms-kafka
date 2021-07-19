package com.kafkaexample.bms.authorization.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jdk.internal.org.jline.utils.Log;

@Service
public class JwtUtil {

	private String secretkey = "${jwt.secret}";

	public String extractUsername(String token) {
		Log.info("Start");
		Log.info("End - Success");
		return extractClaim(token, Claims::getSubject);

	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		Log.info("Start");
		final Claims claims = extractAllClaims(token);
		Log.info("End - Success");
		return claimsResolver.apply(claims);

	}

	private Claims extractAllClaims(String token) {
		Log.info("Start");
		Log.info("End - Success");
		return Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token).getBody();

	}

	public String generateToken(UserDetails userDetails) {
		Log.info("Start");
		Log.info("End - Success");
		return createToken(new HashMap<>(), userDetails.getUsername());
	}

	private String createToken(Map<String, Object> claims, String subject) {
		Log.info("Start");
		Log.info("End - Success");
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1800000))
				.signWith(SignatureAlgorithm.HS256, secretkey).compact();

	}

	public Boolean validateToken(String token) {
		Log.info("Start");
		try {
			Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token).getBody();
			Log.info("End - Success");
			return true;
		} catch (Exception e) {
			Log.error("End - Exception");
			return false;
		}

	}
}