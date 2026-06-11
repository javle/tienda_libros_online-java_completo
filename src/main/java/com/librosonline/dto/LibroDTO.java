package com.librosonline.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public class LibroDTO {

    private Long id;

    @NotBlank(message = "El título es obligatorio")
    @Size(max = 150, message = "El título no puede exceder los 150 caracteres")
    private String titulo;

    @NotBlank(message = "El autor es obligatorio")
    @Size(max = 120, message = "El autor no puede exceder los 120 caracteres")
    private String autor;

    @Size(max = 50, message = "La categoría no puede exceder los 50 caracteres")
    private String categoria;

    @Size(max = 30, message = "El ISBN no puede exceder los 30 caracteres")
    private String isbn;

    private String descripcion;

    @Size(max = 500, message = "La URL de la imagen es demasiado larga")
    private String imagenUrl;

    @NotNull(message = "El precio es obligatorio")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private BigDecimal precio;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;

    private boolean activo;

    // Campo calculado: devuelve URL por defecto si la imagen está vacía
    public String getImagenPortada() {
        return (imagenUrl == null || imagenUrl.isBlank())
                ? "/img/libro-default.svg"
                : imagenUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
