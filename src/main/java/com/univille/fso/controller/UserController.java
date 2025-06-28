package com.univille.fso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.univille.fso.entity.User;
import com.univille.fso.repository.UserRoleRepository;
import com.univille.fso.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserService service;

    @Autowired
    private UserRoleRepository roleRepository;

    @GetMapping
    public ModelAndView users() {
        var mv = new ModelAndView("users");
        mv.addObject("list", service.findAll());
        return mv;
    }

    @GetMapping
    @RequestMapping("/add")
    public ModelAndView add() {
        var mv = new ModelAndView("addUser");
        mv.addObject("user", new User());
        return mv;
    }

    @GetMapping
    @RequestMapping("/profile")
    public ModelAndView profile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedUsername = authentication.getName();
        ModelAndView mv = new ModelAndView("updateUser");
        var opt = service.findByUsername(loggedUsername);
        if (opt.isPresent()) {
            mv.addObject("user", opt.get());
            return mv;
        }
        return new ModelAndView("redirect:/game");
    }

    @GetMapping("/{id}/delete")
    public ModelAndView delete(@PathVariable long id) {
        ModelAndView mv = new ModelAndView();
        var opt = service.findById(id);
        if(opt.isPresent()) {
            try {
                service.delete(opt.get());
                mv.setViewName("redirect:/");
            } catch(DataIntegrityViolationException e) {
                mv.addObject("error", "Não é possível excluir este usuário porque ele está sendo utilizada em outros registros.");
                mv.addObject("user", opt.get());
                mv.setViewName("updateUser");
            }
        } else {
            mv.addObject("error", "Usuário não encontrado.");
            mv.setViewName("updateUser");
        }
        return mv;
    }

    @PostMapping
    @RequestMapping("/save")
    public ModelAndView insert(@ModelAttribute("user") User user) {
        try {
            if(!service.isValidEmail(user.getEmail())) {
                throw new IllegalArgumentException("Formato de email inválido.");
            }

            if(!service.isValidPassword(user.getPassword())) {
                throw new IllegalArgumentException("Senha deve conter letras, números e ter no mínimo 8 caracteres.");
            }

            var roleUser = roleRepository.findAll().stream().filter(userRole -> userRole.getCode().equals("ROLE_USER")).findFirst();
            user.setRole(roleUser.get());
            user.setActive(true);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            service.save(user);
            return new ModelAndView("redirect:/");
        } catch(IllegalArgumentException e) {
            var mv = new ModelAndView("addUser");
            mv.addObject("user", user);
            mv.addObject("error", e.getMessage());
            return mv;
        }
    }

    @PostMapping
    @RequestMapping("/update")
    public ModelAndView update(@ModelAttribute("user") User user) {
        try {
            if(!service.isValidEmail(user.getEmail())) {
                throw new IllegalArgumentException("Formato de email inválido.");
            }

            if(!service.isValidPassword(user.getPassword())) {
                throw new IllegalArgumentException("Senha deve conter letras, números e ter no mínimo 8 caracteres.");
            }

            var roleUser = roleRepository.findAll().stream().filter(userRole -> userRole.getCode().equals("ROLE_USER")).findFirst();
            user.setRole(roleUser.get());
            user.setActive(true);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            service.save(user);
            return new ModelAndView("redirect:/game");
        } catch(IllegalArgumentException e) {
            var mv = new ModelAndView("updateUser");
            mv.addObject("user", user);
            mv.addObject("error", e.getMessage());
            return mv;
        }
    }
}
