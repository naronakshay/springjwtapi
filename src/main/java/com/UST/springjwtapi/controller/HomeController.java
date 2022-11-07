package com.UST.springjwtapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.UST.springjwtapi.model.JwtRequest;
import com.UST.springjwtapi.model.JwtResponse;
import com.UST.springjwtapi.service.UserService;
import com.UST.springjwtapiu.utility.JwUtility;

public class HomeController {
	@Autowired
	private JwUtility jwUtility;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userservice;
	
	@GetMapping("/")
	public String home() {
		return "Welcome to daily Daily Code Buffer!!";
		
	}
	
	@PostMapping("/authenticate")
	public JwtResponse authenticate (@RequestBody JwtRequest jwtRequest) throws Exception{
		
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							jwtRequest.getUsername(),
							jwtRequest.getPassword()
							));
		}
		catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS",e);
		}
		
		final UserDetails userDetails  = userservice.loadUserByUsername(jwtRequest.getUsername());
		
		final String token = jwUtility.generateToken(userDetails);
		 
		return new JwtResponse(token);
		//return new JwtResponse(token);
		
	}
	
	
	
	
	
	

}
