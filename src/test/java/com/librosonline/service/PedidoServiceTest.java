package com.librosonline.service;

import com.librosonline.dto.CarritoItem;
import com.librosonline.model.*;
import com.librosonline.repository.LibroRepository;
import com.librosonline.repository.PedidoRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private LibroRepository libroRepository;

    @Mock
    private CarritoService carritoService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void testCheckoutExitosoDescuentaStock() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        DireccionEnvio direccion = new DireccionEnvio();
        
        List<CarritoItem> items = new ArrayList<>();
        CarritoItem item = new CarritoItem(10L, "Libro de Test", new BigDecimal("10000"), 2, "/img.jpg");
        items.add(item);

        when(carritoService.obtenerCarrito(session)).thenReturn(items);

        Libro libroEnDb = new Libro();
        libroEnDb.setId(10L);
        libroEnDb.setStock(5); // Tiene 5, compramos 2, deben quedar 3
        
        when(libroRepository.findById(10L)).thenReturn(Optional.of(libroEnDb));
        when(pedidoRepository.save(any(Pedido.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        Pedido resultado = pedidoService.crearPedidoDesdeCarrito(usuario, session, direccion);

        // Assert
        assertNotNull(resultado);
        assertEquals(3, libroEnDb.getStock(), "El stock debe haberse descontado correctamente");
        verify(libroRepository, times(1)).save(libroEnDb);
        verify(carritoService, times(1)).vaciar(session);
    }

    @Test
    void testCheckoutLanzaExcepcionSiCarritoEstaVacio() {
        // Arrange
        Usuario usuario = new Usuario();
        DireccionEnvio direccion = new DireccionEnvio();
        
        when(carritoService.obtenerCarrito(session)).thenReturn(new ArrayList<>()); // Vacío

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            pedidoService.crearPedidoDesdeCarrito(usuario, session, direccion);
        });
        
        assertEquals("El carrito está vacío.", exception.getMessage());
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }

    @Test
    void testRestockPorCancelacionDePedido() {
        // Arrange
        Pedido pedido = new Pedido();
        pedido.setId(100L);
        pedido.setEstado(PedidoEstado.PAGADO);

        Libro libro = new Libro();
        libro.setId(5L);
        libro.setStock(10); // Tiene 10 en BD actualmente

        PedidoDetalle detalle = new PedidoDetalle();
        detalle.setLibro(libro);
        detalle.setCantidad(3); // Se habían comprado 3 en este pedido
        pedido.agregarDetalle(detalle);

        when(pedidoRepository.findById(100L)).thenReturn(Optional.of(pedido));

        // Act
        pedidoService.actualizarEstado(100L, PedidoEstado.CANCELADO);

        // Assert
        assertEquals(PedidoEstado.CANCELADO, pedido.getEstado());
        assertEquals(13, libro.getStock(), "El stock debe haber regresado a la normalidad (+3)");
        verify(libroRepository, times(1)).save(libro);
        verify(pedidoRepository, times(1)).save(pedido);
    }
}
