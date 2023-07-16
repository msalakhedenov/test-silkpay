package com.sm.testsilkpay.model.web.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login or Signup request object")
public class AuthRequest {

  @Schema(description = "Username", example = "cool_username")
  private String username;

  @Schema(description = "Password", example = "cool_password")
  private String password;

}
