package com.UST.springjwtapi.model;

public class JwtResponse {
	
	private String jwttoken;

	

	public JwtResponse(String token) {
		this.jwttoken=token;
	}

	public String getJwttoken() {
		return jwttoken;
	}

	public void setJwttoken(String jwttoken) {
		this.jwttoken = jwttoken;
	}
	

}
