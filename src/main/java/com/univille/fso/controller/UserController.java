package com.univille.fso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.univille.fso.entity.User;
import com.univille.fso.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserService service;

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
    @RequestMapping("/{id}")
    public ModelAndView update(@PathVariable long id) {
        var mv = new ModelAndView("updateUser");
        var opt = service.findById(id);
        if(opt.isPresent()) {
            mv.addObject("user", opt.get());
            return mv;
        }
        return new ModelAndView("redirect:/user");
    }

    @DeleteMapping("/{id}/delete")
    public ModelAndView delete(@PathVariable long id) {
        ModelAndView mv = new ModelAndView();
        var opt = service.findById(id);
        if(opt.isPresent()) {
            try {
                service.delete(opt.get());
                mv.setViewName("redirect:/game");
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
            user.setActive(true);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            service.save(user);
            return new ModelAndView("redirect:/");
        } catch(Exception e) {
            var mv = new ModelAndView("addUser");
            mv.addObject("user", user);
            mv.addObject("error", e.getMessage());
            return mv;
        }
    }
}
