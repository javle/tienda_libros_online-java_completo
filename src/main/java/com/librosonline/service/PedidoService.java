package com.librosonline.service;

import com.librosonline.dto.CarritoItem;
import com.librosonline.model.*;
import com.librosonline.repository.LibroRepository;
import com.librosonline.repository.PedidoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final LibroRepository libroRepository;
    private final CarritoService carritoService;

    public PedidoService(PedidoRepository pedidoRepository, LibroRepository libroRepository, CarritoService carritoService) {
        this.pedidoRepository = pedidoRepository;
        this.libroRepository = libroRepository;
        this.carritoService = carritoService;
    }

    public Pedido crearPedidoDesdeCarrito(Usuario usuario, HttpSession session) {
        List<CarritoItem> items = carritoService.obtenerCarrito(session);
        if (items.isEmpty()) {
            throw new IllegalStateException("El carrito está vacío.");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setEstado(PedidoEstado.CREADO);

        BigDecimal total = BigDecimal.ZERO;
        for (CarritoItem item : items) {
            Libro libro = libroRepository.findById(item.getLibroId())
                    .orElseThrow(() -> new IllegalStateException("Libro no encontrado: " + item.getLibroId()));

            if (libro.getStock() < item.getCantidad()) {
                throw new IllegalStateException("Stock insuficiente para: " + libro.getTitulo());
            }

            libro.setStock(libro.getStock() - item.getCantidad());
            libroRepository.save(libro);

            PedidoDetalle detalle = new PedidoDetalle();
            detalle.setLibro(libro);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecio());
            detalle.setSubtotal(item.getSubtotal());
            pedido.agregarDetalle(detalle);
            total = total.add(item.getSubtotal());
        }

        pedido.setTotal(total);
        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        carritoService.vaciar(session);
        return pedidoGuardado;
    }

    public List<Pedido> listarPorUsuario(Usuario usuario) {
        return pedidoRepository.findByUsuarioOrderByFechaDesc(usuario);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAllByOrderByFechaDesc();
    }

    public long totalPedidos() {
        return pedidoRepository.count();
    }
}
