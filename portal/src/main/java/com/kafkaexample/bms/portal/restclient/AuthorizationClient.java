package com.kafkaexample.bms.portal.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.kafkaexample.bms.portal.model.UserCredentials;

@FeignClient(name = "${feign.authorizationClientName}", url = "${feign.authorizationClientUrl}")
public interface AuthorizationClient {

	@PostMapping("/login")
	public ResponseEntity<String> login(@RequestBody UserCredentials userCredentials);

	@GetMapping("/validate")
	public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token);

}
