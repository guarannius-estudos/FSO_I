package com.univille.fso.service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean isValidPassword(String password) {
        String regex = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public Optional<User> findById(long id) {
        return repository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return repository.findByUsername(username);
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
