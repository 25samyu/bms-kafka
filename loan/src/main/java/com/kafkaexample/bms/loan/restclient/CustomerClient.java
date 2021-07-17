package com.kafkaexample.bms.loan.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${feign.customerClientName}", url = "${feign.customerClientUrl}")
public interface CustomerClient {

	@GetMapping("/email")
	public ResponseEntity<String> getEmail(@RequestParam Long accountNumber);
}
