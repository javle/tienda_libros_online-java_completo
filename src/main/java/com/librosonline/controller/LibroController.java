package com.librosonline.controller;

import com.librosonline.service.LibroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @GetMapping
    public String listarLibros() {
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String detalleLibro(@PathVariable Long id, Model model) {
        model.addAttribute("libro", libroService.obtenerPorIdDTO(id));
        return "libros/detalle";
    }
}
