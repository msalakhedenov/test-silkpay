package com.sm.testsilkpay.model.web.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Schema(description = "An object containing refresh token")
public class RefreshJwtRequest {

  @Schema(description = "Refresh token", example = "abed1234edf13")
  private String refreshToken;

}
