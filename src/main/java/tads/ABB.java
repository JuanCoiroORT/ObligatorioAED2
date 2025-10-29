package tads;

import dominio.Medicamento;
import interfaz.Categoria;

import java.util.Comparator;

public class ABB<T extends Comparable<T>> {

    private Nodo raiz;
    private Comparator<T> comparator;

    public ABB(Comparator<T> comparator) {
        this.comparator = comparator;
        this.raiz = null;
    }

    private class Nodo {
        private T dato;
        private Nodo izquierdo;
        private Nodo derecho;

        public Nodo(T dato) {
            this.dato = dato;
            this.izquierdo = null;
            this.derecho = null;
        }

        public T getDato() { return dato; }
        public Nodo getIzquierdo() { return izquierdo; }
        public Nodo getDerecho() { return derecho; }
        public void setIzquierdo(Nodo izquierdo) { this.izquierdo = izquierdo; }
        public void setDerecho(Nodo derecho) { this.derecho = derecho; }
    }

    public boolean isEmpty() {
        return raiz == null;
    }

    public void insertar(T valor) {
        raiz = insertarRec(raiz, valor);
    }

    private Nodo insertarRec(Nodo nodo, T valor) {
        if (nodo == null) return new Nodo(valor);

        int cmp = comparator.compare(valor, nodo.getDato());
        if (cmp < 0) nodo.setIzquierdo(insertarRec(nodo.getIzquierdo(), valor));
        else if (cmp > 0) nodo.setDerecho(insertarRec(nodo.getDerecho(), valor));

        return nodo;
    }

    public boolean buscar(T valor) {
        return buscarRec(raiz, valor);
    }

    private boolean buscarRec(Nodo nodo, T valor) {
        if (nodo == null) return false;

        int cmp = comparator.compare(valor, nodo.getDato());
        if (cmp == 0) return true;
        else if (cmp < 0) return buscarRec(nodo.getIzquierdo(), valor);
        else return buscarRec(nodo.getDerecho(), valor);
    }

    public static class Contador {
        private int valor = 0;
        public void incrementar() { valor++; }
        public int getValor() { return valor; }
    }

    public T buscarConRecorridos(T valor, Contador contador) {
        return buscarConRecorridosRec(raiz, valor, contador);
    }

    private T buscarConRecorridosRec(Nodo nodo, T valor, Contador contador) {
        if (nodo == null) return null;

        contador.incrementar();
        int cmp = comparator.compare(valor, nodo.getDato());

        if (cmp == 0) return (T) nodo.getDato();
        else if (cmp < 0) return buscarConRecorridosRec(nodo.getIzquierdo(), valor, contador);
        else return buscarConRecorridosRec(nodo.getDerecho(), valor, contador);
    }

    // =================== MÉTODOS CON tads.Lista ===================

    public void listarInOrden(Lista<Medicamento> lista){
        listarInOrdenRec(raiz, lista);
    }

    private void listarInOrdenRec(Nodo nodo, Lista<Medicamento> lista) {
        if (nodo == null) return;

        listarInOrdenRec(nodo.getIzquierdo(), lista);
        lista.agregarFinal((Medicamento) nodo.getDato());
        listarInOrdenRec(nodo.getDerecho(), lista);
    }

    public void listarInOrdenDesc(Lista<Medicamento> lista){
        listarInOrdenDescRec(raiz, lista);
    }

    private void listarInOrdenDescRec(Nodo nodo, Lista<Medicamento> lista) {
        if (nodo == null) return;

        listarInOrdenDescRec(nodo.getDerecho(), lista);
        lista.agregarFinal((Medicamento) nodo.getDato());
        listarInOrdenDescRec(nodo.getIzquierdo(), lista);
    }

    public void listarPorCategoria(Categoria categoria, Lista<Medicamento> lista){
        listarPorCategoriaRec(raiz, categoria, lista);
    }

    private void listarPorCategoriaRec(Nodo nodo, Categoria categoria, Lista<Medicamento> lista) {
        if (nodo == null) return;

        listarPorCategoriaRec(nodo.getIzquierdo(), categoria, lista);

        Medicamento med = (Medicamento) nodo.getDato();
        if (med.getCategoria().equals(categoria)) {
            lista.agregarFinal(med);
        }

        listarPorCategoriaRec(nodo.getDerecho(), categoria, lista);
    }
}
