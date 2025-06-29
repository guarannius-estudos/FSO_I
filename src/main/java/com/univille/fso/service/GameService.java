package com.univille.fso.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.univille.fso.entity.AgeRange;
import com.univille.fso.entity.Developer;
import com.univille.fso.entity.Game;
import com.univille.fso.entity.Genre;
import com.univille.fso.repository.AgeRangeRepository;
import com.univille.fso.repository.DeveloperRepository;
import com.univille.fso.repository.GameRepository;
import com.univille.fso.repository.GenreRepository;

@Service
public class GameService {

    @Autowired
    private AgeRangeRepository ageRangeRepository;

    @Autowired
    private DeveloperRepository developerRepository;

    @Autowired
    private GameRepository repository;

    @Autowired
    private GenreRepository genreRepository;

    private static final double CONVERSION_RATE = 0.20;

    public double convertToUSD(double value) {
        return value * CONVERSION_RATE;
    }

    public Optional<Game> findById(long id) {
        return repository.findById(id);
    }

    public List<Game> findByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    public List<Game> findAll() {
        return repository.findAll(Sort.by("name"));
    }

    public List<AgeRange> findAllAgeRanges() {
        return ageRangeRepository.findAll();
    }

    public List<Developer> findAllDevelopers() {
        return developerRepository.findAll();
    }

    public List<Genre> findAllGenres() {
        return genreRepository.findAll();
    }

    public void save(Game game) {
        if(Strings.isBlank(game.getName())) {
            throw new RuntimeException("Nome não informado.");
        }
        repository.save(game);
    }

    public void delete(Game game) {
        repository.delete(game);
    }
}
