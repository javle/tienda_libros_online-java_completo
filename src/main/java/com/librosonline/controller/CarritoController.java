package com.librosonline.controller;

import com.librosonline.service.CarritoService;
import com.librosonline.service.LibroService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final LibroService libroService;
    private final CarritoService carritoService;

    public CarritoController(LibroService libroService, CarritoService carritoService) {
        this.libroService = libroService;
        this.carritoService = carritoService;
    }

    @GetMapping
    public String verCarrito(Model model, HttpSession session) {
        model.addAttribute("items", carritoService.obtenerCarrito(session));
        model.addAttribute("total", carritoService.calcularTotal(session));
        return "carrito/index";
    }

    @PostMapping("/api/agregar/{id}")
    @ResponseBody
    public java.util.Map<String, Object> agregarApi(@PathVariable Long id,
                          @RequestParam(defaultValue = "1") int cantidad,
                          HttpSession session) {
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        return libroService.buscarPorId(id)
                .map(libro -> {
                    int qtyInCart = carritoService.obtenerCarrito(session).stream()
                            .filter(i -> i.getLibroId().equals(libro.getId()))
                            .mapToInt(com.librosonline.dto.CarritoItem::getCantidad)
                            .sum();
                            
                    int limite = Math.min(libro.getStock(), 10);
                    if (qtyInCart + cantidad > limite) {
                        response.put("success", false);
                        response.put("mensaje", "Límite alcanzado (" + limite + " unidades max).");
                        return response;
                    }

                    carritoService.agregarLibro(session, libro, Math.max(cantidad, 1));
                    response.put("success", true);
                    response.put("cartCount", carritoService.cantidadItems(session));
                    response.put("mensaje", "¡Libro agregado!");
                    return response;
                })
                .orElseGet(() -> {
                    response.put("success", false);
                    response.put("mensaje", "Libro no encontrado.");
                    return response;
                });
    }

    @PostMapping("/agregar/{id}")
    public String agregar(@PathVariable Long id,
                          @RequestParam(defaultValue = "1") int cantidad,
                          HttpSession session,
                          RedirectAttributes redirectAttributes) {
        return libroService.buscarPorId(id)
                .map(libro -> {
                    carritoService.agregarLibro(session, libro, Math.max(cantidad, 1));
                    redirectAttributes.addFlashAttribute("mensajeExito", "Libro agregado al carrito.");
                    return "redirect:/libros";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("mensajeError", "Libro no encontrado.");
                    return "redirect:/libros";
                });
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @RequestParam int cantidad, HttpSession session, RedirectAttributes redirectAttributes) {
        if (cantidad < 1) {
            redirectAttributes.addFlashAttribute("mensajeError", "La cantidad no puede ser menor a 1.");
            return "redirect:/carrito";
        }
        
        libroService.buscarPorId(id).ifPresentOrElse(libro -> {
            int limite = Math.min(libro.getStock(), 10);
            if (cantidad > limite) {
                redirectAttributes.addFlashAttribute("mensajeError", "Solo puedes comprar hasta " + limite + " unidades de '" + libro.getTitulo() + "'.");
                carritoService.actualizarCantidad(session, id, limite);
            } else {
                carritoService.actualizarCantidad(session, id, cantidad);
            }
        }, () -> redirectAttributes.addFlashAttribute("mensajeError", "Libro no encontrado."));

        return "redirect:/carrito";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session) {
        carritoService.eliminar(session, id);
        return "redirect:/carrito";
    }

}
