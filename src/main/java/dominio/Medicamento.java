package dominio;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import interfaz.Categoria;

public class Medicamento implements Comparable<Medicamento> {

    private String codigo;
    private String nombre;
    private String fechaVencimiento;
    private Categoria categoria;



    public Medicamento(String codigo, String nombre, String fechaVencimiento,Categoria categoria) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.fechaVencimiento = fechaVencimiento;
        this.categoria = categoria;
    }





    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public Categoria getCategoria() {
        return categoria;
    }


    public static boolean esCodigoValido(String codigo){
        return codigo != null && !codigo.isEmpty(); // Devuelve true si el codigo es distinto de null y no es vacio
    }


    public static boolean esNombreValido(String nombre){
        return nombre != null && !nombre.isEmpty(); // Nombre distinto de null y no es vacio devuelve true
    }

    // metodo para validar la fecha con expresion irregular

    public static boolean esFechaValida(String fecha) {
        String regex = "^(?:19|20)\\d\\d-(?:0[1-9]|1[0-2])-(?:0[1-9]|[12][0-9]|3[01])$";  // Formato: AAAA-MM-DD
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(fecha);
        return matcher.matches();
    }


    @Override
    public int compareTo(Medicamento o) {
        Integer codigo1 = Integer.parseInt(this.codigo);
        Integer codigo2 = Integer.parseInt(o.codigo);
        return codigo1.compareTo(codigo2);
    }



}
