package com.librosonline.controller;

import com.librosonline.config.SessionHelper;
import com.librosonline.model.DireccionEnvio;
import com.librosonline.model.Usuario;
import com.librosonline.service.CarritoService;
import com.librosonline.service.PedidoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final CarritoService carritoService;
    private final PedidoService pedidoService;

    public CheckoutController(CarritoService carritoService, PedidoService pedidoService) {
        this.carritoService = carritoService;
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public String vistaCheckout(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = SessionHelper.getUsuario(session);
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "Debes iniciar sesión para finalizar tu compra.");
            return "redirect:/login";
        }

        if (carritoService.cantidadItems(session) == 0) {
            redirectAttributes.addFlashAttribute("mensajeError", "Tu carrito está vacío.");
            return "redirect:/carrito";
        }

        model.addAttribute("items", carritoService.obtenerCarrito(session));
        model.addAttribute("total", carritoService.calcularTotal(session));
        model.addAttribute("usuario", usuario);
        return "checkout";
    }

    @PostMapping("/procesar")
    public String procesarPago(
            @org.springframework.web.bind.annotation.RequestParam String direccion,
            @org.springframework.web.bind.annotation.RequestParam String ciudad,
            @org.springframework.web.bind.annotation.RequestParam String codigoPostal,
            @org.springframework.web.bind.annotation.RequestParam String telefono,
            HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuario = SessionHelper.getUsuario(session);
        if (usuario == null) {
            return "redirect:/login";
        }

        if (carritoService.cantidadItems(session) == 0) {
            return "redirect:/carrito";
        }

        try {
            DireccionEnvio direccionEnvio = new DireccionEnvio(direccion, ciudad, codigoPostal, telefono);
            var pedido = pedidoService.crearPedidoDesdeCarrito(usuario, session, direccionEnvio);
            return "redirect:/checkout/confirmacion/" + pedido.getId();
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/carrito";
        }
    }

    @GetMapping("/confirmacion/{id}")
    public String confirmacion(@PathVariable Long id, Model model, HttpSession session) {
        Usuario usuario = SessionHelper.getUsuario(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("pedidoId", id);
        return "pedidos/confirmacion";
    }
}
