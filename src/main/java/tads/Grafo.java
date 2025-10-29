package tads;

import dominio.Conexion;
import java.util.HashMap;
import java.util.Map;

public class Grafo<T> {
    private int maxVertices;
    private Map<T, Lista<Conexion<T>>> adyacencias;

    public Grafo(int maxVertices) {
        this.maxVertices = maxVertices;
        this.adyacencias = new HashMap<>();
    }

    // Agrega un vértice al grafo
    public boolean agregarVertice(T v) {
        if (adyacencias.size() >= maxVertices) return false;
        if (adyacencias.containsKey(v)) return false;
        adyacencias.put(v, new Lista<>());
        return true;
    }

    // Verifica si existe un vértice
    public boolean existeVertice(T v) {
        return adyacencias.containsKey(v);
    }

    // Verifica si existe una arista entre un vértice origen y un código de destino
    public boolean existeArista(T origen, String codigoDestino) {
        if (!existeVertice(origen)) return false;

        Lista<Conexion<T>> lista = adyacencias.get(origen);
        Nodo<Conexion<T>> actual = lista.getInicio();

        while (actual != null) {
            if (actual.getDato().getCodigoDestino().equals(codigoDestino)) {
                return true;
            }
            actual = actual.getSiguiente();
        }

        return false;
    }

    // Agrega una arista entre dos vértices (grafo no dirigido)
    public boolean agregarArista(String codigoOrigen, String codigoDestino, int tiempo, int distancia, int costo) {
        if (!existeVertice((T) codigoOrigen) || !existeVertice((T) codigoDestino)) return false;
        if (existeArista((T) codigoOrigen, codigoDestino)) return false;

        Conexion<T> conexion1 = new Conexion<>(codigoOrigen, codigoDestino, tiempo, distancia, costo);
        Conexion<T> conexion2 = new Conexion<>(codigoDestino, codigoOrigen, tiempo, distancia, costo);

        adyacencias.get((T) codigoOrigen).agregarInicio(conexion1);
        adyacencias.get((T) codigoDestino).agregarInicio(conexion2);

        return true;
    }

    // Devuelve la lista de conexiones (adyacentes) de un vértice
    public Lista<Conexion<T>> obtenerAdyacentes(T v) {
        return adyacencias.get(v);
    }

    // Devuelve la cantidad de vértices del grafo
    public int cantidadVertices() {
        return adyacencias.size();
    }
}
