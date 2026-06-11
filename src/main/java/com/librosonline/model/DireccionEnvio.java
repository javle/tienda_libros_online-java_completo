package com.librosonline.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DireccionEnvio {

    @Column(length = 200)
    private String direccion;

    @Column(length = 100)
    private String ciudad;

    @Column(length = 20)
    private String codigoPostal;

    @Column(length = 30)
    private String telefono;

    public DireccionEnvio() {
    }

    public DireccionEnvio(String direccion, String ciudad, String codigoPostal, String telefono) {
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.codigoPostal = codigoPostal;
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
