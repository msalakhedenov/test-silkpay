package com.sm.testsilkpay.model.web.auth;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

  private String username;
  private String password;

}
