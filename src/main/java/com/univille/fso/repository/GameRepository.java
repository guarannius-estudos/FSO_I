package com.univille.fso.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.univille.fso.entity.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByNameContainingIgnoreCase(String name);
}
