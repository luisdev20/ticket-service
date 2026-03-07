package com.ticket.datastructures;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PilaTickets {

    private NodoPila tope; // nodo en la cima de la pila
    private Long ticketId;
    private int tamanio;

    public PilaTickets(Long ticketId) {
        this.ticketId = ticketId;
        this.tope = null;
        this.tamanio = 0;
    }

    public void push(String estado) {
        NodoPila nuevo = new NodoPila(estado, LocalDateTime.now().toString());
        nuevo.abajo = tope; // el nuevo tope apunta al anterior
        tope = nuevo;
        tamanio++;
    }

    public NodoPila pop() {
        if (tope == null)
            return null;
        NodoPila retirado = tope;
        tope = tope.abajo; // el tope ahora es el nodo de abajo
        tamanio--;
        return retirado;
    }

    public NodoPila peek() {
        return tope;
    }

    public List<String> obtenerHistorial() {
        List<String> historial = new ArrayList<>();
        NodoPila actual = tope;
        while (actual != null) {
            historial.add(actual.timestamp + " → " + actual.estado);
            actual = actual.abajo;
        }
        return historial;
    }

    public boolean estaVacia() {
        return tope == null;
    }

    public int getTamanio() {
        return tamanio;
    }

    public Long getTicketId() {
        return ticketId;
    }
}
