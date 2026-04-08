package com.librosonline.controller;

import com.librosonline.config.SessionHelper;
import com.librosonline.model.Rol;
import com.librosonline.model.Usuario;
import com.librosonline.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("registroForm", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("registroForm") Usuario usuario,
                                   RedirectAttributes redirectAttributes) {
        usuario.setRol(Rol.CLIENTE);

        if (usuarioService.registrar(usuario)) {
            redirectAttributes.addFlashAttribute("mensajeExito", "Registro exitoso. Ahora puedes iniciar sesión.");
            return "redirect:/login";
        }

        redirectAttributes.addFlashAttribute("mensajeError", "El usuario o correo ya existen.");
        return "redirect:/registro";
    }

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("loginForm", new Usuario());
        return "login";
    }

    @PostMapping("/login")
    public String iniciarSesion(@ModelAttribute("loginForm") Usuario usuario,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        return usuarioService.iniciarSesion(usuario.getUsuario(), usuario.getClave())
                .map(u -> {
                    session.setAttribute(SessionHelper.USUARIO_SESSION_KEY, u);
                    redirectAttributes.addFlashAttribute("mensajeExito", "Bienvenido, " + u.getNombre() + ".");

                    if (u.getRol() == Rol.ADMIN) {
                        return "redirect:/admin";
                    }

                    return "redirect:/";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("mensajeError", "Usuario o clave incorrectos.");
                    return "redirect:/login";
                });
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("mensajeExito", "Sesión cerrada correctamente.");
        return "redirect:/";
    }
}