package tads;

import java.util.HashMap;
import java.util.Map;

public class Grafo<T> {
    private int maxVertices;
    private Map<T, Lista<Arista<T>>> adyacencias;

    public Grafo(int maxVertices) {
        this.maxVertices = maxVertices;
        this.adyacencias = new HashMap<>();
    }

    public boolean agregarVertice(T v) {
        if (adyacencias.size() >= maxVertices) return false;
        if (adyacencias.containsKey(v)) return false;
        adyacencias.put(v, new Lista<>());
        return true;
    }

    public boolean existeVertice(T v) {
        return adyacencias.containsKey(v);
    }

    public boolean existeArista(T origen, T destino) {
        if (!existeVertice(origen) || !existeVertice(destino)) return false;

        Lista<Arista<T>> lista = adyacencias.get(origen);

        Nodo<Arista<T>> actual = lista.getInicio();
        while (actual != null) {
            if (actual.getDato().getDestino().equals(destino)) {
                return true;
            }
            actual = actual.getSiguiente();
        }

        return false;
    }

    public boolean agregarArista(T origen, T destino, int tiempo, int distancia, int costo) {
        if (existeArista(origen, destino)) return false;

        adyacencias.get(origen).agregarInicio(new Arista<>(destino, tiempo, distancia, costo));
        adyacencias.get(destino).agregarInicio(new Arista<>(origen, tiempo, distancia, costo));

        return true;
    }

    public Lista<Arista<T>> obtenerAdyacentes(T v) {
        return adyacencias.get(v);
    }

    public int cantidadVertices() {
        return adyacencias.size();
    }
}
