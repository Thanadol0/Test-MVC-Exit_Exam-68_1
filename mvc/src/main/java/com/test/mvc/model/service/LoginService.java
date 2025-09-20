package com.test.mvc.model.service;

import com.test.mvc.model.entity.User;
import com.test.mvc.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    public Optional<User> authenticate(String username, String password) {
        return userRepository.findByUsernameAndPassword(username, password);
    }

}
