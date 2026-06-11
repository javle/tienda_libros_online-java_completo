package com.librosonline.service;

import com.librosonline.dto.CarritoItem;
import com.librosonline.model.*;
import com.librosonline.repository.LibroRepository;
import com.librosonline.repository.PedidoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import com.librosonline.dto.PedidoDTO;

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

    @org.springframework.transaction.annotation.Transactional
    public Pedido crearPedidoDesdeCarrito(Usuario usuario, HttpSession session, DireccionEnvio direccion) {
        List<CarritoItem> items = carritoService.obtenerCarrito(session);
        if (items.isEmpty()) {
            throw new IllegalStateException("El carrito está vacío.");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setEstado(PedidoEstado.PAGADO); // Asumimos pago exitoso aquí
        pedido.setDireccionEnvio(direccion);

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

    private PedidoDTO mapToDTO(Pedido p) {
        PedidoDTO dto = new PedidoDTO();
        dto.setId(p.getId());
        dto.setNombreUsuario(p.getUsuario() != null ? p.getUsuario().getNombre() : "Desconocido");
        dto.setFecha(p.getFecha());
        dto.setEstado(p.getEstado());
        dto.setTotal(p.getTotal());
        if (p.getDireccionEnvio() != null) {
            dto.setDireccion(p.getDireccionEnvio().getDireccion());
            dto.setCiudad(p.getDireccionEnvio().getCiudad());
            dto.setCodigoPostal(p.getDireccionEnvio().getCodigoPostal());
            dto.setTelefono(p.getDireccionEnvio().getTelefono());
        }
        return dto;
    }

    public Page<PedidoDTO> listarPorUsuario(Usuario usuario, Pageable pageable) {
        return pedidoRepository.findByUsuarioOrderByFechaDesc(usuario, pageable)
                .map(this::mapToDTO);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Page<Pedido> listarPedidosEntityPorUsuario(Usuario usuario, Pageable pageable) {
        Page<Pedido> pedidos = pedidoRepository.findByUsuarioOrderByFechaDesc(usuario, pageable);
        for (Pedido p : pedidos) {
            p.getDetalles().size(); // forzar carga
            for (PedidoDetalle pd : p.getDetalles()) {
                if (pd.getLibro() != null) {
                    pd.getLibro().getTitulo(); // forzar carga
                }
            }
        }
        return pedidos;
    }

    public Page<PedidoDTO> listarTodos(Pageable pageable) {
        return pedidoRepository.findAllByOrderByFechaDesc(pageable)
                .map(this::mapToDTO);
    }

    public long totalPedidos() {
        return pedidoRepository.count();
    }

    @org.springframework.transaction.annotation.Transactional
    public void actualizarEstado(Long id, PedidoEstado nuevoEstado) {
        pedidoRepository.findById(id).ifPresent(pedido -> {
            // Lógica de Restock automático
            if (nuevoEstado == PedidoEstado.CANCELADO && pedido.getEstado() != PedidoEstado.CANCELADO) {
                for (PedidoDetalle detalle : pedido.getDetalles()) {
                    Libro libro = detalle.getLibro();
                    libro.setStock(libro.getStock() + detalle.getCantidad());
                    libroRepository.save(libro);
                }
            }
            // Si por alguna razón pasa de CANCELADO a PAGADO/PENDIENTE, habría que descontar stock de nuevo, 
            // pero para esta app mantendremos la regla de que CANCELADO es final.
            
            pedido.setEstado(nuevoEstado);
            pedidoRepository.save(pedido);
        });
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Pedido obtenerPedidoValidado(Long id, Usuario usuario, boolean isAdmin) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Pedido no encontrado."));
                
        if (!isAdmin && !pedido.getUsuario().getId().equals(usuario.getId())) {
            throw new IllegalStateException("No tienes permiso para ver este pedido.");
        }
        
        // Forzar inicialización perezosa de los detalles y sus libros para Thymeleaf
        pedido.getDetalles().size(); 
        for (PedidoDetalle pd : pedido.getDetalles()) {
            if (pd.getLibro() != null) {
                pd.getLibro().getTitulo(); // asegurar carga del libro
            }
        }
        return pedido;
    }
}
