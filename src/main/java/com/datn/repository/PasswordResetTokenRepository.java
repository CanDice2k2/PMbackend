package com.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.datn.model.PasswordResetToken;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
	PasswordResetToken findByToken(String token);
}
