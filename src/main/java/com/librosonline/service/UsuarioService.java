package com.librosonline.service;

import com.librosonline.model.Rol;
import com.librosonline.model.Usuario;
import com.librosonline.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;
import com.librosonline.dto.RegistroDTO;
import com.librosonline.dto.UsuarioDTO;
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public boolean registrar(RegistroDTO registroDTO) {
        if (usuarioRepository.existsByUsuario(registroDTO.getUsuario()) || usuarioRepository.existsByCorreo(registroDTO.getCorreo())) {
            return false;
        }
        Usuario usuario = new Usuario();
        usuario.setNombre(registroDTO.getNombre());
        usuario.setUsuario(registroDTO.getUsuario());
        usuario.setCorreo(registroDTO.getCorreo());
        usuario.setRol(Rol.CLIENTE);
        usuario.setClave(passwordEncoder.encode(registroDTO.getClave()));
        
        usuarioRepository.save(usuario);
        return true;
    }



    public void actualizarPerfil(Long id, Usuario usuarioActualizado) {
        usuarioRepository.findById(id).ifPresent(u -> {
            u.setNombre(usuarioActualizado.getNombre());
            u.setCorreo(usuarioActualizado.getCorreo());
            if (usuarioActualizado.getClave() != null && !usuarioActualizado.getClave().isBlank()) {
                u.setClave(passwordEncoder.encode(usuarioActualizado.getClave()));
            }
            usuarioRepository.save(u);
        });
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
            admin.setClave(passwordEncoder.encode("admin123"));
            admin.setRol(Rol.ADMIN);
            usuarioRepository.save(admin);
        }
    }

    public java.util.List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream().map(u -> {
            UsuarioDTO dto = new UsuarioDTO();
            dto.setId(u.getId());
            dto.setNombre(u.getNombre());
            dto.setUsuario(u.getUsuario());
            dto.setCorreo(u.getCorreo());
            dto.setRol(u.getRol());
            return dto;
        }).collect(Collectors.toList());
    }
}
