package tads;

public class Cola<T> {
    private T[] elementos;
    private int frente;
    private int fin;
    private int capacidad;

    public Cola(int capacidad) {
        this.capacidad = capacidad;
        this.elementos = (T[]) new Object[capacidad];
        this.frente = 0;
        this.fin = 0;
    }

    //Verificar si cola esta vacia
    public boolean isEmpty() {
        return frente == fin;
    }

    //Verificar si la cola esta llena
    public boolean isFull(){
        return fin == capacidad;
    }

    //Aniadir elemento al final de la cola
    public void push(T elemento) {
        if(isFull()){
            throw new RuntimeException("Cola llena");
        }
        elementos[fin++] = elemento;
    }

    //Sacar elemento del frente de la cola
    public T pop(){
        if(isEmpty()){
            throw new RuntimeException("Cola vacia");
        }
        return elementos[frente++];
    }

    //Obtener elemento el frente sin quitarlo
    public T frente(){
        if(isEmpty()){
            throw new RuntimeException("Cola vacia");
        }
        return elementos[frente++];
    }

    //Limpiar la cola
    public void limpiar(){
        frente = 0;
        fin = 0;
    }

    //Tamanio actual de la cola
    public int tamanio(){
        return fin - frente;
    }
}
