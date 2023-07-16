package com.sm.testsilkpay.service.impl;

import com.sm.testsilkpay.exception.AuthException;
import com.sm.testsilkpay.model.entity.User;
import com.sm.testsilkpay.model.web.auth.AuthRequest;
import com.sm.testsilkpay.repository.UserRepository;
import com.sm.testsilkpay.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository  userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void createUser(AuthRequest createUserRequest) {
    String username = createUserRequest.getUsername();

    if (userRepository.findByUsername(username).isPresent()) {
      throw new AuthException("Username already exists", HttpStatus.CONFLICT);
    }

    User user = new User();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(createUserRequest.getPassword()));
    userRepository.save(user);
  }

  @Override
  public User findByUsername(String username) {
    return userRepository.findByUsername(username)
                         .orElseThrow(() -> new UsernameNotFoundException("Could not find a user with username = " + username));
  }

}
