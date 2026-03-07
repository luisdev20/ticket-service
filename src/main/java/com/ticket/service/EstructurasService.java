package com.ticket.service;

import com.ticket.datastructures.*;
import com.ticket.model.Ticket;
import com.ticket.model.Usuario;
import com.ticket.repository.TicketRepository;
import com.ticket.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstructurasService {

    private final TicketRepository ticketRepository;
    private final UsuarioRepository usuarioRepository;

    // Pilas de historial en memoria (una por ticket), persisten durante la sesión
    private final Map<Long, PilaTickets> pilasPorTicket = new HashMap<>();

    public ListaEnlazadaTickets construirListaEnlazada() {
        log.info("[ListaEnlazada] Cargando tickets de la BD y construyendo lista enlazada");
        List<Ticket> tickets = ticketRepository.findAll();
        ListaEnlazadaTickets lista = new ListaEnlazadaTickets();
        for (Ticket t : tickets) {
            lista.agregar(t);
        }
        log.info("[ListaEnlazada] Lista construida con {} nodos", lista.getTamanio());
        return lista;
    }

    public Ticket buscarEnLista(Long id) {
        log.info("[ListaEnlazada] Búsqueda lineal por ID: {}", id);
        return construirListaEnlazada().buscarPorId(id);
    }

    public PilaTickets obtenerPila(Long ticketId) {
        if (!pilasPorTicket.containsKey(ticketId)) {
            PilaTickets pila = new PilaTickets(ticketId);
            // inicializar con el estado actual del ticket desde la BD
            ticketRepository.findById(ticketId).ifPresent(t -> {
                pila.push(t.getEstado().name());
                log.info("[Pila] Pila inicializada con estado '{}' para ticket {}", t.getEstado(), ticketId);
            });
            pilasPorTicket.put(ticketId, pila);
        }
        return pilasPorTicket.get(ticketId);
    }

    public List<String> pushEstado(Long ticketId, String nuevoEstado) {
        PilaTickets pila = obtenerPila(ticketId);
        pila.push(nuevoEstado);
        log.info("[Pila] Estado '{}' apilado en ticket {}", nuevoEstado, ticketId);
        return pila.obtenerHistorial();
    }

    public String popEstado(Long ticketId) {
        PilaTickets pila = obtenerPila(ticketId);
        NodoPila retirado = pila.pop();
        if (retirado == null) {
            log.warn("[Pila] La pila del ticket {} está vacía", ticketId);
            return null;
        }
        log.info("[Pila] Estado '{}' retirado del ticket {}", retirado.estado, ticketId);
        return retirado.estado;
    }

    public ColaTickets construirCola() {
        log.info("[Cola] Construyendo cola FIFO de tickets pendientes");
        List<Ticket> tickets = ticketRepository.findAll();
        // Filtrar solo tickets abiertos y ordenar por fecha de creación
        ColaTickets cola = new ColaTickets();
        tickets.stream()
                .filter(t -> t.getEstado() != null &&
                        t.getEstado().name().equals("ABIERTO"))
                .sorted((a, b) -> {
                    if (a.getFechaCreacion() == null)
                        return 1;
                    if (b.getFechaCreacion() == null)
                        return -1;
                    return a.getFechaCreacion().compareTo(b.getFechaCreacion());
                })
                .forEach(cola::encolar);
        log.info("[Cola] Cola construida con {} tickets", cola.getTamanio());
        return cola;
    }

    public ArbolBSTTickets construirBST() {
        log.info("[BST] Construyendo árbol BST con tickets de la BD");
        List<Ticket> tickets = ticketRepository.findAll();
        ArbolBSTTickets arbol = new ArbolBSTTickets();
        for (Ticket t : tickets) {
            arbol.insertar(t);
        }
        log.info("[BST] Árbol construido con {} nodos", tickets.size());
        return arbol;
    }

    public Ticket buscarEnBST(Long id) {
        log.info("[BST] Búsqueda O(log n) por ID: {}", id);
        return construirBST().buscar(id);
    }

    public TablaHashUsuarios construirTablaHash() {
        log.info("[Hash] Construyendo tabla hash de usuarios");
        List<Usuario> usuarios = usuarioRepository.findAll();
        TablaHashUsuarios tabla = new TablaHashUsuarios();
        for (Usuario u : usuarios) {
            tabla.insertar(u);
        }
        log.info("[Hash] Tabla construida con {} usuarios en {} cubetas",
                tabla.getTamanio(), tabla.getCapacidad());
        return tabla;
    }

    public Usuario buscarEnHash(String email) {
        log.info("[Hash] Búsqueda por email: {}", email);
        return construirTablaHash().buscar(email);
    }
}
