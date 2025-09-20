package com.test.mvc.model.repository;

import com.test.mvc.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@org.springframework.stereotype.Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByUsernameAndPassword(String username, String password);
}

