package com.univille.fso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.univille.fso.service.GameService;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    private GameService service;

    @GetMapping
    public ModelAndView home() {
        var mv = new ModelAndView("home");
        mv.addObject("list", service.findAll());
        return mv;
    }
}
