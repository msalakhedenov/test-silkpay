package com.sm.testsilkpay.service;

import com.sm.testsilkpay.model.web.auth.AuthRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

  void createUser(AuthRequest createUserRequest);

}
