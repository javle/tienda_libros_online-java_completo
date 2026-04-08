package com.librosonline.controller;

import com.librosonline.config.SessionHelper;
import com.librosonline.model.Libro;
import com.librosonline.service.LibroService;
import com.librosonline.service.PedidoService;
import com.librosonline.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final LibroService libroService;
    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;

    public AdminController(LibroService libroService, PedidoService pedidoService, UsuarioService usuarioService) {
        this.libroService = libroService;
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String panel(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!SessionHelper.isAdmin(session)) {
            redirectAttributes.addFlashAttribute("mensajeError", "Acceso restringido al panel administrador.");
            return "redirect:/login";
        }
        model.addAttribute("totalLibros", libroService.totalLibros());
        model.addAttribute("totalUsuarios", usuarioService.totalUsuarios());
        model.addAttribute("totalPedidos", pedidoService.totalPedidos());
        model.addAttribute("libros", libroService.listarTodos());
        model.addAttribute("pedidos", pedidoService.listarTodos());
        return "admin/panel";
    }

    @GetMapping("/libros/nuevo")
    public String nuevoLibro(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!SessionHelper.isAdmin(session)) {
            redirectAttributes.addFlashAttribute("mensajeError", "Acceso restringido al panel administrador.");
            return "redirect:/login";
        }
        model.addAttribute("libro", new Libro());
        return "admin/formulario-libro";
    }

    @GetMapping("/libros/editar/{id}")
    public String editarLibro(@PathVariable Long id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!SessionHelper.isAdmin(session)) {
            redirectAttributes.addFlashAttribute("mensajeError", "Acceso restringido al panel administrador.");
            return "redirect:/login";
        }
        Libro libro = libroService.buscarPorId(id).orElse(new Libro());
        model.addAttribute("libro", libro);
        return "admin/formulario-libro";
    }

    @PostMapping("/libros/guardar")
    public String guardarLibro(@ModelAttribute Libro libro, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!SessionHelper.isAdmin(session)) {
            redirectAttributes.addFlashAttribute("mensajeError", "Acceso restringido al panel administrador.");
            return "redirect:/login";
        }
        libroService.guardar(libro);
        redirectAttributes.addFlashAttribute("mensajeExito", "Libro guardado correctamente.");
        return "redirect:/admin";
    }

    @PostMapping("/libros/eliminar/{id}")
    public String eliminarLibro(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!SessionHelper.isAdmin(session)) {
            redirectAttributes.addFlashAttribute("mensajeError", "Acceso restringido al panel administrador.");
            return "redirect:/login";
        }
        libroService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensajeExito", "Libro desactivado correctamente.");
        return "redirect:/admin";
    }
}
