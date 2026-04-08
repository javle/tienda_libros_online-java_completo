package com.librosonline.config;

import com.librosonline.service.LibroService;
import com.librosonline.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UsuarioService usuarioService, LibroService libroService) {
        return args -> {
            usuarioService.crearAdminSiNoExiste();
            libroService.crearLibrosIniciales();
        };
    }
}
