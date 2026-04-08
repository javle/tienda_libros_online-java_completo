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
        return libroRepository
                .findByActivoTrueAndTituloContainingIgnoreCaseOrActivoTrueAndAutorContainingIgnoreCaseOrderByFechaActualizacionDesc(q, q);
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

        guardar(crearLibro(
                "Clean Code",
                "Robert C. Martin",
                "Programación",
                "9780132350884",
                "Buenas prácticas para escribir código limpio y mantenible.",
                "https://covers.openlibrary.org/b/isbn/9780132350884-L.jpg",
                new BigDecimal("109000"),
                15
        ));

        guardar(crearLibro(
                "The Pragmatic Programmer",
                "Andrew Hunt, David Thomas",
                "Programación",
                "9780135957059",
                "Uno de los clásicos más recomendados para desarrolladores.",
                "https://covers.openlibrary.org/b/isbn/9780135957059-L.jpg",
                new BigDecimal("115000"),
                12
        ));

        guardar(crearLibro(
                "Effective Java",
                "Joshua Bloch",
                "Java",
                "9780134685991",
                "Buenas prácticas esenciales para desarrollar mejor en Java.",
                "https://covers.openlibrary.org/b/isbn/9780134685991-L.jpg",
                new BigDecimal("119000"),
                10
        ));

        guardar(crearLibro(
                "Head First Design Patterns",
                "Eric Freeman, Elisabeth Freeman",
                "Patrones de diseño",
                "9780596007126",
                "Introducción visual y práctica a patrones de diseño.",
                "https://covers.openlibrary.org/b/isbn/9780596007126-L.jpg",
                new BigDecimal("125000"),
                11
        ));

        guardar(crearLibro(
                "Refactoring",
                "Martin Fowler",
                "Programación",
                "9780134757599",
                "Cómo mejorar el diseño del código existente sin romperlo.",
                "https://covers.openlibrary.org/b/isbn/9780134757599-L.jpg",
                new BigDecimal("129000"),
                9
        ));

        guardar(crearLibro(
                "Design Patterns",
                "Erich Gamma, Richard Helm, Ralph Johnson, John Vlissides",
                "Patrones de diseño",
                "9780201633610",
                "El libro base de patrones orientados a objetos.",
                "https://covers.openlibrary.org/b/isbn/9780201633610-L.jpg",
                new BigDecimal("139000"),
                8
        ));

        guardar(crearLibro(
                "Clean Architecture",
                "Robert C. Martin",
                "Arquitectura",
                "9780134494166",
                "Principios para estructurar software mantenible y escalable.",
                "https://covers.openlibrary.org/b/isbn/9780134494166-L.jpg",
                new BigDecimal("124000"),
                12
        ));

        guardar(crearLibro(
                "Domain-Driven Design",
                "Eric Evans",
                "Arquitectura",
                "9780321125217",
                "Diseño guiado por dominio para sistemas complejos.",
                "https://covers.openlibrary.org/b/isbn/9780321125217-L.jpg",
                new BigDecimal("145000"),
                7
        ));

        guardar(crearLibro(
                "Spring in Action",
                "Craig Walls",
                "Backend",
                "9781617294945",
                "Guía práctica para desarrollar aplicaciones con Spring.",
                "https://covers.openlibrary.org/b/isbn/9781617294945-L.jpg",
                new BigDecimal("118000"),
                14
        ));

        guardar(crearLibro(
                "Java Concurrency in Practice",
                "Brian Goetz",
                "Java",
                "9780321349606",
                "Concurrencia segura y escalable en Java.",
                "https://covers.openlibrary.org/b/isbn/9780321349606-L.jpg",
                new BigDecimal("132000"),
                8
        ));

        guardar(crearLibro(
                "Head First Java",
                "Kathy Sierra, Bert Bates",
                "Java",
                "9781491910771",
                "Libro muy popular para aprender Java desde cero.",
                "https://covers.openlibrary.org/b/isbn/9781491910771-L.jpg",
                new BigDecimal("114000"),
                13
        ));

        guardar(crearLibro(
                "Grokking Algorithms",
                "Aditya Bhargava",
                "Algoritmos",
                "9781617292231",
                "Algoritmos explicados de forma visual y amigable.",
                "https://covers.openlibrary.org/b/isbn/9781617292231-L.jpg",
                new BigDecimal("99000"),
                16
        ));

        guardar(crearLibro(
                "Cracking the Coding Interview",
                "Gayle Laakmann McDowell",
                "Entrevistas",
                "9780984782857",
                "Preparación técnica para entrevistas de desarrollo.",
                "https://covers.openlibrary.org/b/isbn/9780984782857-L.jpg",
                new BigDecimal("121000"),
                9
        ));

        guardar(crearLibro(
                "Designing Data-Intensive Applications",
                "Martin Kleppmann",
                "Bases de datos",
                "9781449373320",
                "Sistemas confiables, escalables y mantenibles orientados a datos.",
                "https://covers.openlibrary.org/b/isbn/9781449373320-L.jpg",
                new BigDecimal("149000"),
                6
        ));

        guardar(crearLibro(
                "Code Complete",
                "Steve McConnell",
                "Programación",
                "9780735619678",
                "Clásico sobre construcción de software profesional.",
                "https://covers.openlibrary.org/b/isbn/9780735619678-L.jpg",
                new BigDecimal("134000"),
                10
        ));

        guardar(crearLibro(
                "The Clean Coder",
                "Robert C. Martin",
                "Programación",
                "9780137081073",
                "Hábitos y disciplina profesional para desarrolladores.",
                "https://covers.openlibrary.org/b/isbn/9780137081073-L.jpg",
                new BigDecimal("97000"),
                12
        ));
    }

    private Libro crearLibro(String titulo,
                             String autor,
                             String categoria,
                             String isbn,
                             String descripcion,
                             String imagenUrl,
                             BigDecimal precio,
                             int stock) {
        Libro libro = new Libro();
        libro.setTitulo(titulo);
        libro.setAutor(autor);
        libro.setCategoria(categoria);
        libro.setIsbn(isbn);
        libro.setDescripcion(descripcion);
        libro.setImagenUrl(imagenUrl);
        libro.setPrecio(precio);
        libro.setStock(stock);
        libro.setActivo(true);
        return libro;
    }
}
