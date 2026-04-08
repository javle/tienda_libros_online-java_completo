package com.librosonline.service;

import com.librosonline.dto.CarritoItem;
import com.librosonline.model.Libro;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CarritoService {

    private static final String CARRITO_KEY = "carrito";

    @SuppressWarnings("unchecked")
    public List<CarritoItem> obtenerCarrito(HttpSession session) {
        List<CarritoItem> carrito = (List<CarritoItem>) session.getAttribute(CARRITO_KEY);
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute(CARRITO_KEY, carrito);
        }
        return carrito;
    }

    public void agregarLibro(HttpSession session, Libro libro, int cantidad) {
        List<CarritoItem> carrito = obtenerCarrito(session);
        for (CarritoItem item : carrito) {
            if (item.getLibroId().equals(libro.getId())) {
                item.setCantidad(item.getCantidad() + cantidad);
                session.setAttribute(CARRITO_KEY, carrito);
                return;
            }
        }
        carrito.add(new CarritoItem(libro.getId(), libro.getTitulo(), libro.getPrecio(), cantidad));
        session.setAttribute(CARRITO_KEY, carrito);
    }

    public void actualizarCantidad(HttpSession session, Long libroId, int cantidad) {
        List<CarritoItem> carrito = obtenerCarrito(session);
        carrito.removeIf(item -> {
            if (item.getLibroId().equals(libroId)) {
                if (cantidad <= 0) {
                    return true;
                }
                item.setCantidad(cantidad);
            }
            return false;
        });
        session.setAttribute(CARRITO_KEY, carrito);
    }

    public void eliminar(HttpSession session, Long libroId) {
        List<CarritoItem> carrito = obtenerCarrito(session);
        carrito.removeIf(item -> item.getLibroId().equals(libroId));
        session.setAttribute(CARRITO_KEY, carrito);
    }

    public BigDecimal calcularTotal(HttpSession session) {
        return obtenerCarrito(session).stream()
                .map(CarritoItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int cantidadItems(HttpSession session) {
        return obtenerCarrito(session).stream().mapToInt(CarritoItem::getCantidad).sum();
    }

    public void vaciar(HttpSession session) {
        session.setAttribute(CARRITO_KEY, new ArrayList<CarritoItem>());
    }
}
