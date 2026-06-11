package com.librosonline.controller;

import com.librosonline.model.Rol;
import com.librosonline.model.Usuario;
import com.librosonline.security.CustomUserDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private CustomUserDetails createMockUser(Rol rol) {
        Usuario usuario = new Usuario();
        usuario.setUsuario("testuser");
        usuario.setClave("testpass");
        usuario.setRol(rol);
        return new CustomUserDetails(usuario);
    }

    // --- 1. Protección del Panel de Administración (/admin/**) ---

    @Test
    void testAdminAccess_AnonymousUser_ShouldRedirectToLogin() throws Exception {
        // Al intentar entrar a una URL protegida sin login, Spring Security redirige al login (302)
        mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testAdminAccess_ClienteRole_ShouldBeForbidden() throws Exception {
        // Un usuario autenticado con rol diferente a ADMIN recibe 403 Forbidden por Spring Security
        mockMvc.perform(get("/admin").with(user(createMockUser(Rol.CLIENTE))))
                .andExpect(status().isForbidden());
    }

    @Test
    void testAdminAccess_AdminRole_ShouldBeOk() throws Exception {
        // Un usuario con rol ADMIN debe tener acceso correcto
        mockMvc.perform(get("/admin").with(user(createMockUser(Rol.ADMIN))))
                .andExpect(status().isOk());
    }

    // --- 2. Protección del Historial de Pedidos (/pedidos) ---

    @Test
    void testPedidosAccess_AnonymousUser_ShouldRedirectToLogin() throws Exception {
        // Historial privado protegido contra usuarios anónimos
        mockMvc.perform(get("/pedidos"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void testPedidosAccess_AuthenticatedUser_ShouldBeOk() throws Exception {
        // Historial accesible para cualquier usuario autenticado
        mockMvc.perform(get("/pedidos").with(user(createMockUser(Rol.CLIENTE))))
                .andExpect(status().isOk());
    }
}
