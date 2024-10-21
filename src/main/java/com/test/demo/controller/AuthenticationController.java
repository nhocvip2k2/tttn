package com.test.demo.controller;

import com.nimbusds.jose.JOSEException;
import com.test.demo.dto.AuthenticationRequest;
import com.test.demo.dto.IntrospectRequest;
import com.test.demo.dto.LogoutRequest;
import com.test.demo.dto.RefreshRequest;
import com.test.demo.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
@Controller
@RestController
public class AuthenticationController {
	@Autowired
	private AuthenticationService authenticationService;
	@PostMapping("/token")
	RefreshRequest check(@RequestBody AuthenticationRequest request) {
		return authenticationService.authentication(request);
	}
	@PostMapping("/Introspect")
	boolean introspect(@RequestBody IntrospectRequest request) throws JOSEException, ParseException {
		return authenticationService.introspect(request);
	}
	@PostMapping("/Logout")
	String logout(@RequestBody LogoutRequest request) throws JOSEException, ParseException {
		  authenticationService.logout(request);
		  return "ok";
	}
}	
