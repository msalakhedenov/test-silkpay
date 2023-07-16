package com.sm.testsilkpay.controller;

import com.sm.testsilkpay.model.web.auth.AuthRequest;
import com.sm.testsilkpay.model.web.auth.AuthResponse;
import com.sm.testsilkpay.model.web.auth.RefreshJwtRequest;
import com.sm.testsilkpay.service.AuthService;
import com.sm.testsilkpay.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

  private final AuthService authService;
  private final UserService userService;

  @PostMapping("/signup")
  public void signup(@RequestBody AuthRequest authRequest) {
    userService.createUser(authRequest);
  }

  @PostMapping("/login")
  public AuthResponse login(@RequestBody AuthRequest authRequest) {
    return authService.login(authRequest);
  }

  @PostMapping("/token")
  public AuthResponse getAccessToken(@RequestBody RefreshJwtRequest refreshJwtRequest) {
    return authService.getAccessToken(refreshJwtRequest.getRefreshToken());
  }

  @PostMapping("/refresh")
  public AuthResponse getRefreshToken(@RequestBody RefreshJwtRequest refreshJwtRequest) {
    return authService.refresh(refreshJwtRequest.getRefreshToken());
  }

}
