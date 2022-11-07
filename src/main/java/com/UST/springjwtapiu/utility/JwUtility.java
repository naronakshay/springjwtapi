package com.UST.springjwtapiu.utility;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwUtility  implements Serializable{
	
	
	
	




	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final long Jwt_Token_VALIDATION=5*60*60;
	
	@Value("${jwt.secret}")
	private String secretkey;
	
	public String getUsernameFromToken(String token) {
		
		return getClaimsFromToken(token,Claims::getSubject);
		
	}
	
	public Date getExpirationDateFromToken(String token) {
		
		return getClaimsFromToken(token,Claims::getExpiration);
		
	}
	
	public <T> T getClaimsFromToken(String token, Function<Claims,T>claimsReslover) {
		final Claims claims =getAllClaimsFromToken(token);
		return claimsReslover.apply(claims);
	}
	
	
	private Claims getAllClaimsFromToken (String token) {
		return Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration=getExpirationDateFromToken(token);
		return expiration.before(new Date());
		
	}
	
	public String generateToken(UserDetails userDetails) {
		Map<String,Object> claims= new HashMap<>();
		return doGenerateToken(claims,userDetails.getUsername());
	}

	private String doGenerateToken(Map<String, Object> claims, String username) {
		return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis()+Jwt_Token_VALIDATION*1000))
				.signWith(SignatureAlgorithm.HS512, secretkey).compact();
	}
	
	public Boolean validationToken(String token,UserDetails userDetails) {
		final String username= getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername())&& !isTokenExpired(token));
	}

	
	

}
