package com.univille.fso.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.univille.fso.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByUsernameAndActiveTrue(String username);
}
