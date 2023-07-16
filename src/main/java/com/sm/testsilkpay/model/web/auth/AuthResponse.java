package com.sm.testsilkpay.model.web.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Schema(description = "An object containing access and refresh tokens")
public class AuthResponse {

  @Schema(description = "Access token", example = "abed1234edf13")
  private String accessToken;

  @Schema(description = "Refresh token", example = "abed1234edf13")
  private String refreshToken;

}
