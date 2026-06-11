package com.librosonline.controller;

import com.librosonline.dto.LibroDTO;
import com.librosonline.service.LibroService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpSession;
import com.librosonline.config.SessionHelper;
import com.librosonline.model.Usuario;
import com.librosonline.model.Resena;
import com.librosonline.repository.ResenaRepository;

import java.util.List;

@Controller
public class HomeController {

    private final LibroService libroService;
    private final ResenaRepository resenaRepository;

    public HomeController(LibroService libroService, ResenaRepository resenaRepository) {
        this.libroService = libroService;
        this.resenaRepository = resenaRepository;
    }

    @GetMapping("/")
    public String landing(Model model) {
        return "home";
    }

    @GetMapping("/catalogo")
    public String catalogo(@RequestParam(required = false) String categoria,
                           @RequestParam(required = false) String q,
                           @RequestParam(defaultValue = "0") int page,
                           Model model) {
        
        Pageable pageable = PageRequest.of(page, 12);
        Page<LibroDTO> librosPage = libroService.listarActivos(q, categoria, pageable);
        List<String> categorias = libroService.obtenerCategorias();

        model.addAttribute("librosPage", librosPage);
        model.addAttribute("libros", librosPage.getContent());
        model.addAttribute("categorias", categorias);
        model.addAttribute("categoriaSeleccionada", categoria == null ? "" : categoria);
        model.addAttribute("q", q == null ? "" : q);

        return "catalogo";
    }

    @GetMapping("/api/recomendaciones")
    @ResponseBody
    public List<LibroDTO> obtenerRecomendaciones(@RequestParam String categoria) {
        return libroService.listarActivos(null, categoria, PageRequest.of(0, 3)).getContent();
    }

    @GetMapping("/catalogo/{id}")
    public String detalleLibro(@PathVariable Long id, Model model) {
        return libroService.buscarPorId(id).map(libro -> {
            List<Resena> resenas = resenaRepository.findByLibroIdOrderByFechaCreacionDesc(id);
            double promedio = resenas.isEmpty() ? 0.0 : resenas.stream().mapToInt(Resena::getEstrellas).average().orElse(0.0);
            
            model.addAttribute("libro", libro);
            model.addAttribute("resenas", resenas);
            model.addAttribute("promedio", promedio);
            return "detalle-libro";
        }).orElse("redirect:/catalogo");
    }

    @PostMapping("/catalogo/{id}/resenas")
    public String dejarResena(@PathVariable Long id, @RequestParam int estrellas, @RequestParam String comentario, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = SessionHelper.getUsuario(session);
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "Debes iniciar sesión para dejar una reseña.");
            return "redirect:/catalogo/" + id;
        }

        libroService.buscarPorId(id).ifPresent(libro -> {
            Resena resena = new Resena(libro, usuario.getNombre(), estrellas, comentario);
            resenaRepository.save(resena);
            redirectAttributes.addFlashAttribute("mensajeExito", "¡Gracias por tu opinión!");
        });

        return "redirect:/catalogo/" + id;
    }
}
