package com.librosonline.repository;

import com.librosonline.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    List<Libro> findByActivoTrueOrderByFechaActualizacionDesc();
    List<Libro> findByActivoTrueAndTituloContainingIgnoreCaseOrActivoTrueAndAutorContainingIgnoreCaseOrderByFechaActualizacionDesc(String titulo, String autor);
}
