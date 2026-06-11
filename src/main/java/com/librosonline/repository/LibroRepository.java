package com.librosonline.repository;

import com.librosonline.model.Libro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    
    Page<Libro> findByActivoTrueOrderByFechaActualizacionDesc(Pageable pageable);
    
    Page<Libro> findByActivoTrueAndCategoriaOrderByFechaActualizacionDesc(String categoria, Pageable pageable);
    
    @Query("SELECT l FROM Libro l WHERE l.activo = true AND (LOWER(l.titulo) LIKE LOWER(CONCAT('%', :q, '%')) OR LOWER(l.autor) LIKE LOWER(CONCAT('%', :q, '%')))")
    Page<Libro> buscarCatalogo(@Param("q") String q, Pageable pageable);

    @Query("SELECT DISTINCT l.categoria FROM Libro l WHERE l.activo = true AND l.categoria IS NOT NULL AND l.categoria != '' ORDER BY l.categoria")
    List<String> findCategoriasActivas();
}
