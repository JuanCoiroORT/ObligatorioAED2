package tads;

import dominio.Medicamento;
import interfaz.Categoria;

import java.util.Comparator;
import java.util.List;

public class ABB<T extends Comparable<T>> {

    private Nodo raiz;
    private Comparator<T> comparator;
    private List<Medicamento> lista;

    public ABB(Comparator<T> comparator) {
        this.comparator = comparator;
        this.raiz = null;
    }


    //Metodo para validar que un arbol este vacio
    public boolean isEmpty(){
        return raiz == null;
    }

    // insertar un valor en el arbol
    public void insertar(T valor) {
        raiz = insertarRec(raiz, valor);
    }

    private Nodo insertarRec(Nodo nodo, T valor) {
        if (nodo == null) {
            return new Nodo(valor);
        }

        int cmp = comparator.compare(valor, (T) nodo.getDato());

        if(cmp < 0) {
            nodo.setIzquierdo(insertarRec(nodo.getIzquierdo(), valor));
        }
        else if (cmp > 0){
            nodo.setDerecho(insertarRec(nodo.getDerecho(), valor));
        }

        return nodo;
    }

    // buscar un valor en el arbol
    public boolean buscar(T valor) {
        return buscarRec(raiz, valor);
    }

    private boolean buscarRec(Nodo nodo, T valor) {
        if (nodo == null) {
            return false;
        }

        int cmp = comparator.compare(valor, (T) nodo.getDato());

        if(cmp == 0) return true;
        else if(cmp > 0) return buscarRec(nodo.getIzquierdo(), valor);
        else return buscarRec(nodo.getDerecho(), valor);
    }

    // recorrido en orden (in-order traversal)
    public void recorrerInOrden() {
        recorrerInOrdenRec(raiz);
    }

    private void recorrerInOrdenRec(Nodo nodo) {
        if (nodo != null) {
            recorrerInOrdenRec(nodo.getIzquierdo());
            System.out.print(nodo.getDato() + " ");
            recorrerInOrdenRec(nodo.getDerecho());
        }
    }



    public static class Contador {
        private int valor = 0;
        public void incrementar() { valor++; }
        public int getValor() { return valor; }
    }

    public T buscarConRecorridos(T valor, Contador contador) {
        return (T) buscarConRecorridosRec(raiz, valor, contador);
    }

    private T buscarConRecorridosRec(Nodo<T> nodo, T valor, Contador contador) {
        if (nodo == null) return null;

        contador.incrementar();
        int cmp = comparator.compare(valor, nodo.getDato());

        if (cmp == 0) return nodo.getDato();
        else if (cmp < 0) return buscarConRecorridosRec(nodo.getIzquierdo(), valor, contador);
        else return buscarConRecorridosRec(nodo.getDerecho(), valor, contador);
    }


    // Metodo de recorrido inOrder que devuelve los elementos de la lista en orden ascendente por codigo
    public void listarInOrden(List<Medicamento> lista){
        //this.lista = lista;
        listarInOrdenRec(raiz, (Lista<Medicamento>) lista);
    }

    private void listarInOrdenRec(Nodo<T> nodo, Lista<Medicamento> lista) {
        if (nodo == null) return;

        listarInOrdenRec(nodo.getIzquierdo(), lista);
        lista.agregarFinal((Medicamento) nodo.getDato());
        listarInOrdenRec(nodo.getDerecho(), lista);
    }



    //Metodo de recorrido inOrderDesc que devuelve los elementos de la lista en orden ascendente pos codigo
    public void listarInOrdenDesc(List<Medicamento> lista){
        listarInOrdenDescRec(raiz, (Lista<Medicamento>) lista);
    }

    private void listarInOrdenDescRec(Nodo<T> nodo, Lista<Medicamento> lista) {
        if (nodo == null) return;

        listarInOrdenDescRec(nodo.getDerecho(), lista);
        lista.agregarFinal((Medicamento) nodo.getDato());
        listarInOrdenDescRec(nodo.getIzquierdo(), lista);
    }



    //Metodo para listar por categoria recorriendo el arbol en orden creciente
    public void listarPorCategoria(Categoria categoria, List<Medicamento> lista){
        listarPorCategoriaRec(raiz, categoria, (Lista<Medicamento>) lista);
    }

    private void listarPorCategoriaRec(Nodo<T> nodo, Categoria categoria, Lista<Medicamento> lista) {
        if (nodo == null) return;

        listarPorCategoriaRec(nodo.getIzquierdo(), categoria, lista);

        Medicamento med = (Medicamento) nodo.getDato();
        if (med.getCategoria().equals(categoria)) {
            lista.agregarFinal(med);
        }

        listarPorCategoriaRec(nodo.getDerecho(), categoria, lista);
    }









}
