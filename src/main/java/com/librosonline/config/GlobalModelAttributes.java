package com.librosonline.config;

import com.librosonline.service.CarritoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalModelAttributes {

    private final CarritoService carritoService;

    public GlobalModelAttributes(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @ModelAttribute
    public void addGlobalAttributes(Model model, HttpSession session) {
        model.addAttribute("usuarioSesion", SessionHelper.getUsuario(session));
        model.addAttribute("esAdmin", SessionHelper.isAdmin(session));
        model.addAttribute("cartCount", carritoService.cantidadItems(session));
    }
}
