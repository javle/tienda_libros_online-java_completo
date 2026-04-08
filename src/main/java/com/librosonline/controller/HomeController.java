package com.librosonline.controller;

import com.librosonline.model.Libro;
import com.librosonline.service.LibroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

@Controller
public class HomeController {

    private final LibroService libroService;

    public HomeController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping("/")
    public String home(@RequestParam(required = false) String categoria, Model model) {
        List<Libro> todosLosLibros = libroService.listarActivos(null);

        List<String> categorias = todosLosLibros.stream()
                .map(Libro::getCategoria)
                .filter(Objects::nonNull)
                .filter(cat -> !cat.isBlank())
                .distinct()
                .sorted()
                .toList();

        List<Libro> libros = (categoria == null || categoria.isBlank())
                ? todosLosLibros
                : todosLosLibros.stream()
                    .filter(libro -> libro.getCategoria() != null
                            && libro.getCategoria().equalsIgnoreCase(categoria))
                    .toList();

        model.addAttribute("libros", libros);
        model.addAttribute("categorias", categorias);
        model.addAttribute("categoriaSeleccionada", categoria == null ? "" : categoria);

        return "home";
    }
}
