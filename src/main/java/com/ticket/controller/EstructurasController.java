package com.ticket.controller;

import com.ticket.datastructures.*;
import com.ticket.dto.EstructuraResponseDTO;
import com.ticket.model.Ticket;
import com.ticket.model.Usuario;
import com.ticket.service.EstructurasService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estructuras")
@RequiredArgsConstructor
@Slf4j
public class EstructurasController {

        private final EstructurasService estructurasService;

        @GetMapping("/lista")
        public ResponseEntity<EstructuraResponseDTO> obtenerLista() {
                log.info("GET /api/estructuras/lista");
                ListaEnlazadaTickets lista = estructurasService.construirListaEnlazada();
                List<Ticket> tickets = lista.obtenerTodos();

                EstructuraResponseDTO resp = new EstructuraResponseDTO(
                                "Lista Enlazada Simple",
                                "recorridoCompleto",
                                "O(n)",
                                "Se recorrieron " + lista.getTamanio() + " nodos enlazados desde la cabeza hasta null.",
                                lista.getTamanio(),
                                tickets);
                return ResponseEntity.ok(resp);
        }

        /**
         * Búsqueda lineal O(n) en la lista enlazada por ID de ticket.
         */
        @GetMapping("/lista/buscar/{id}")
        public ResponseEntity<EstructuraResponseDTO> buscarEnLista(@PathVariable Long id) {
                log.info("GET /api/estructuras/lista/buscar/{}", id);
                Ticket ticket = estructurasService.buscarEnLista(id);

                EstructuraResponseDTO resp = new EstructuraResponseDTO(
                                "Lista Enlazada Simple",
                                "buscarPorId",
                                "O(n)",
                                ticket != null
                                                ? "Ticket ID=" + id + " encontrado recorriendo la lista nodo a nodo."
                                                : "Ticket ID=" + id + " no encontrado (se recorrió toda la lista).",
                                1,
                                ticket);
                return ticket != null ? ResponseEntity.ok(resp) : ResponseEntity.notFound().build();
        }

        /**
         * Consulta el historial de estados apilados para un ticket (LIFO).
         */
        @GetMapping("/pila/{ticketId}")
        public ResponseEntity<EstructuraResponseDTO> verPila(@PathVariable Long ticketId) {
                log.info("GET /api/estructuras/pila/{}", ticketId);
                PilaTickets pila = estructurasService.obtenerPila(ticketId);
                NodoPila tope = pila.peek();

                EstructuraResponseDTO resp = new EstructuraResponseDTO(
                                "Pila (Stack - LIFO)",
                                "peek + historial",
                                "O(1) peek | O(n) historial",
                                "Estado actual (tope): " + (tope != null ? tope.estado : "vacía") +
                                                ". Historial completo del mas reciente al mas antiguo.",
                                pila.getTamanio(),
                                null);
                resp.setHistorial(pila.obtenerHistorial());
                return ResponseEntity.ok(resp);
        }

        /**
         * PUSH: apila un nuevo estado en el historial del ticket.
         * Body: {"estado": "EN_PROCESO"}
         */
        @PostMapping("/pila/{ticketId}")
        public ResponseEntity<EstructuraResponseDTO> pushEstado(
                        @PathVariable Long ticketId,
                        @RequestBody Map<String, String> body) {
                String estado = body.getOrDefault("estado", "").toUpperCase();
                if (estado.isBlank()) {
                        return ResponseEntity.badRequest().build();
                }
                log.info("POST /api/estructuras/pila/{} - push estado: {}", ticketId, estado);
                List<String> historial = estructurasService.pushEstado(ticketId, estado);

                EstructuraResponseDTO resp = new EstructuraResponseDTO(
                                "Pila (Stack - LIFO)",
                                "push",
                                "O(1)",
                                "Estado '" + estado + "' apilado en el tope. El tope anterior queda debajo.",
                                historial.size(),
                                null);
                resp.setHistorial(historial);
                return ResponseEntity.ok(resp);
        }

        /**
         * POP: retira el estado del tope (deshacer el último cambio).
         */
        @DeleteMapping("/pila/{ticketId}")
        public ResponseEntity<EstructuraResponseDTO> popEstado(@PathVariable Long ticketId) {
                log.info("DELETE /api/estructuras/pila/{} - pop", ticketId);
                String estadoRetirado = estructurasService.popEstado(ticketId);
                if (estadoRetirado == null) {
                        return ResponseEntity.noContent().build();
                }

                EstructuraResponseDTO resp = new EstructuraResponseDTO(
                                "Pila (Stack - LIFO)",
                                "pop",
                                "O(1)",
                                "Estado '" + estadoRetirado + "' retirado del tope (deshecho).",
                                0,
                                estadoRetirado);
                return ResponseEntity.ok(resp);
        }

