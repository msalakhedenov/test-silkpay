package com.sm.testsilkpay.service.impl;

import com.sm.testsilkpay.model.entity.User;
import com.sm.testsilkpay.service.JwtService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

  private final SecretKey jwtAccessSecretKey;
  private final SecretKey jwtRefreshSecretKey;

  public JwtServiceImpl(@Value("${jwt.secret.access}") String jwtAccessSecret,
                        @Value("${jwt.secret.refresh}") String jwtRefreshSecret) {
    jwtAccessSecretKey  = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
    jwtRefreshSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(jwtRefreshSecret));
  }

  @Override
  public String generateAccessToken(User user) {
    Instant accessExpirationInstant = LocalDateTime.now().plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant();
    Date    accessExpirationDate    = Date.from(accessExpirationInstant);

    return Jwts.builder()
               .setSubject(user.getUsername())
               .setExpiration(accessExpirationDate)
               .signWith(jwtAccessSecretKey)
               .compact();
  }

  @Override
  public String generateRefreshToken(User user) {
    Instant accessExpirationInstant = LocalDateTime.now().plusDays(14).atZone(ZoneId.systemDefault()).toInstant();
    Date    accessExpirationDate    = Date.from(accessExpirationInstant);

    return Jwts.builder()
               .setSubject(user.getUsername())
               .setExpiration(accessExpirationDate)
               .signWith(jwtRefreshSecretKey)
               .compact();
  }

  @Override
  public boolean validateAccessToken(String accessToken) {
    return validateToken(accessToken, jwtAccessSecretKey);
  }

  @Override
  public boolean validateRefreshToken(String refreshToken) {
    return validateToken(refreshToken, jwtRefreshSecretKey);
  }

  @Override
  public Claims getAccessClaims(String accessToken) {
    return getClaims(accessToken, jwtAccessSecretKey);
  }

  @Override
  public Claims getRefreshClaims(String refreshToken) {
    return getClaims(refreshToken, jwtRefreshSecretKey);
  }

  private boolean validateToken(String token, SecretKey key) {
    try {
      Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parse(token);

      return true;

    } catch (ExpiredJwtException e) {
      log.error("Token expired", e);
    } catch (UnsupportedJwtException e) {
      log.error("Unsupported jwt", e);
    } catch (MalformedJwtException e) {
      log.error("Malformed jwt", e);
    } catch (SignatureException e) {
      log.error("Invalid signature", e);
    } catch (Exception e) {
      log.error("Invalid token", e);
    }

    return false;
  }

  private Claims getClaims(String token, SecretKey key) {
    return Jwts.parserBuilder()
               .setSigningKey(key)
               .build()
               .parseClaimsJws(token)
               .getBody();
  }

}
