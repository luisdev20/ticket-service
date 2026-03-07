package com.ticket.dto;

import java.util.List;

public class EstructuraResponseDTO {

    private String estructura;
    private String operacion;
    private String complejidad;
    private String descripcion;
    private int tamanio;
    private Object datos;
    private List<String> historial;

    public EstructuraResponseDTO() {
    }

    public EstructuraResponseDTO(String estructura, String operacion,
            String complejidad, String descripcion,
            int tamanio, Object datos) {
        this.estructura = estructura;
        this.operacion = operacion;
        this.complejidad = complejidad;
        this.descripcion = descripcion;
        this.tamanio = tamanio;
        this.datos = datos;
    }

    // Getters y setters
    public String getEstructura() {
        return estructura;
    }

    public void setEstructura(String estructura) {
        this.estructura = estructura;
    }

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    public String getComplejidad() {
        return complejidad;
    }

    public void setComplejidad(String complejidad) {
        this.complejidad = complejidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getTamanio() {
        return tamanio;
    }

    public void setTamanio(int tamanio) {
        this.tamanio = tamanio;
    }

    public Object getDatos() {
        return datos;
    }

    public void setDatos(Object datos) {
        this.datos = datos;
    }

    public List<String> getHistorial() {
        return historial;
    }

    public void setHistorial(List<String> historial) {
        this.historial = historial;
    }
}
