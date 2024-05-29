package com.Rest.RestFull.configs;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.Rest.RestFull.security.JWTUtil;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;
	
	@Autowired 
	private JWTUtil jwtUtil;

    private static final String[] PUBLIC_MATCHERS = { "/" };

    private static final String[] PUBLIC_MATCHERS_POST = { "/user", "/login" };
    
    private static final String[] PUBLIC_MATCHERS_PUT = { "/user/{id}" };
    
    private static final String[] PUBLIC_MATCHERS_GET = { "/user/{id}" };



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable();
        
        AuthenticationManagerBuilder authenticationManagerBuilder = http
        	.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(this.userDetailsService)
        	.passwordEncoder(bCryptPasswordEncoder);
        this.authenticationManager = authenticationManagerBuilder.build();
            http.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
                    .requestMatchers(HttpMethod.PUT, PUBLIC_MATCHERS_PUT).permitAll()
                    .requestMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
                    .requestMatchers(PUBLIC_MATCHERS).permitAll()
                    .anyRequest().authenticated()
            )
            .sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}