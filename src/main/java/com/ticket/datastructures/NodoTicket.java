package com.ticket.datastructures;

import com.ticket.model.Ticket;

public class NodoTicket {

    public Ticket ticket; // dato almacenado en este nodo
    public NodoTicket siguiente; // puntero al siguiente nodo

    public NodoTicket(Ticket ticket) {
        this.ticket = ticket;
        this.siguiente = null;
    }
}
