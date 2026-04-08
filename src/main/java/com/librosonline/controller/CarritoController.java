package com.librosonline.controller;

import com.librosonline.config.SessionHelper;
import com.librosonline.model.Usuario;
import com.librosonline.service.CarritoService;
import com.librosonline.service.LibroService;
import com.librosonline.service.PedidoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final LibroService libroService;
    private final CarritoService carritoService;
    private final PedidoService pedidoService;

    public CarritoController(LibroService libroService, CarritoService carritoService, PedidoService pedidoService) {
        this.libroService = libroService;
        this.carritoService = carritoService;
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public String verCarrito(Model model, HttpSession session) {
        model.addAttribute("items", carritoService.obtenerCarrito(session));
        model.addAttribute("total", carritoService.calcularTotal(session));
        return "carrito/index";
    }

    @PostMapping("/agregar/{id}")
    public String agregar(@PathVariable Long id,
                          @RequestParam(defaultValue = "1") int cantidad,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        return libroService.buscarPorId(id)
                .map(libro -> {
                    carritoService.agregarLibro(session, libro, Math.max(cantidad, 1));
                    redirectAttributes.addFlashAttribute("mensajeExito", "Libro agregado al carrito.");
                    return "redirect:/libros";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("mensajeError", "Libro no encontrado.");
                    return "redirect:/libros";
                });
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @RequestParam int cantidad, HttpSession session) {
        carritoService.actualizarCantidad(session, id, cantidad);
        return "redirect:/carrito";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        carritoService.eliminar(session, id);
        return "redirect:/carrito";
    }

    @PostMapping("/checkout")
    public String checkout(HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = SessionHelper.getUsuario(session);
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "Debes iniciar sesión para finalizar tu compra.");
            return "redirect:/login";
        }
        try {
            var pedido = pedidoService.crearPedidoDesdeCarrito(usuario, session);
            redirectAttributes.addFlashAttribute("mensajeExito", "Pedido #" + pedido.getId() + " generado correctamente.");
            return "redirect:/pedidos";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/carrito";
        }
    }
}
