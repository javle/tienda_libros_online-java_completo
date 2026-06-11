package com.librosonline.service;

import com.librosonline.dto.CarritoItem;
import com.librosonline.model.Libro;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {

    private CarritoService carritoService;

    @Mock
    private HttpSession session;

    private List<CarritoItem> carritoMock;

    @BeforeEach
    void setUp() {
        carritoService = new CarritoService();
        carritoMock = new ArrayList<>();
        // Lenient since some tests might not call getAttribute
        lenient().when(session.getAttribute("carrito")).thenReturn(carritoMock);
    }

    @Test
    void testAgregarLibroNuevoInicializaCantidad() {
        // Arrange
        Libro libro = new Libro();
        libro.setId(1L);
        libro.setTitulo("Test Book");
        libro.setPrecio(new BigDecimal("50000"));

        // Act
        carritoService.agregarLibro(session, libro, 1);

        // Assert
        assertEquals(1, carritoMock.size(), "El carrito debería tener 1 ítem");
        assertEquals(1L, carritoMock.get(0).getLibroId());
        assertEquals(1, carritoMock.get(0).getCantidad(), "La cantidad debe ser 1");
        verify(session, atLeastOnce()).setAttribute(eq("carrito"), any());
    }

    @Test
    void testAgregarLibroExistenteIncrementaCantidad() {
        // Arrange
        Libro libro = new Libro();
        libro.setId(2L);
        libro.setTitulo("Clean Code");
        libro.setPrecio(new BigDecimal("100000"));

        // Add once
        carritoService.agregarLibro(session, libro, 1);
        
        // Act - Add again
        carritoService.agregarLibro(session, libro, 2);

        // Assert
        assertEquals(1, carritoMock.size(), "No se debe duplicar la fila");
        assertEquals(3, carritoMock.get(0).getCantidad(), "La cantidad se debe sumar correctamente (1+2)");
    }

    @Test
    void testCalcularGranTotalExactitud() {
        // Arrange
        Libro libro1 = new Libro();
        libro1.setId(1L);
        libro1.setTitulo("Libro A");
        libro1.setPrecio(new BigDecimal("50000.50"));

        Libro libro2 = new Libro();
        libro2.setId(2L);
        libro2.setTitulo("Libro B");
        libro2.setPrecio(new BigDecimal("120000.00"));

        carritoService.agregarLibro(session, libro1, 2); // 100001.00
        carritoService.agregarLibro(session, libro2, 1); // 120000.00

        // Act
        BigDecimal total = carritoService.calcularTotal(session);

        // Assert
        BigDecimal expected = new BigDecimal("220001.00");
        assertEquals(0, expected.compareTo(total), "El cálculo del gran total con BigDecimal debe ser exacto");
    }
}
