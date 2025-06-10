package com.univille.fso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.univille.fso.entity.Developer;

public interface DeveloperRepository extends JpaRepository<Developer, Long> {

}
