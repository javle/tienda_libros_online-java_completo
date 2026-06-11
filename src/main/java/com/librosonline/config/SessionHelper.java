package com.librosonline.config;

import com.librosonline.model.Rol;
import com.librosonline.model.Usuario;
import com.librosonline.security.CustomUserDetails;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SessionHelper {

    public static final String USUARIO_SESSION_KEY = "usuarioLogueado";

    private SessionHelper() {
    }

    public static Usuario getUsuario(HttpSession session) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) auth.getPrincipal()).getUsuario();
        }
        return null;
    }

    public static boolean isAdmin(HttpSession session) {
        Usuario usuario = getUsuario(session);
        return usuario != null && usuario.getRol() == Rol.ADMIN;
    }
}
