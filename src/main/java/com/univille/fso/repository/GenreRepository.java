package com.univille.fso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.univille.fso.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
