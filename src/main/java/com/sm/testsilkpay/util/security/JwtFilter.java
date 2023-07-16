package com.sm.testsilkpay.util.security;

import com.sm.testsilkpay.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtFilter extends GenericFilterBean {

  private final static String BEARER = "Bearer ";

  private final JwtService jwtService;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws ServletException, IOException {
    String token = extractToken((HttpServletRequest) request);

    if (token != null && jwtService.validateAccessToken(token)) {
      Claims            claims         = jwtService.getAccessClaims(token);
      String            username       = claims.getSubject();
      JwtAuthentication authentication = new JwtAuthentication(username, true);

      SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    chain.doFilter(request, response);
  }

  private String extractToken(HttpServletRequest httpServletRequest) {
    String authorization = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

    if (authorization != null && authorization.startsWith(BEARER)) {
      return authorization.substring(BEARER.length());
    }

    return null;
  }

}
