package com.librosonline.controller;

import com.librosonline.service.LibroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final LibroService libroService;

    public HomeController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("librosDestacados", libroService.listarActivos(null).stream().limit(4).toList());
        return "home";
    }
}
