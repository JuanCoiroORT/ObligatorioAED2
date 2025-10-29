package tads;

public class Nodo<T> {
    private T dato;
    private Nodo<T> siguiente;
    private Nodo<T> izquierdo;
    private Nodo<T> derecho;

    public Nodo(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public Nodo<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Nodo<T> siguiente) {
        this.siguiente = siguiente;
    }

    public Nodo<T> getIzquierdo() {
        return izquierdo;
    }

    public Nodo<T> getDerecho() {
        return derecho;
    }
    public void setIzquierdo(Nodo<T> izquierdo) {
        this.izquierdo = izquierdo;
    }

    public void setDerecho(Nodo<T> derecho) {
        this.derecho = derecho;
    }
}
