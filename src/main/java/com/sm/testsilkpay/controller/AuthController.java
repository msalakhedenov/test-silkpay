package com.sm.testsilkpay.controller;

import com.sm.testsilkpay.model.web.auth.AuthRequest;
import com.sm.testsilkpay.model.web.auth.AuthResponse;
import com.sm.testsilkpay.model.web.auth.RefreshJwtRequest;
import com.sm.testsilkpay.service.AuthService;
import com.sm.testsilkpay.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Required to authenticate users in the system, get access and refresh tokens")
public class AuthController {

  private final AuthService authService;
  private final UserService userService;

  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
      summary = "Sign up users within the system",
      responses = {
          @ApiResponse(responseCode = "201", description = "User created successfully"),
          @ApiResponse(responseCode = "409", description = "Username already taken")
      }
  )
  public void signup(@RequestBody AuthRequest authRequest) {
    userService.createUser(authRequest);
  }

  @PostMapping("/login")
  @Operation(
      summary = "Log into the system",
      responses = {
          @ApiResponse(responseCode = "200", description = "Login with valid credentials, returns access and refresh tokens"),
          @ApiResponse(responseCode = "401", description = "Login with invalid credentials")
      }
  )
  public AuthResponse login(@RequestBody AuthRequest authRequest) {
    return authService.login(authRequest);
  }

  @PostMapping("/token")
  @Operation(
      summary = "Get new access token",
      description = "Update access token by providing refresh token",
      responses = {
          @ApiResponse(responseCode = "200", description = "Valid refresh token provided"),
          @ApiResponse(responseCode = "403", description = "Invalid refresh token provided")
      }
  )
  public AuthResponse getAccessToken(@RequestBody RefreshJwtRequest refreshJwtRequest) {
    return authService.getAccessToken(refreshJwtRequest.getRefreshToken());
  }

  @PostMapping("/refresh")
  @Operation(
      summary = "Get new refresh token",
      description = "Update refresh token by providing existing non-expired refresh token",
      security = @SecurityRequirement(name = "JWT"),
      responses = {
          @ApiResponse(responseCode = "200", description = "Valid refresh token provided"),
          @ApiResponse(responseCode = "403", description = "Invalid refresh token provided")
      }
  )
  public AuthResponse getRefreshToken(@RequestBody RefreshJwtRequest refreshJwtRequest) {
    return authService.refresh(refreshJwtRequest.getRefreshToken());
  }

}
