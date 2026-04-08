package com.librosonline.config;

import com.librosonline.model.Rol;
import com.librosonline.model.Usuario;
import jakarta.servlet.http.HttpSession;

public final class SessionHelper {

    public static final String USUARIO_SESSION_KEY = "usuarioLogueado";

    private SessionHelper() {
    }

    public static Usuario getUsuario(HttpSession session) {
        Object obj = session.getAttribute(USUARIO_SESSION_KEY);
        return obj instanceof Usuario ? (Usuario) obj : null;
    }

    public static boolean isAdmin(HttpSession session) {
        Usuario usuario = getUsuario(session);
        return usuario != null && usuario.getRol() == Rol.ADMIN;
    }
}
