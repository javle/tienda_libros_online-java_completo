package com.librosonline.service;

import com.librosonline.model.Libro;
import com.librosonline.repository.LibroRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    private final LibroRepository libroRepository;

    public LibroService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public List<Libro> listarActivos(String q) {
        if (q == null || q.isBlank()) {
            return libroRepository.findByActivoTrueOrderByFechaActualizacionDesc();
        }
        return libroRepository.findByActivoTrueAndTituloContainingIgnoreCaseOrActivoTrueAndAutorContainingIgnoreCaseOrderByFechaActualizacionDesc(q, q);
    }

    public List<Libro> listarTodos() {
        return libroRepository.findAll();
    }

    public Optional<Libro> buscarPorId(Long id) {
        return libroRepository.findById(id);
    }

    public Libro guardar(Libro libro) {
        return libroRepository.save(libro);
    }

    public void eliminar(Long id) {
        libroRepository.findById(id).ifPresent(libro -> {
            libro.setActivo(false);
            libroRepository.save(libro);
        });
    }

    public long totalLibros() {
        return libroRepository.count();
    }

    public void crearLibrosIniciales() {
        if (libroRepository.count() > 0) {
            return;
        }

        guardar(libro("Clean Code", "Robert C. Martin", "Programación", "9780132350884", "Buenas prácticas de desarrollo y código limpio.", new BigDecimal("89000"), 10));
        guardar(libro("Spring in Action", "Craig Walls", "Backend", "9781617294945", "Guía práctica para desarrollar aplicaciones con Spring.", new BigDecimal("120000"), 8));
        guardar(libro("Effective Java", "Joshua Bloch", "Java", "9780134685991", "Recomendaciones esenciales para escribir mejor Java.", new BigDecimal("99000"), 7));
        guardar(libro("Diseño de Bases de Datos", "Carlos Coronel", "Bases de datos", "9786071502865", "Fundamentos para modelado y persistencia de datos.", new BigDecimal("75000"), 12));
    }

    private Libro libro(String titulo, String autor, String categoria, String isbn, String descripcion, BigDecimal precio, int stock) {
        Libro libro = new Libro();
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setCategoria(categoria);
        libro.setIsbn(isbn);
        libro.setDescripcion(descripcion);
        libro.setPrecio(precio);
        libro.setStock(stock);
        libro.setActivo(true);
        return libro;
    }
}
