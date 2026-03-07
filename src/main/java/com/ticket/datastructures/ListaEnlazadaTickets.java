package com.ticket.datastructures;

import com.ticket.model.Ticket;
import java.util.ArrayList;
import java.util.List;

public class ListaEnlazadaTickets {

    private NodoTicket cabeza; // primer nodo de la lista
    private int tamanio;

    public ListaEnlazadaTickets() {
        this.cabeza = null;
        this.tamanio = 0;
    }

    public void agregar(Ticket ticket) {
        NodoTicket nuevo = new NodoTicket(ticket);
        if (cabeza == null) {
            cabeza = nuevo; // lista vacía: el nuevo nodo es la cabeza
        } else {
            NodoTicket actual = cabeza;
            while (actual.siguiente != null) { // recorrer hasta el final
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo; // enlazar al final
        }
        tamanio++;
    }

    public void agregarAlInicio(Ticket ticket) {
        NodoTicket nuevo = new NodoTicket(ticket);
        nuevo.siguiente = cabeza;
        cabeza = nuevo;
        tamanio++;
    }

    public List<Ticket> obtenerTodos() {
        List<Ticket> resultado = new ArrayList<>();
        NodoTicket actual = cabeza;
        while (actual != null) {
            resultado.add(actual.ticket);
            actual = actual.siguiente; // avanzar al siguiente nodo
        }
        return resultado;
    }

    public Ticket buscarPorId(Long id) {
        NodoTicket actual = cabeza;
        while (actual != null) {
            if (actual.ticket.getId().equals(id)) {
                return actual.ticket; // encontrado
            }
            actual = actual.siguiente;
        }
        return null; // no encontrado
    }

    public boolean eliminar(Long id) {
        if (cabeza == null)
            return false;

        // caso especial: eliminar la cabeza
        if (cabeza.ticket.getId().equals(id)) {
            cabeza = cabeza.siguiente;
            tamanio--;
            return true;
        }

        NodoTicket anterior = cabeza;
        NodoTicket actual = cabeza.siguiente;
        while (actual != null) {
            if (actual.ticket.getId().equals(id)) {
                anterior.siguiente = actual.siguiente; // saltar el nodo eliminado
                tamanio--;
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
        return false;
    }

    public int getTamanio() {
        return tamanio;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }
}
