package com.sm.testsilkpay.service;

import com.sm.testsilkpay.model.entity.User;
import com.sm.testsilkpay.model.web.auth.AuthRequest;

public interface UserService {

  void createUser(AuthRequest createUserRequest);

  User findByUsername(String username);

}
