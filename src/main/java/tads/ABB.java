package tads;

import dominio.Medicamento;
import interfaz.Categoria;

import java.util.Comparator;
import java.util.List;

public class ABB<T extends Comparable<T>> {

    private Nodo raiz;
    private Comparator<T> comparator;

    public ABB(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    private class Nodo {
        T valor;
        Nodo izquierdo, derecho;
        Nodo(T valor){this.valor = valor;}
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

        int cmp = comparator.compare(valor, nodo.valor);

        if(cmp < 0) nodo.izquierdo = insertarRec(nodo.izquierdo, valor);
        else if (cmp > 0) nodo.derecho = insertarRec(nodo.derecho, valor);

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

        int cmp = comparator.compare(valor, nodo.valor);

        if(cmp == 0) return true;
        else if(cmp > 0) return buscarRec(nodo.izquierdo, valor);
        else return buscarRec(nodo.derecho, valor);
    }

    // recorrido en orden (in-order traversal)
    public void recorrerInOrden() {
        recorrerInOrdenRec(raiz);
    }

    private void recorrerInOrdenRec(Nodo nodo) {
        if (nodo != null) {
            recorrerInOrdenRec(nodo.izquierdo);
            System.out.print(nodo.valor + " ");
            recorrerInOrdenRec(nodo.derecho);
        }
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
        int cmp = comparator.compare(valor, nodo.valor);
        if (cmp == 0) return nodo.valor;
        else if (cmp < 0) return buscarConRecorridosRec(nodo.izquierdo, valor, contador);
        else return buscarConRecorridosRec(nodo.derecho, valor, contador);
    }


    // Metodo de recorrido inOrder que devuelve los elementos de la lista en orden ascendente por codigo
    public void listarInOrden(List<Medicamento> lista){
        listarInOrdenRec(raiz, lista);
    }

    private void listarInOrdenRec(Nodo nodo, List<Medicamento> lista){
        if(nodo == null) return;
        listarInOrdenRec(nodo.izquierdo, lista);
        lista.add((Medicamento) nodo.valor);
        listarInOrdenRec(nodo.derecho, lista);
    }


    //Metodo de recorrido inOrderDesc que devuelve los elementos de la lista en orden ascendente pos codigo
    public void listarInOrdenDesc(List<Medicamento> lista){
        listarInOrdenDescRec(raiz, lista);
    }

    private void listarInOrdenDescRec(Nodo nodo, List<Medicamento> lista){
        if(nodo == null) return;
        listarInOrdenDescRec(nodo.derecho, lista); // primero derecho, donde van los mayores
        lista.add((Medicamento) nodo.valor);
        listarInOrdenDescRec(nodo.izquierdo, lista);
    }


    //Metodo para listar por categoria recorriendo el arbol en orden creciente
    public void listarPorCategoria(Categoria categoria, List<Medicamento> lista){
        listarPorCategoriaRec(raiz, categoria, lista);
    }

    private void listarPorCategoriaRec(Nodo nodo, Categoria categoria, List<Medicamento> lista){
        if(nodo == null) return;

        listarPorCategoriaRec(nodo.izquierdo, categoria, lista);

        Medicamento med = (Medicamento) nodo.valor;

        if(med.getCategoria().equals(categoria)) lista.add(med);

        listarPorCategoriaRec(nodo.derecho, categoria, lista);

    }








}
