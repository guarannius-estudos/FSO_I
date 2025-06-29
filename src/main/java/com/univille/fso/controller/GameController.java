package com.univille.fso.controller;

import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.univille.fso.entity.Game;
import com.univille.fso.service.GameService;

@Controller
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService service;

    @GetMapping
    public ModelAndView home() {
        var mv = new ModelAndView("games");
        mv.addObject("list", service.findAll());
        return mv;
    }

    @GetMapping("/search/{name}")
    public ModelAndView homeByName(@PathVariable String name) {
        var mv = new ModelAndView("searchResults");
        mv.addObject("list", service.findByName(name));
        return mv;
    }

    @GetMapping("/add")
    public ModelAndView add() {
        var mv = new ModelAndView("addGame");
        mv.addObject("game", new Game());
        mv.addObject("developers", service.findAllDevelopers());
        mv.addObject("genres", service.findAllGenres());
        mv.addObject("ageRanges", service.findAllAgeRanges());
        return mv;
    }

    @GetMapping("/{id}")
    public ModelAndView update(@PathVariable long id) {
        var mv = new ModelAndView("updateGame");
        var opt = service.findById(id);
        if(opt.isPresent()) {
            mv.addObject("game", opt.get());
            mv.addObject("developers", service.findAllDevelopers());
            mv.addObject("genres", service.findAllGenres());
            mv.addObject("ageRanges", service.findAllAgeRanges());
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
                mv.setViewName("redirect:/game");
            } catch(DataIntegrityViolationException e) {
                mv.addObject("error", "Não é possível excluir este jogo porque ele está sendo utilizada em outros registros.");
                mv.addObject("game", opt.get());
                mv.setViewName("updateGame");
            }
        } else {
            mv.addObject("error", "Jogo não encontrado.");
            mv.setViewName("games");
        }
        return mv;
    }

    @PostMapping("/save")
    public ModelAndView insert(@ModelAttribute("game") Game game, @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            if(!imageFile.isEmpty()) {
                game.setImage(imageFile.getBytes());
            } else {
                if (game.getId() > 0) {
                    var existingGame = service.findById(game.getId());
                    if(existingGame.isPresent()) {
                        game.setImage(existingGame.get().getImage());
                    }
                } else {
                    ClassPathResource imgFile = new ClassPathResource("static/img/sem-foto.png");
                    game.setImage(Files.readAllBytes(imgFile.getFile().toPath()));
                }
            }

            double valueUSD = service.convertToUSD(game.getValue());
            game.setValueUSD(valueUSD);
            service.save(game);
            return new ModelAndView("redirect:/game");

        } catch(IOException e) {
            var mv = new ModelAndView("addGame");
            mv.addObject("error", "Erro ao processar imagem: " + e.getMessage());
            mv.addObject("game", game);
            mv.addObject("developers", service.findAllDevelopers());
            mv.addObject("genres", service.findAllGenres());
            mv.addObject("ageRanges", service.findAllAgeRanges());
            return mv;
        } catch(Exception e) {
            var mv = new ModelAndView("addGame");
            mv.addObject("game", game);
            mv.addObject("developers", service.findAllDevelopers());
            mv.addObject("genres", service.findAllGenres());
            mv.addObject("ageRanges", service.findAllAgeRanges());
            mv.addObject("error", e.getMessage());
            return mv;
        }
    }

    @GetMapping("/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable long id) {
        var opt = service.findById(id);
        if (opt.isPresent() && opt.get().getImage() != null) {
            byte[] image = opt.get().getImage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }
}
