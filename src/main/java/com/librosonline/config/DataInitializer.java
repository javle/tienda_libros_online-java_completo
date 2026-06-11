package com.librosonline.config;

import com.librosonline.service.LibroService;
import com.librosonline.service.UsuarioService;
import com.librosonline.repository.LibroRepository;
import com.librosonline.repository.ResenaRepository;
import com.librosonline.model.Resena;
import com.librosonline.model.Libro;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UsuarioService usuarioService, LibroService libroService, LibroRepository libroRepository, ResenaRepository resenaRepository) {
        return args -> {
            usuarioService.crearAdminSiNoExiste();
            libroService.crearLibrosIniciales();

            if (resenaRepository.count() == 0) {
                List<Libro> libros = libroRepository.findAll();
                for (Libro libro : libros) {
                    if (libro.getTitulo().contains("Clean") || libro.getTitulo().contains("Data") || libro.getTitulo().contains("Design")) {
                        resenaRepository.save(new Resena(libro, "Carlos R.", 5, "¡Excelente libro! Totalmente recomendado para mejorar tus habilidades."));
                        resenaRepository.save(new Resena(libro, "Ana Gómez", 4, "Muy buen contenido, aunque el primer capítulo es un poco lento."));
                    } else if (libro.getTitulo().contains("Java") || libro.getTitulo().contains("Spring")) {
                        resenaRepository.save(new Resena(libro, "David Torres", 5, "Fundamental para cualquier desarrollador backend moderno."));
                        resenaRepository.save(new Resena(libro, "Laura M.", 5, "Llegó rápido y en perfectas condiciones. El contenido vale oro."));
                    } else {
                        resenaRepository.save(new Resena(libro, "Usuario Anónimo", 4, "Buena lectura, me sirvió bastante para repasar conceptos clave."));
                    }
                }
            }
        };
    }
}
