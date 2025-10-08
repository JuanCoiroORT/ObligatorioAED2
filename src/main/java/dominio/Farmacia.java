package dominio;

public class Farmacia implements Comparable<Farmacia> {

    private String codigo;

    private String nombre;



    public Farmacia(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public int compareTo(Farmacia o) {
        return this.codigo.compareTo(o.getCodigo());
    }
}
