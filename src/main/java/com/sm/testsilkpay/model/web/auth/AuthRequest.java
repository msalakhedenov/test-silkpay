package com.sm.testsilkpay.model.web.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login or Signup request object")
public class AuthRequest {

  @Schema(description = "Username", example = "cool_username")
  @NotBlank
  private String username;

  @Schema(description = "Password", example = "cool_password")
  @NotBlank
  private String password;

}
