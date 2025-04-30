package com.univille.fso.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.univille.fso.entity.Game;
import com.univille.fso.repository.GameRepository;

@Service
public class GameService {

    @Autowired
    private GameRepository repository;

    public Optional<Game> findById(long id) {
        return repository.findById(id);
    }

    public List<Game> findByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    public List<Game> findAll() {
        return repository.findAll(Sort.by("name"));
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
