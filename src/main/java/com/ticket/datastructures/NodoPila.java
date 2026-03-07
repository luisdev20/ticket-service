package com.ticket.datastructures;

public class NodoPila {

    public String estado; // ej: "ABIERTO", "EN_PROCESO", "CERRADO"
    public String timestamp; // cuándo se cambió al estado
    public NodoPila abajo; // nodo debajo en la pila

    public NodoPila(String estado, String timestamp) {
        this.estado = estado;
        this.timestamp = timestamp;
        this.abajo = null;
    }
}
