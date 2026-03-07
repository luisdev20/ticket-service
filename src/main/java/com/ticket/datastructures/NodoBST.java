package com.ticket.datastructures;

import com.ticket.model.Ticket;

public class NodoBST {

    public Ticket ticket;
    public NodoBST izquierdo; // id < clave actual
    public NodoBST derecho; // id > clave actual

    public NodoBST(Ticket ticket) {
        this.ticket = ticket;
        this.izquierdo = null;
        this.derecho = null;
    }
}
