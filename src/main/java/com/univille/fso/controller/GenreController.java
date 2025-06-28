package com.univille.fso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.univille.fso.entity.Genre;
import com.univille.fso.service.GenreService;

@Controller
@RequestMapping("/genre")
public class GenreController {

    @Autowired
    private GenreService service;

    @GetMapping
    public ModelAndView genres() {
        var mv = new ModelAndView("genres");
        mv.addObject("list", service.findAll());
        return mv;
    }

    @GetMapping
    @RequestMapping("/add")
    public ModelAndView add() {
        var mv = new ModelAndView("addGenre");
        mv.addObject("genre", new Genre());
        return mv;
    }

    @GetMapping
    @RequestMapping("/{id}")
    public ModelAndView update(@PathVariable long id) {
        var mv = new ModelAndView("updateGenre");
        var opt = service.findById(id);
        if(opt.isPresent()) {
            mv.addObject("genre", opt.get());
            return mv;
        }
        return new ModelAndView("redirect:/genre");
    }

    @GetMapping("/{id}/delete")
    public ModelAndView delete(@PathVariable long id) {
        ModelAndView mv = new ModelAndView();
        var opt = service.findById(id);
        if(opt.isPresent()) {
            try {
                service.delete(opt.get());
                mv.setViewName("redirect:/genre");
            } catch(DataIntegrityViolationException e) {
                mv.addObject("error", "Não é possível excluir este Gênero porque ele está sendo utilizada em outros registros.");
                mv.addObject("genre", opt.get());
                mv.setViewName("updateGenre");
            }
        } else {
            mv.addObject("error", "Gênero não encontrado.");
            mv.setViewName("updateGenre");
        }
        return mv;
    }

    @PostMapping
    @RequestMapping("/save")
    public ModelAndView insert(@ModelAttribute("genre") Genre genre) {
        try {
            service.save(genre);
            return new ModelAndView("redirect:/genre");
        } catch(Exception e) {
            var mv = new ModelAndView("addGenre");
            mv.addObject("genre", genre);
            mv.addObject("error", e.getMessage());
            return mv;
        }
    }
}
