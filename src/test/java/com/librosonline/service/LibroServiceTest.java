package com.librosonline.service;

import com.librosonline.dto.LibroDTO;
import com.librosonline.exception.ResourceNotFoundException;
import com.librosonline.model.Libro;
import com.librosonline.repository.LibroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private LibroService libroService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obtenerPorIdDTO_Existente_RetornaDTO() {
        // Arrange
        Libro libro = new Libro();
        libro.setId(1L);
        libro.setTitulo("Test Libro");
        libro.setPrecio(new BigDecimal("100"));
        when(libroRepository.findById(1L)).thenReturn(Optional.of(libro));

        // Act
        LibroDTO result = libroService.obtenerPorIdDTO(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Test Libro", result.getTitulo());
        verify(libroRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerPorIdDTO_NoExistente_LanzaExcepcion() {
        // Arrange
        when(libroRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            libroService.obtenerPorIdDTO(99L);
        });
        verify(libroRepository, times(1)).findById(99L);
    }
}
