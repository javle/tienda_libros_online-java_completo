package com.librosonline.service;

import com.librosonline.dto.LibroDTO;
import com.librosonline.exception.ResourceNotFoundException;
import com.librosonline.model.Libro;
import com.librosonline.repository.LibroRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<LibroDTO> listarActivos(String q, String categoria, Pageable pageable) {
        Page<Libro> libros;
        if (q != null && !q.isBlank()) {
            libros = libroRepository.buscarCatalogo(q, pageable);
        } else if (categoria != null && !categoria.isBlank()) {
            libros = libroRepository.findByActivoTrueAndCategoriaOrderByFechaActualizacionDesc(categoria, pageable);
        } else {
            libros = libroRepository.findByActivoTrueOrderByFechaActualizacionDesc(pageable);
        }
        return libros.map(this::convertirADTO);
    }

    public List<String> obtenerCategorias() {
        return libroRepository.findCategoriasActivas();
    }

    public Page<LibroDTO> listarTodos(Pageable pageable) {
        return libroRepository.findAll(pageable).map(this::convertirADTO);
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

    public LibroDTO obtenerPorIdDTO(Long id) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con ID: " + id));
        return convertirADTO(libro);
    }

    public void guardarDTO(LibroDTO libroDTO) {
        Libro libro;
        if (libroDTO.getId() != null) {
            libro = libroRepository.findById(libroDTO.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Libro no encontrado con ID: " + libroDTO.getId()));
        } else {
            libro = new Libro();
        }

        libro.setTitulo(libroDTO.getTitulo());
        libro.setAutor(libroDTO.getAutor());
        libro.setCategoria(libroDTO.getCategoria());
        libro.setIsbn(libroDTO.getIsbn());
        libro.setDescripcion(libroDTO.getDescripcion());
        libro.setImagenUrl(libroDTO.getImagenUrl());
        libro.setPrecio(libroDTO.getPrecio());
        libro.setStock(libroDTO.getStock());
        
        // Si el libro ya existía, podríamos querer respetar su estado activo.
        // Pero si estamos editándolo desde un DTO que incluye 'activo', debemos actualizarlo.
        libro.setActivo(libroDTO.isActivo());
        
        libroRepository.save(libro);
    }

    private LibroDTO convertirADTO(Libro libro) {
        LibroDTO dto = new LibroDTO();
        dto.setId(libro.getId());
        dto.setTitulo(libro.getTitulo());
        dto.setAutor(libro.getAutor());
        dto.setCategoria(libro.getCategoria());
        dto.setIsbn(libro.getIsbn());
        dto.setDescripcion(libro.getDescripcion());
        dto.setImagenUrl(libro.getImagenUrl());
        dto.setPrecio(libro.getPrecio());
        dto.setStock(libro.getStock());
        dto.setActivo(libro.isActivo());
        return dto;
    }

    public void crearLibrosIniciales() {
        List<Libro> existentes = libroRepository.findAll();
        List<String> titulos = existentes.stream().map(Libro::getTitulo).toList();

        // Actualizar categorías de los existentes según la petición del usuario
        for (Libro ex : existentes) {
            boolean cambiado = false;
            if (ex.getTitulo().equals("Designing Data-Intensive Applications")) { ex.setCategoria("Diseño de Sistemas"); cambiado = true; }
            if (ex.getTitulo().equals("The Clean Coder")) { ex.setCategoria("Habilidades Blandas y Carrera"); cambiado = true; }
            if (ex.getTitulo().equals("Grokking Algorithms")) { ex.setCategoria("Algoritmos"); cambiado = true; }
            if (ex.getTitulo().equals("Cracking the Coding Interview")) { ex.setCategoria("Entrevistas"); cambiado = true; }
            if (cambiado) libroRepository.save(ex);
        }

        if (titulos.isEmpty()) {
            guardar(crearLibro("Clean Code", "Robert C. Martin", "Programación", "9780132350884", "Buenas prácticas para escribir código limpio y mantenible.", "https://covers.openlibrary.org/b/isbn/9780132350884-L.jpg", new BigDecimal("109000"), 15));
            guardar(crearLibro("The Pragmatic Programmer", "Andrew Hunt, David Thomas", "Programación", "9780135957059", "Uno de los clásicos más recomendados para desarrolladores.", "https://covers.openlibrary.org/b/isbn/9780135957059-L.jpg", new BigDecimal("115000"), 12));
            guardar(crearLibro("Effective Java", "Joshua Bloch", "Java", "9780134685991", "Buenas prácticas esenciales para desarrollar mejor en Java.", "https://covers.openlibrary.org/b/isbn/9780134685991-L.jpg", new BigDecimal("119000"), 10));
            guardar(crearLibro("Head First Design Patterns", "Eric Freeman", "Patrones de diseño", "9780596007126", "Introducción visual y práctica a patrones de diseño.", "https://covers.openlibrary.org/b/isbn/9780596007126-L.jpg", new BigDecimal("125000"), 11));
            guardar(crearLibro("Refactoring", "Martin Fowler", "Programación", "9780134757599", "Cómo mejorar el diseño del código existente sin romperlo.", "https://covers.openlibrary.org/b/isbn/9780134757599-L.jpg", new BigDecimal("129000"), 9));
            guardar(crearLibro("Design Patterns", "Erich Gamma", "Patrones de diseño", "9780201633610", "El libro base de patrones orientados a objetos.", "https://covers.openlibrary.org/b/isbn/9780201633610-L.jpg", new BigDecimal("139000"), 8));
            guardar(crearLibro("Clean Architecture", "Robert C. Martin", "Arquitectura", "9780134494166", "Principios para estructurar software mantenible y escalable.", "https://covers.openlibrary.org/b/isbn/9780134494166-L.jpg", new BigDecimal("124000"), 12));
            guardar(crearLibro("Domain-Driven Design", "Eric Evans", "Arquitectura", "9780321125217", "Diseño guiado por dominio para sistemas complejos.", "https://covers.openlibrary.org/b/isbn/9780321125217-L.jpg", new BigDecimal("145000"), 7));
            guardar(crearLibro("Spring in Action", "Craig Walls", "Backend", "9781617294945", "Guía práctica para desarrollar aplicaciones con Spring.", "https://covers.openlibrary.org/b/isbn/9781617294945-L.jpg", new BigDecimal("118000"), 14));
            guardar(crearLibro("Java Concurrency in Practice", "Brian Goetz", "Java", "9780321349606", "Concurrencia segura y escalable en Java.", "https://covers.openlibrary.org/b/isbn/9780321349606-L.jpg", new BigDecimal("132000"), 8));
            guardar(crearLibro("Head First Java", "Kathy Sierra", "Java", "9781491910771", "Libro muy popular para aprender Java desde cero.", "https://covers.openlibrary.org/b/isbn/9781491910771-L.jpg", new BigDecimal("114000"), 13));
            guardar(crearLibro("Grokking Algorithms", "Aditya Bhargava", "Algoritmos", "9781617292231", "Algoritmos explicados de forma visual y amigable.", "https://covers.openlibrary.org/b/isbn/9781617292231-L.jpg", new BigDecimal("99000"), 16));
            guardar(crearLibro("Cracking the Coding Interview", "Gayle McDowell", "Entrevistas", "9780984782857", "Preparación técnica para entrevistas de desarrollo.", "https://covers.openlibrary.org/b/isbn/9780984782857-L.jpg", new BigDecimal("121000"), 9));
            guardar(crearLibro("Designing Data-Intensive Applications", "Martin Kleppmann", "Diseño de Sistemas", "9781449373320", "Sistemas confiables, escalables y mantenibles orientados a datos.", "https://covers.openlibrary.org/b/isbn/9781449373320-L.jpg", new BigDecimal("149000"), 6));
            guardar(crearLibro("Code Complete", "Steve McConnell", "Programación", "9780735619678", "Clásico sobre construcción de software profesional.", "https://covers.openlibrary.org/b/isbn/9780735619678-L.jpg", new BigDecimal("134000"), 10));
            guardar(crearLibro("The Clean Coder", "Robert C. Martin", "Habilidades Blandas y Carrera", "9780137081073", "Hábitos y disciplina profesional para desarrolladores.", "https://covers.openlibrary.org/b/isbn/9780137081073-L.jpg", new BigDecimal("97000"), 12));
        }

        // Nuevos Libros a insertar si no existen
        agregarSiNoExiste(titulos, "The Phoenix Project", "Gene Kim", "DevOps y Cloud", "9781942788294", "Novela sobre IT, DevOps y el éxito del negocio.", "https://covers.openlibrary.org/b/isbn/9781942788294-L.jpg", new BigDecimal("105000"), 15);
        agregarSiNoExiste(titulos, "Continuous Delivery", "Jez Humble, David Farley", "DevOps y Cloud", "9780321601919", "Principios para entregas de software rápidas y confiables.", "https://covers.openlibrary.org/b/isbn/9780321601919-L.jpg", new BigDecimal("135000"), 8);
        agregarSiNoExiste(titulos, "Soft Skills: The software developer's life manual", "John Sonmez", "Habilidades Blandas y Carrera", "9781617292392", "Guía de vida, finanzas y productividad para desarrolladores.", "https://covers.openlibrary.org/b/isbn/9781617292392-L.jpg", new BigDecimal("112000"), 20);
        agregarSiNoExiste(titulos, "Real-World Cryptography", "David Wong", "Ciberseguridad", "9781617296710", "Criptografía aplicada para sistemas modernos y seguros.", "https://covers.openlibrary.org/b/isbn/9781617296710-L.jpg", new BigDecimal("155000"), 10);
        agregarSiNoExiste(titulos, "Site Reliability Engineering", "Niall Richard Murphy", "Diseño de Sistemas", "9781491929124", "Cómo Google opera sistemas en producción a gran escala.", "https://covers.openlibrary.org/b/isbn/9781491929124-L.jpg", new BigDecimal("142000"), 12);
        agregarSiNoExiste(titulos, "Building Microservices", "Sam Newman", "Diseño de Sistemas", "9781492034025", "Diseñando sistemas distribuidos finamente acoplados.", "https://covers.openlibrary.org/b/isbn/9781492034025-L.jpg", new BigDecimal("138000"), 15);
        agregarSiNoExiste(titulos, "System Design Interview", "Alex Xu", "Diseño de Sistemas", "9781736049112", "Guía definitiva para superar entrevistas de diseño de sistemas.", "https://covers.openlibrary.org/b/isbn/9781736049112-L.jpg", new BigDecimal("165000"), 25);
        agregarSiNoExiste(titulos, "Serious Cryptography", "Jean-Philippe Aumasson", "Ciberseguridad", "9781593278267", "Una introducción moderna a la criptografía práctica.", "https://covers.openlibrary.org/b/isbn/9781593278267-L.jpg", new BigDecimal("148000"), 9);
        agregarSiNoExiste(titulos, "Black Hat Python", "Justin Seitz", "Ciberseguridad", "9781593275907", "Programación en Python para hackers y pentesters.", "https://covers.openlibrary.org/b/isbn/9781593275907-L.jpg", new BigDecimal("125000"), 14);
        agregarSiNoExiste(titulos, "The DevOps Handbook", "Gene Kim", "DevOps y Cloud", "9781942788003", "Cómo crear agilidad, confiabilidad y seguridad de clase mundial.", "https://covers.openlibrary.org/b/isbn/9781942788003-L.jpg", new BigDecimal("130000"), 18);
        agregarSiNoExiste(titulos, "Atomic Habits", "James Clear", "Habilidades Blandas y Carrera", "9780735211292", "Cambios pequeños, resultados extraordinarios.", "https://covers.openlibrary.org/b/isbn/9780735211292-L.jpg", new BigDecimal("85000"), 30);
        agregarSiNoExiste(titulos, "Deep Work", "Cal Newport", "Habilidades Blandas y Carrera", "9781455586691", "Reglas para el éxito enfocado en un mundo distraído.", "https://covers.openlibrary.org/b/isbn/9781455586691-L.jpg", new BigDecimal("92000"), 22);
        agregarSiNoExiste(titulos, "The Software Craftsman", "Sandro Mancuso", "Habilidades Blandas y Carrera", "9780134052502", "Profesionalismo, pragmatismo y orgullo en el desarrollo.", "https://covers.openlibrary.org/b/isbn/9780134052502-L.jpg", new BigDecimal("115000"), 11);
        agregarSiNoExiste(titulos, "Kubernetes Up & Running", "Kelsey Hightower", "DevOps y Cloud", "9781492046530", "Orquestación de contenedores en la nube.", "https://covers.openlibrary.org/b/isbn/9781492046530-L.jpg", new BigDecimal("152000"), 16);
    }

    private void agregarSiNoExiste(List<String> titulosExistentes, String titulo, String autor, String categoria, String isbn, String descripcion, String imagenUrl, BigDecimal precio, int stock) {
        if (!titulosExistentes.contains(titulo)) {
            guardar(crearLibro(titulo, autor, categoria, isbn, descripcion, imagenUrl, precio, stock));
        }
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
