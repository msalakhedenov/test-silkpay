package com.sm.testsilkpay.service.impl;

import com.sm.testsilkpay.exception.AuthException;
import com.sm.testsilkpay.model.entity.User;
import com.sm.testsilkpay.model.web.auth.AuthRequest;
import com.sm.testsilkpay.model.web.auth.AuthResponse;
import com.sm.testsilkpay.service.AuthService;
import com.sm.testsilkpay.service.JwtService;
import com.sm.testsilkpay.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final JwtService      jwtService;
  private final UserService     userService;
  private final PasswordEncoder passwordEncoder;

  @Override
  public AuthResponse login(AuthRequest authRequest) {
    User user = userService.findByUsername(authRequest.getUsername());

    if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
      String accessToken  = jwtService.generateAccessToken(user);
      String refreshToken = jwtService.generateRefreshToken(user);

      return AuthResponse.of(accessToken, refreshToken);
    }

    throw new AuthException("Invalid username or password", HttpStatus.UNAUTHORIZED);
  }

  @Override
  public AuthResponse getAccessToken(String refreshToken) {
    if (!jwtService.validateRefreshToken(refreshToken)) {
      return null;
    }

    Claims refreshClaims = jwtService.getRefreshClaims(refreshToken);
    String username      = refreshClaims.getSubject();
    User   user          = userService.findByUsername(username);
    String accessToken   = jwtService.generateAccessToken(user);

    return AuthResponse.of(accessToken, null);
  }

  @Override
  public AuthResponse refresh(String refreshToken) {
    if (!jwtService.validateRefreshToken(refreshToken)) {
      return null;
    }

    Claims refreshClaims   = jwtService.getRefreshClaims(refreshToken);
    String username        = refreshClaims.getSubject();
    User   user            = userService.findByUsername(username);
    String accessToken     = jwtService.generateAccessToken(user);
    String newRefreshToken = jwtService.generateRefreshToken(user);

    return AuthResponse.of(accessToken, newRefreshToken);
  }

  @Override
  public Authentication getAuthInfo() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

}
