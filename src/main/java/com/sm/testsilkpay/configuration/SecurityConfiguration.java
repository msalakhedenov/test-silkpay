package com.sm.testsilkpay.configuration;

import com.sm.testsilkpay.util.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

@Configuration
public class SecurityConfiguration {

  private static final String[] publiclyAvailableRoutes = {
      "/api/auth/login",
      "/api/auth/signup",
      "/api/auth/token",
      "/swagger-ui/**",
      "/api/docs/**"
  };

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {
    return http.httpBasic(AbstractHttpConfigurer::disable)
               .csrf(AbstractHttpConfigurer::disable)
               .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .headers(configurer -> configurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
               .authorizeHttpRequests(auth -> auth.requestMatchers(publiclyAvailableRoutes).permitAll()
                                                  .requestMatchers(antMatcher("/h2-console/**")).permitAll()
                                                  .anyRequest().authenticated())
               .addFilterAfter(jwtFilter, UsernamePasswordAuthenticationFilter.class)
               .build();
  }

}
