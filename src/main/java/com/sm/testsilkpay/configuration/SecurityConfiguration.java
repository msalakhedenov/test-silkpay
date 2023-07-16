package com.sm.testsilkpay.configuration;

import com.sm.testsilkpay.util.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
    return http.httpBasic(AbstractHttpConfigurer::disable)
               .csrf(AbstractHttpConfigurer::disable)
               .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .authorizeHttpRequests(
                   auth -> auth.requestMatchers("/api/auth/login", "/api/auth/signup", "/api/auth/token").permitAll()
                               .anyRequest().authenticated())
               .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
               .build();
  }

}
