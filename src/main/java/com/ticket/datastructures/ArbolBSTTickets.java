package com.ticket.datastructures;

import com.ticket.model.Ticket;
import java.util.ArrayList;
import java.util.List;

public class ArbolBSTTickets {

    private NodoBST raiz; // raíz del árbol

    public ArbolBSTTickets() {
        this.raiz = null;
    }

    // ---- INSERTAR ----

    /**
     * Inserta un ticket en el árbol usando su ID como clave.
     */
    public void insertar(Ticket ticket) {
        raiz = insertarRec(raiz, ticket);
    }

    private NodoBST insertarRec(NodoBST nodo, Ticket ticket) {
        if (nodo == null) {
            return new NodoBST(ticket); // posición encontrada
        }
        long clave = ticket.getId();
        if (clave < nodo.ticket.getId()) {
            nodo.izquierdo = insertarRec(nodo.izquierdo, ticket); // ir a la izquierda
        } else if (clave > nodo.ticket.getId()) {
            nodo.derecho = insertarRec(nodo.derecho, ticket); // ir a la derecha
        }
        // si clave == id existente, no se duplica
        return nodo;
    }

    // ---- BUSCAR ----

    /**
     * Busca un ticket por ID — O(log n) en árbol balanceado.
     * En cada paso descarta la mitad del árbol según la comparación.
     */
    public Ticket buscar(Long id) {
        NodoBST resultado = buscarRec(raiz, id);
        return (resultado != null) ? resultado.ticket : null;
    }

    private NodoBST buscarRec(NodoBST nodo, Long id) {
        if (nodo == null)
            return null; // no encontrado
        if (id.equals(nodo.ticket.getId()))
            return nodo; // encontrado

        if (id < nodo.ticket.getId()) {
            return buscarRec(nodo.izquierdo, id); // buscar en subárbol izquierdo
        } else {
            return buscarRec(nodo.derecho, id); // buscar en subárbol derecho
        }
    }

    // ---- RECORRIDO INORDEN ----

    /**
     * Recorrido INORDEN (izquierdo → raíz → derecho).
     * Devuelve los tickets ordenados de MENOR a MAYOR id.
     * Propiedad fundamental del BST: el inorden siempre es ordenado.
     */
    public List<Ticket> inorden() {
        List<Ticket> resultado = new ArrayList<>();
        inordenRec(raiz, resultado);
        return resultado;
    }

    private void inordenRec(NodoBST nodo, List<Ticket> resultado) {
        if (nodo == null)
            return;
        inordenRec(nodo.izquierdo, resultado); // primero el subárbol izquierdo
        resultado.add(nodo.ticket); // luego la raíz
        inordenRec(nodo.derecho, resultado); // luego el subárbol derecho
    }

    // ---- ELIMINAR ----

    /**
     * Elimina un nodo del árbol manteniendo la propiedad BST.
     * Caso 3 (nodo con dos hijos): se reemplaza con el sucesor inorden (mínimo del
     * subárbol derecho).
     */
    public void eliminar(Long id) {
        raiz = eliminarRec(raiz, id);
    }

    private NodoBST eliminarRec(NodoBST nodo, Long id) {
        if (nodo == null)
            return null;

        if (id < nodo.ticket.getId()) {
            nodo.izquierdo = eliminarRec(nodo.izquierdo, id);
        } else if (id > nodo.ticket.getId()) {
            nodo.derecho = eliminarRec(nodo.derecho, id);
        } else {
            // nodo encontrado
            if (nodo.izquierdo == null)
                return nodo.derecho; // caso 1: sin hijo izq
            if (nodo.derecho == null)
                return nodo.izquierdo; // caso 2: sin hijo der

            // caso 3: dos hijos — reemplazar con el mínimo del subárbol derecho
            NodoBST sucesor = minimo(nodo.derecho);
            nodo.ticket = sucesor.ticket;
            nodo.derecho = eliminarRec(nodo.derecho, sucesor.ticket.getId());
        }
        return nodo;
    }

    private NodoBST minimo(NodoBST nodo) {
        while (nodo.izquierdo != null)
            nodo = nodo.izquierdo;
        return nodo;
    }

    public boolean estaVacio() {
        return raiz == null;
    }
}