        /**
         * Muestra la cola de tickets abiertos en orden de llegada (FIFO).
         */
        @GetMapping("/cola")
        public ResponseEntity<EstructuraResponseDTO> verCola() {
                log.info("GET /api/estructuras/cola");
                ColaTickets cola = estructurasService.construirCola();
                List<Ticket> tickets = cola.obtenerCola();
                Ticket frente = cola.verFrente();

                EstructuraResponseDTO resp = new EstructuraResponseDTO(
                                "Cola (Queue - FIFO)",
                                "verCola",
                                "O(n)",
                                "Cola de " + cola.getTamanio() + " tickets abiertos. Próximo a atender: " +
                                                (frente != null ? "ID=" + frente.getId() + " - " + frente.getTitulo()
                                                                : "ninguno"),
                                cola.getTamanio(),
                                tickets);
                return ResponseEntity.ok(resp);
        }

        /**
         * DESENCOLAR: atiende el primer ticket de la cola (retira del frente).
         */
        @DeleteMapping("/cola/atender")
        public ResponseEntity<EstructuraResponseDTO> atenderTicket() {
                log.info("DELETE /api/estructuras/cola/atender - desencolar");
                ColaTickets cola = estructurasService.construirCola();
                Ticket atendido = cola.desencolar();

                if (atendido == null) {
                        return ResponseEntity.noContent().build();
                }
                EstructuraResponseDTO resp = new EstructuraResponseDTO(
                                "Cola (Queue - FIFO)",
                                "desencolar",
                                "O(1)",
                                "Ticket ID=" + atendido.getId() + " atendido y removido del frente de la cola.",
                                0,
                                atendido);
                return ResponseEntity.ok(resp);
        }

        /**
         * Devuelve todos los tickets en INORDEN (ordenados por ID ascendente).
         */
        @GetMapping("/bst")
        public ResponseEntity<EstructuraResponseDTO> obtenerBSTInorden() {
                log.info("GET /api/estructuras/bst - recorrido inorden");
                ArbolBSTTickets arbol = estructurasService.construirBST();
                List<Ticket> tickets = arbol.inorden();

                EstructuraResponseDTO resp = new EstructuraResponseDTO(
                                "Árbol Binario de Búsqueda (BST)",
                                "recorridoInorden",
                                "O(n)",
                                "Recorrido inorden (izq→raíz→der): devuelve tickets ordenados por ID de menor a mayor.",
                                tickets.size(),
                                tickets);
                return ResponseEntity.ok(resp);
        }

        /**
         * Búsqueda O(log n) en el BST por ID de ticket.
         * Mucho más eficiente que la búsqueda lineal de la lista enlazada.
         */
        @GetMapping("/bst/buscar/{id}")
        public ResponseEntity<EstructuraResponseDTO> buscarEnBST(@PathVariable Long id) {
                log.info("GET /api/estructuras/bst/buscar/{}", id);
                Ticket ticket = estructurasService.buscarEnBST(id);

                EstructuraResponseDTO resp = new EstructuraResponseDTO(
                                "Árbol Binario de Búsqueda (BST)",
                                "buscarPorId",
                                "O(log n)",
                                ticket != null
                                                ? "Ticket ID=" + id
                                                                + " encontrado usando búsqueda binaria (descarta mitad del árbol en cada paso)."
                                                : "Ticket ID=" + id + " no existe en el árbol.",
                                1,
                                ticket);
                return ticket != null ? ResponseEntity.ok(resp) : ResponseEntity.notFound().build();
        }

        /**
         * Devuelve todos los usuarios indexados en la tabla hash (con info de cubeta).
         */
        @GetMapping("/hash")
        public ResponseEntity<EstructuraResponseDTO> obtenerHash() {
                log.info("GET /api/estructuras/hash");
                TablaHashUsuarios tabla = estructurasService.construirTablaHash();
                List<TablaHashUsuarios.UsuarioHashInfo> entradas = tabla.obtenerTodos();

                EstructuraResponseDTO resp = new EstructuraResponseDTO(
                                "Tabla Hash",
                                "obtenerTodos",
                                "O(capacidad + n)",
                                "Usuarios distribuidos en " + tabla.getCapacidad() + " cubetas. " +
                                                "Cada entrada muestra el índice de cubeta calculado por hash(email) % "
                                                + tabla.getCapacidad(),
                                tabla.getTamanio(),
                                entradas);
                return ResponseEntity.ok(resp);
        }

        /**
         * Búsqueda O(1) promedio por email en la tabla hash.
         */
        @GetMapping("/hash/buscar")
        public ResponseEntity<EstructuraResponseDTO> buscarEnHash(@RequestParam String email) {
                log.info("GET /api/estructuras/hash/buscar?email={}", email);
                Usuario usuario = estructurasService.buscarEnHash(email);

                EstructuraResponseDTO resp = new EstructuraResponseDTO(
                                "Tabla Hash",
                                "buscarPorEmail",
                                "O(1) promedio",
                                usuario != null
                                                ? "Usuario '" + email + "' encontrado directamente en su cubeta (hash="
                                                                + Math.abs(email.hashCode()) % 16 + ")."
                                                : "Email '" + email + "' no encontrado en la tabla hash.",
                                1,
                                usuario);
                return usuario != null ? ResponseEntity.ok(resp) : ResponseEntity.notFound().build();
        }
}
