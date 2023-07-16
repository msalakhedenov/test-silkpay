package com.sm.testsilkpay.service;

import com.sm.testsilkpay.model.entity.User;
import io.jsonwebtoken.Claims;

public interface JwtService {

  String generateAccessToken(User user);

  String generateRefreshToken(User user);

  boolean validateAccessToken(String accessToken);

  boolean validateRefreshToken(String refreshToken);

  Claims getAccessClaims(String accessToken);

  Claims getRefreshClaims(String refreshToken);

}
