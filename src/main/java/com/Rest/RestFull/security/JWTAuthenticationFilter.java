package com.Rest.RestFull.security;

import java.io.IOException;
import java.rmi.ServerException;
import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.Rest.RestFull.exceptions.GlobalExceptionHandler;
import com.Rest.RestFull.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;


import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//cai tudo aqui do login 
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private AuthenticationManager authenticationManager;

	private JWTUtil jwtUtil;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		super();
		setAuthenticationFailureHandler(new GlobalExceptionHandler());
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			User userCreadentials = new ObjectMapper().readValue(request.getInputStream(), User.class);

			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					userCreadentials.getUsername(), userCreadentials.getPassword(), new ArrayList<>());
			
			
			Authentication authentication = this.authenticationManager.authenticate(authToken);
			return authentication;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	protected void successfulAuthentication(HttpServletRequest request,
		HttpServletResponse response, FilterChain filterChain, Authentication authentication) throws IOException, ServerException{
		UserSpringSecurity userSpringSecurity = (UserSpringSecurity) authentication.getPrincipal();
		String username =userSpringSecurity.getUsername();
		String token= this.jwtUtil.genereateToken(username);
		response.addHeader("Authorization", "Bearer "+token);
		response.addHeader("access-control-expose-headers","Authorization");
	}

	
	
	
	
	
}
