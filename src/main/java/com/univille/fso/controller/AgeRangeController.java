package com.univille.fso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.univille.fso.entity.AgeRange;
import com.univille.fso.service.AgeRangeService;

@Controller
@RequestMapping("/ageRange")
public class AgeRangeController {

    @Autowired
    private AgeRangeService service;

    @GetMapping
    public ModelAndView ageRanges() {
        var mv = new ModelAndView("ageRanges");
        mv.addObject("list", service.findAll());
        System.out.println(service.findAll());
        return mv;
    }

    @GetMapping
    @RequestMapping("/add")
    public ModelAndView add() {
        var mv = new ModelAndView("addAgeRange");
        mv.addObject("ageRange", new AgeRange());
        return mv;
    }

    @GetMapping
    @RequestMapping("/{id}")
    public ModelAndView update(@PathVariable long id) {
        var mv = new ModelAndView("updateAgeRange");
        var opt = service.findById(id);
        if(opt.isPresent()) {
            mv.addObject("ageRange", opt.get());
            return mv;
        }
        return new ModelAndView("redirect:/ageRange");
    }

    @DeleteMapping("/{id}/delete")
    public ModelAndView delete(@PathVariable long id) {
        ModelAndView mv = new ModelAndView();
        var opt = service.findById(id);
        if(opt.isPresent()) {
            try {
                service.delete(opt.get());
                mv.setViewName("redirect:/ageRange");
            } catch(DataIntegrityViolationException e) {
                mv.addObject("error", "Não é possível excluir esta faixa etária porque ela está sendo utilizada em outros registros.");
                mv.addObject("ageRange", opt.get());
                mv.setViewName("updateAgeRange");
            }
        } else {
            mv.addObject("error", "Faixa etária não encontrada.");
            mv.setViewName("updateAgeRange");
        }
        return mv;
    }

    @PostMapping
    @RequestMapping("/save")
    public ModelAndView insert(@ModelAttribute("ageRange") AgeRange ageRange) {
        try {
            service.save(ageRange);
            return new ModelAndView("redirect:/ageRange");
        } catch(Exception e) {
            var mv = new ModelAndView("addAgeRange");
            mv.addObject("ageRange", ageRange);
            mv.addObject("error", e.getMessage());
            return mv;
        }
    }
}
