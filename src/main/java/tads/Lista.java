package tads;

public class Lista<T> {
    private Nodo<T> inicio;
    private int largo;

    public Lista() {
        inicio = null;
        largo = 0;
    }

    public Nodo<T> getInicio() {
        return inicio;
    }

    public int  getLargo() {
        return largo;
    }

    public boolean esVacia() {
        return inicio == null;
    }

    public int largo() {
        return largo;
    }

    public void agregarInicio(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.setSiguiente(inicio);
        inicio = nuevo;
        largo++;
    }

    public void agregarFinal(T dato) {
        Nodo<T> nuevo = new Nodo<>(dato);
        if (esVacia()) {
            inicio = nuevo;
        } else {
            Nodo<T> aux = inicio;
            while (aux.getSiguiente() != null) {
                aux = aux.getSiguiente();
            }
            aux.setSiguiente(nuevo);
        }
        largo++;
    }

    public boolean existe(T dato) {
        Nodo<T> aux = inicio;
        while (aux != null) {
            if (aux.getDato().equals(dato)) return true;
            aux = aux.getSiguiente();
        }
        return false;
    }

    public void eliminarDato(T dato) {
        if (inicio == null) return;

        // dato esta en inicio
        if (inicio.getDato().equals(dato)) {
            inicio = inicio.getSiguiente();
            return;
        }

        Nodo<T> actual = inicio;
        while (actual.getSiguiente()!= null && !actual.getSiguiente().getDato().equals(dato)) {
            actual = actual.getSiguiente();
        }

        if (actual.getSiguiente() != null) {
            actual.setSiguiente(actual.getSiguiente().getSiguiente());
        }
    }
}
