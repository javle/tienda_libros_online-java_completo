package com.librosonline.controller;

import com.librosonline.config.SessionHelper;
import com.librosonline.model.Usuario;
import com.librosonline.service.PedidoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public String listarPedidos(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = SessionHelper.getUsuario(session);
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "Debes iniciar sesión para ver tus pedidos.");
            return "redirect:/login";
        }
        model.addAttribute("pedidos", pedidoService.listarPorUsuario(usuario));
        return "pedidos/lista";
    }
}
