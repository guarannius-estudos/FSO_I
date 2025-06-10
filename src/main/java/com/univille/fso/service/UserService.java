package com.univille.fso.service;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.univille.fso.entity.User;
import com.univille.fso.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public Optional<User> findById(long id) {
        return repository.findById(id);
    }

    public List<User> findAll() {
        return repository.findAll(Sort.by("name"));
    }

    public void save(User user) {
        if(Strings.isBlank(user.getName())) {
            throw new RuntimeException("Nome não informado.");
        }
        repository.save(user);
    }

    public void delete(User user) {
        repository.delete(user);
    }
}
