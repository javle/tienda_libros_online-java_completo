package com.librosonline.service;

import com.librosonline.model.Rol;
import com.librosonline.model.Usuario;
import com.librosonline.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public boolean registrar(Usuario usuario) {
        if (usuarioRepository.existsByUsuario(usuario.getUsuario()) || usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            return false;
        }
        if (usuario.getRol() == null) {
            usuario.setRol(Rol.CLIENTE);
        }
        usuarioRepository.save(usuario);
        return true;
    }

    public Optional<Usuario> iniciarSesion(String usuario, String clave) {
        return usuarioRepository.findByUsuario(usuario)
                .filter(u -> u.getClave().equals(clave));
    }

    public Optional<Usuario> buscarPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    public long totalUsuarios() {
        return usuarioRepository.count();
    }

    public void crearAdminSiNoExiste() {
        if (usuarioRepository.findByUsuario("admin").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setNombre("Administrador");
            admin.setUsuario("admin");
            admin.setCorreo("admin@librosonline.com");
            admin.setClave("admin123");
            admin.setRol(Rol.ADMIN);
            usuarioRepository.save(admin);
        }
    }
}
