package com.univille.fso.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.univille.fso.entity.Developer;
import com.univille.fso.repository.DeveloperRepository;

@Service
public class DeveloperService {

    @Autowired
    private DeveloperRepository repository;

    public Optional<Developer> findById(long id) {
        return repository.findById(id);
    }

    public List<Developer> findAll() {
        return repository.findAll(Sort.by("name"));
    }

    public void save(Developer developer) {
        if(Strings.isBlank(developer.getName())) {
            throw new RuntimeException("Nome não informado.");
        }
        repository.save(developer);
    }

    public void delete(Developer developer) {
        repository.delete(developer);
    }
}
