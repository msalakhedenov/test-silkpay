package com.sm.testsilkpay.service;

import com.sm.testsilkpay.model.web.auth.AuthRequest;
import com.sm.testsilkpay.model.web.auth.AuthResponse;

public interface AuthService {

  AuthResponse login(AuthRequest authRequest);

  AuthResponse getAccessToken(String refreshToken);

  AuthResponse refresh(String refreshToken);

}
