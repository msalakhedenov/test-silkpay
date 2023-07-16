package com.sm.testsilkpay.model.web.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class RefreshJwtRequest {

  private String refreshToken;

}
