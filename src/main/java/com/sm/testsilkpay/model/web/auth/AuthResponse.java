package com.sm.testsilkpay.model.web.auth;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class AuthResponse {

  private String accessToken;
  private String refreshToken;

}
