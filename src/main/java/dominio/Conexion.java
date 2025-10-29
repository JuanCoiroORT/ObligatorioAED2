package dominio;

public class Conexion<T> {

    private String codigoOrigen;
    private String codigoDestino;
    private int tiempo;     // en minutos
    private int distancia;  // en metros
    private int costo;      // en pesos

    public Conexion(String codigoOrigen, String codigoDestino, int tiempo, int distancia, int costo) {
        this.codigoOrigen = codigoOrigen;
        this.codigoDestino = codigoDestino;
        this.tiempo = tiempo;
        this.distancia = distancia;
        this.costo = costo;
    }

    public String getCodigoOrigen() {
        return codigoOrigen;
    }

    public void setCodigoOrigen(String codigoOrigen) {
        this.codigoOrigen = codigoOrigen;
    }

    public String getCodigoDestino() {
        return codigoDestino;
    }

    public void setCodigoDestino(String codigoDestino) {
        this.codigoDestino = codigoDestino;
    }

    public int getTiempo() {
        return tiempo;
    }

    public void setTiempo(int tiempo) {
        this.tiempo = tiempo;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    @Override
    public String toString() {
        return "Conexión [Origen: " + codigoOrigen +
                ", Destino: " + codigoDestino +
                ", Tiempo: " + tiempo + " mins" +
                ", Distancia: " + distancia + " m" +
                ", Costo: " + costo + " pesos]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Conexion<?> conexion = (Conexion<?>) obj;
        return codigoOrigen.equals(conexion.codigoOrigen)
                && codigoDestino.equals(conexion.codigoDestino);
    }

    @Override
    public int hashCode() {
        return 31 * codigoOrigen.hashCode() + codigoDestino.hashCode();
    }
}
