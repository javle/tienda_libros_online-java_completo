package com.librosonline.dto;

import java.math.BigDecimal;

public class CarritoItem {
    private Long libroId;
    private String titulo;
    private BigDecimal precio;
    private int cantidad;

    public CarritoItem() {
    }

    public CarritoItem(Long libroId, String titulo, BigDecimal precio, int cantidad) {
        this.libroId = libroId;
        this.titulo = titulo;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public Long getLibroId() {
        return libroId;
    }

    public void setLibroId(Long libroId) {
        this.libroId = libroId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getSubtotal() {
        return precio.multiply(BigDecimal.valueOf(cantidad));
    }
}
