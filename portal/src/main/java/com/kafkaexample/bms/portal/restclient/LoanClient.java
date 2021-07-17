package com.kafkaexample.bms.portal.restclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${feign.loanClientName}", url = "${feign.loanClientUrl}")
public interface LoanClient {

	@GetMapping("/retrieveLoan")
	public void retrieveLoans(@RequestParam Long accountNumber);
}
