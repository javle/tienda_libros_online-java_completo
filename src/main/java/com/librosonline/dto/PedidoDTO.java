package com.librosonline.dto;

import com.librosonline.model.PedidoEstado;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PedidoDTO {
    private Long id;
    private String nombreUsuario;
    private LocalDateTime fecha;
    private PedidoEstado estado;
    private BigDecimal total;
    
    // Logistica
    private String direccion;
    private String ciudad;
    private String codigoPostal;
    private String telefono;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public PedidoEstado getEstado() { return estado; }
    public void setEstado(PedidoEstado estado) { this.estado = estado; }
    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getCiudad() { return ciudad; }
    public void setCiudad(String ciudad) { this.ciudad = ciudad; }
    public String getCodigoPostal() { return codigoPostal; }
    public void setCodigoPostal(String codigoPostal) { this.codigoPostal = codigoPostal; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
