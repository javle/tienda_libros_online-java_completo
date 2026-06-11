package com.librosonline.controller;

import com.librosonline.config.SessionHelper;
import com.librosonline.model.Usuario;
import com.librosonline.service.PedidoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.PageRequest;
import com.librosonline.model.Pedido;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public String listarPedidos(
            @RequestParam(defaultValue = "0") int page,
            HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = SessionHelper.getUsuario(session);
        if (usuario == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "Debes iniciar sesión para ver tus pedidos.");
            return "redirect:/login";
        }
        var pedidosPage = pedidoService.listarPedidosEntityPorUsuario(usuario, PageRequest.of(page, 12));
        model.addAttribute("pedidosPage", pedidosPage);
        model.addAttribute("pedidos", pedidosPage.getContent());
        return "pedidos/lista";
    }

    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Usuario usuario = SessionHelper.getUsuario(session);
        if (usuario == null) {
            return "redirect:/login";
        }
        
        try {
            Pedido pedido = pedidoService.obtenerPedidoValidado(id, usuario, SessionHelper.isAdmin(session));
            model.addAttribute("pedido", pedido);
            return "pedidos/detalle";
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/pedidos";
        }
    }
}
