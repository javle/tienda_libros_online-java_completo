package com.librosonline.controller;

import com.librosonline.model.Usuario;
import com.librosonline.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.librosonline.dto.RegistroDTO;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("registroForm", new RegistroDTO());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@Valid @ModelAttribute("registroForm") RegistroDTO registroDTO,
                                   BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("mensajeError", "Por favor, verifica que todos los campos sean correctos.");
            return "registro";
        }

        if (usuarioService.registrar(registroDTO)) {
            redirectAttributes.addFlashAttribute("mensajeExito", "Registro exitoso. Ahora puedes iniciar sesión.");
            return "redirect:/login";
        }

        model.addAttribute("mensajeError", "El usuario o correo ya existen.");
        return "registro";
    }

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("loginForm", new Usuario());
        return "login";
    }


}