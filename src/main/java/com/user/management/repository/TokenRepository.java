package com.user.management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.management.model.Token;

import java.util.Optional;

public interface TokenRepository  extends JpaRepository<Token, Integer> {
    Optional<Token> findByDeviceID(String deviceID);
    Optional<Token> findByUserID(Integer UserID);

}
