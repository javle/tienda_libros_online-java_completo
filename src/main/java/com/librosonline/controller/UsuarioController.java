package com.librosonline.controller;

import com.librosonline.model.Usuario;
import com.librosonline.security.CustomUserDetails;
import com.librosonline.service.UsuarioService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/perfil")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String verPerfil(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        Usuario usuario = usuarioService.buscarPorId(userDetails.getUsuario().getId()).orElseThrow();
        model.addAttribute("usuario", usuario);
        return "usuario/perfil";
    }

    @PostMapping("/actualizar")
    public String actualizarPerfil(@ModelAttribute("usuario") Usuario usuarioActualizado, 
                                   @AuthenticationPrincipal CustomUserDetails userDetails,
                                   RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        usuarioService.actualizarPerfil(userDetails.getUsuario().getId(), usuarioActualizado);
        redirectAttributes.addFlashAttribute("mensajeExito", "Perfil actualizado correctamente.");
        return "redirect:/perfil";
    }
}
