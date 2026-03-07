package com.ticket.datastructures;

import com.ticket.model.Usuario;
import java.util.ArrayList;
import java.util.List;

public class TablaHashUsuarios {

    // Nodo interno de la cubeta (encadenamiento por colisiones)
    private static class EntradaHash {
        String email;
        Usuario usuario;
        EntradaHash siguiente; // para resolver colisiones

        EntradaHash(String email, Usuario usuario) {
            this.email = email;
            this.usuario = usuario;
            this.siguiente = null;
        }
    }

    private static final int CAPACIDAD_DEFAULT = 16; // número de cubetas
    private EntradaHash[] cubetas;
    private int tamanio;

    public TablaHashUsuarios() {
        this(CAPACIDAD_DEFAULT);
    }

    public TablaHashUsuarios(int capacidad) {
        this.cubetas = new EntradaHash[capacidad];
        this.tamanio = 0;
    }

    /**
     * FUNCIÓN HASH: convierte el email en un índice de cubeta.
     * Se usa hashCode() de Java y módulo para limitar al rango [0, capacidad-1].
     */
    private int hash(String email) {
        return Math.abs(email.hashCode()) % cubetas.length;
    }

    /**
     * INSERTAR: guarda un usuario indexado por su email — O(1) promedio.
     * Si ya existe el email, actualiza el usuario (upsert).
     */
    public void insertar(Usuario usuario) {
        String email = usuario.getEmail();
        int indice = hash(email);

        EntradaHash actual = cubetas[indice];
        // verificar si ya existe el email en la cubeta (encadenamiento)
        while (actual != null) {
            if (actual.email.equals(email)) {
                actual.usuario = usuario; // actualizar si ya existe
                return;
            }
            actual = actual.siguiente;
        }

        // no existe: insertar al inicio de la cubeta
        EntradaHash nueva = new EntradaHash(email, usuario);
        nueva.siguiente = cubetas[indice]; // encadenar con los existentes
        cubetas[indice] = nueva;
        tamanio++;
    }

    /**
     * BUSCAR: recupera un usuario por email — O(1) promedio.
     * Solo visita los nodos de UNA cubeta, no todos los usuarios.
     */
    public Usuario buscar(String email) {
        int indice = hash(email);
        EntradaHash actual = cubetas[indice];
        while (actual != null) {
            if (actual.email.equals(email)) {
                return actual.usuario; // encontrado
            }
            actual = actual.siguiente;
        }
        return null; // no encontrado
    }

    /**
     * ELIMINAR: remueve un usuario por email de su cubeta.
     */
    public boolean eliminar(String email) {
        int indice = hash(email);
        EntradaHash actual = cubetas[indice];
        EntradaHash anterior = null;

        while (actual != null) {
            if (actual.email.equals(email)) {
                if (anterior == null) {
                    cubetas[indice] = actual.siguiente; // era el primero de la cubeta
                } else {
                    anterior.siguiente = actual.siguiente; // saltar el nodo eliminado
                }
                tamanio--;
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
        return false;
    }

    /**
     * Devuelve todos los usuarios almacenados en la tabla.
     * Recorre las cubetas (O(capacidad + tamaño)).
     */
    public List<UsuarioHashInfo> obtenerTodos() {
        List<UsuarioHashInfo> resultado = new ArrayList<>();
        for (int i = 0; i < cubetas.length; i++) {
            EntradaHash actual = cubetas[i];
            while (actual != null) {
                resultado.add(new UsuarioHashInfo(i, actual.email, actual.usuario));
                actual = actual.siguiente;
            }
        }
        return resultado;
    }

    /**
     * DTO interno para devolver info de la cubeta junto al usuario.
     */
    public static class UsuarioHashInfo {
        public int indiceCubeta;
        public String email;
        public Usuario usuario;

        public UsuarioHashInfo(int indiceCubeta, String email, Usuario usuario) {
            this.indiceCubeta = indiceCubeta;
            this.email = email;
            this.usuario = usuario;
        }
    }

    public int getTamanio() {
        return tamanio;
    }

    public int getCapacidad() {
        return cubetas.length;
    }
}
