package com.librosonline.controller;

import com.librosonline.config.SessionHelper;
import com.librosonline.dto.LibroDTO;
import com.librosonline.model.PedidoEstado;
import com.librosonline.service.LibroService;
import com.librosonline.service.PedidoService;
import com.librosonline.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.PageRequest;

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
    public String panel(
            @RequestParam(defaultValue = "0") int pageLibros,
            @RequestParam(defaultValue = "0") int pagePedidos,
            HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!SessionHelper.isAdmin(session)) {
            redirectAttributes.addFlashAttribute("mensajeError", "Acceso restringido al panel administrador.");
            return "redirect:/login";
        }
        model.addAttribute("totalLibros", libroService.totalLibros());
        model.addAttribute("totalUsuarios", usuarioService.totalUsuarios());
        model.addAttribute("totalPedidos", pedidoService.totalPedidos());
        
        var librosPage = libroService.listarTodos(PageRequest.of(pageLibros, 20));
        var pedidosPage = pedidoService.listarTodos(PageRequest.of(pagePedidos, 20));

        model.addAttribute("librosPage", librosPage);
        model.addAttribute("libros", librosPage.getContent());
        model.addAttribute("pedidosPage", pedidosPage);
        model.addAttribute("pedidos", pedidosPage.getContent());
        
        return "admin/panel";
    }

    @GetMapping("/libros/nuevo")
    public String nuevoLibro(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!SessionHelper.isAdmin(session)) {
            redirectAttributes.addFlashAttribute("mensajeError", "Acceso restringido al panel administrador.");
            return "redirect:/login";
        }
        model.addAttribute("libro", new LibroDTO());
        return "admin/formulario-libro";
    }

    @GetMapping("/libros/editar/{id}")
    public String editarLibro(@PathVariable Long id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!SessionHelper.isAdmin(session)) {
            redirectAttributes.addFlashAttribute("mensajeError", "Acceso restringido al panel administrador.");
            return "redirect:/login";
        }
        LibroDTO libroDTO = libroService.obtenerPorIdDTO(id);
        model.addAttribute("libro", libroDTO);
        return "admin/formulario-libro";
    }

    @PostMapping("/libros/guardar")
    public String guardarLibro(@Valid @ModelAttribute("libro") LibroDTO libroDTO, BindingResult bindingResult, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!SessionHelper.isAdmin(session)) {
            redirectAttributes.addFlashAttribute("mensajeError", "Acceso restringido al panel administrador.");
            return "redirect:/login";
        }
        
        if (bindingResult.hasErrors()) {
            return "admin/formulario-libro";
        }
        
        libroService.guardarDTO(libroDTO);
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

    @PostMapping("/pedidos/estado/{id}")
    public String cambiarEstadoPedido(@PathVariable Long id, @RequestParam PedidoEstado estado, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!SessionHelper.isAdmin(session)) {
            redirectAttributes.addFlashAttribute("mensajeError", "Acceso restringido al panel administrador.");
            return "redirect:/login";
        }
        pedidoService.actualizarEstado(id, estado);
        redirectAttributes.addFlashAttribute("mensajeExito", "Estado del pedido actualizado.");
        return "redirect:/admin";
    }

    @GetMapping("/usuarios")
    public String listarUsuarios(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (!SessionHelper.isAdmin(session)) {
            redirectAttributes.addFlashAttribute("mensajeError", "Acceso restringido al panel administrador.");
            return "redirect:/login";
        }
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "admin/usuarios";
    }
}
