package com.ticket.datastructures;

import com.ticket.model.Ticket;
import java.util.ArrayList;
import java.util.List;

public class ColaTickets {

    private NodoTicket frente; // primer ticket en la cola (el que se atiende)
    private NodoTicket final_; // último ticket en la cola (donde se agregan nuevos)
    private int tamanio;

    public ColaTickets() {
        this.frente = null;
        this.final_ = null;
        this.tamanio = 0;
    }

    /**
     * ENCOLAR: Agrega un ticket al FINAL de la cola — O(1).
     * Se usa el puntero 'final_' para no recorrer toda la lista.
     */
    public void encolar(Ticket ticket) {
        NodoTicket nuevo = new NodoTicket(ticket);
        if (final_ == null) {
            frente = nuevo; // cola vacía: el nuevo es frente y final
            final_ = nuevo;
        } else {
            final_.siguiente = nuevo; // el antiguo final apunta al nuevo
            final_ = nuevo; // el nuevo pasa a ser el final
        }
        tamanio++;
    }

    /**
     * DESENCOLAR: Retira y devuelve el ticket del FRENTE — O(1).
     * Simula que un agente "atiende" el ticket más antiguo.
     */
    public Ticket desencolar() {
        if (frente == null)
            return null;
        Ticket atendido = frente.ticket;
        frente = frente.siguiente; // el frente ahora es el siguiente
        if (frente == null) {
            final_ = null; // la cola quedó vacía
        }
        tamanio--;
        return atendido;
    }

    /**
     * VER FRENTE: Consulta el próximo a atender sin retirarlo.
     */
    public Ticket verFrente() {
        if (frente == null)
            return null;
        return frente.ticket;
    }

    /**
     * Devuelve todos los tickets en orden de atención (FIFO).
     */
    public List<Ticket> obtenerCola() {
        List<Ticket> resultado = new ArrayList<>();
        NodoTicket actual = frente;
        while (actual != null) {
            resultado.add(actual.ticket);
            actual = actual.siguiente;
        }
        return resultado;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int getTamanio() {
        return tamanio;
    }
}
