package com.librosonline.controller;

import com.librosonline.service.LibroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    public String listarLibros(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("libros", libroService.listarActivos(q));
        model.addAttribute("q", q == null ? "" : q);
        return "libros/lista";
    }
}
