package com.UST.springjwtapi.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.UST.springjwtapi.service.UserService;
import com.UST.springjwtapiu.utility.JwUtility;

@Component
public class JwtFilter  extends OncePerRequestFilter{
	
	@Autowired 
	private JwUtility jwUtility;
	
	@Autowired 
	private UserService userService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String authorization = request.getHeader("Authorization");
		String token=null;
		String userName=null;
		
		if(null!=authorization && authorization.startsWith("Bearer ") ) {
			token= authorization.substring(7);
			userName=jwUtility.getUsernameFromToken(token);
			
		}
		
		if(null != userName && SecurityContextHolder.getContext().getAuthentication()== null) {
			UserDetails userDetails = userService.loadUserByUsername(userName);
			
			if(jwUtility.validationToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken  usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		
		filterChain.doFilter(request, response);
		
	}
	

}
