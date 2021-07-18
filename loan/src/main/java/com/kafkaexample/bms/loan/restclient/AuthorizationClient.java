package com.kafkaexample.bms.loan.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${feign.authorizationClientName}", url = "${feign.authorizationClientUrl}")
public interface AuthorizationClient {

	@GetMapping("/validate")
	public ResponseEntity<String> validateToken(@RequestHeader("Authorization") String token);

}
