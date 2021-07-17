package com.cognizant.zuulgateway;

import javax.servlet.http.HttpSession;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

public class SampleFilter extends ZuulFilter {

	@Override
	public Object run() throws ZuulException {
		RequestContext context = RequestContext.getCurrentContext();

		HttpSession session = context.getRequest().getSession();

		context.addZuulRequestHeader("Authorization", (String) session.getAttribute("TOKEN"));

		return null;
	}

	@Override
	public boolean shouldFilter() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int filterOrder() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public String filterType() {
		// TODO Auto-generated method stub
		return "pre";
	}

}
