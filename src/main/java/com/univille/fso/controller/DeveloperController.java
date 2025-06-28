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

import com.univille.fso.entity.Developer;
import com.univille.fso.service.DeveloperService;

@Controller
@RequestMapping("/developer")
public class DeveloperController {

    @Autowired
    private DeveloperService service;

    @GetMapping
    public ModelAndView developers() {
        var mv = new ModelAndView("developers");
        mv.addObject("list", service.findAll());
        return mv;
    }

    @GetMapping
    @RequestMapping("/add")
    public ModelAndView add() {
        var mv = new ModelAndView("addDeveloper");
        mv.addObject("developer", new Developer());
        return mv;
    }

    @GetMapping
    @RequestMapping("/{id}")
    public ModelAndView update(@PathVariable long id) {
        var mv = new ModelAndView("updateDeveloper");
        var opt = service.findById(id);
        if(opt.isPresent()) {
            mv.addObject("developer", opt.get());
            return mv;
        }
        return new ModelAndView("redirect:/developer");
    }

    @GetMapping("/{id}/delete")
    public ModelAndView delete(@PathVariable long id) {
        ModelAndView mv = new ModelAndView();
        var opt = service.findById(id);
        if(opt.isPresent()) {
            try {
                service.delete(opt.get());
                mv.setViewName("redirect:/developer");
            } catch(DataIntegrityViolationException e) {
                mv.addObject("error", "Não é possível excluir esta desenvolvedora porque ela está sendo utilizada em outros registros.");
                mv.addObject("developer", opt.get());
                mv.setViewName("updateDeveloper");
            }
        } else {
            mv.addObject("error", "Desenvolvedora não encontrada.");
            mv.setViewName("updateDeveloper");
        }
        return mv;
    }

    @PostMapping
    @RequestMapping("/save")
    public ModelAndView insert(@ModelAttribute("developer") Developer developer) {
        try {
            service.save(developer);
            return new ModelAndView("redirect:/developer");
        } catch(Exception e) {
            var mv = new ModelAndView("addDeveloper");
            mv.addObject("developer", developer);
            mv.addObject("error", e.getMessage());
            return mv;
        }
    }
}
