/**
 * Aca es donde va su implementación, nada les impide crear sus propios objetos en otros paquetes como tads
 */
package sistema;
import dominio.Medicamento;
import dominio.Medicamento.*;
import interfaz.Categoria;
import interfaz.Retorno;

public class Main {

    public static void main(String[] args) {

        // Crear una instancia del sistema
        ImplementacionSistema sistema = new ImplementacionSistema();

        // Inicializar el sistema con un máximo de 5 farmacias (puedes cambiar este número)
        Retorno retorno = sistema.inicializarSistema(5);
        System.out.println(retorno.getValorString());

        // Registrar algunos medicamentos
        // Medicamento 1
        Retorno retorno1 = sistema.registrarMedicamento("12345", "Paracetamol", "2025-12-31", Categoria.VENTA_LIBRE);
        System.out.println(retorno1.getValorString());

        // pruebas que estan en la letra del obligatorio
        /*Retorno retornoPrueba = sistema.registrarMedicamento("54321","virexan","2026-09-09",Categoria.VENTA_LIBRE);
        System.out.println(retornoPrueba.getValorString());
        Retorno retornoPrueba2 = sistema.registrarMedicamento("","virexan","2026-09-09",Categoria.VENTA_LIBRE);
        System.out.println(retornoPrueba2.getValorString());
        Retorno retornoPrueba3 = sistema.registrarMedicamento("23154","virexan","26-09-09",Categoria.VENTA_LIBRE);
        System.out.println(retornoPrueba3.getValorString());*/

        // Medicamento 2 (con código duplicado)
        Retorno retorno2 = sistema.registrarMedicamento("12345", "Ibuprofeno", "2026-05-15", Categoria.RECETA_COMUN);
        System.out.println(retorno2.getValorString()); // Debería dar un error por código duplicado

        // Medicamento 3 (con nombre duplicado)
        Retorno retorno3 = sistema.registrarMedicamento("67890", "Paracetamol", "2027-07-20", Categoria.RECETA_COMUN);
        System.out.println(retorno3.getValorString()); // Debería dar un error por nombre duplicado

        // Medicamento 4 (correcto)
        Retorno retorno4 = sistema.registrarMedicamento("98765", "Aspirina", "2028-09-01", Categoria.RECETA_CONTROLADA);
        System.out.println(retorno4.getValorString());

        // Ahora intentar registrar un medicamento con una fecha incorrecta
        Retorno retorno5 = sistema.registrarMedicamento("11223", "Amoxicilina", "2023-32-01", Categoria.VENTA_LIBRE);
        System.out.println(retorno5.getValorString()); // Debería dar error de fecha

        Medicamento med = new Medicamento("12345", "Paracetamol", "2025-12-31", Categoria.VENTA_LIBRE);
        // Buscar medicamentos por codigo (existente y no existente)
        boolean encontrado1 = sistema.getArbolMedicamentos().buscar(med);
        System.out.println("¿Medicamento con código 12345 encontrado? " + encontrado1); // True

        boolean encontrado2 = sistema.getArbolMedicamentos().buscar(new Medicamento("00000", "Paracetamol", "2025-12-31", Categoria.VENTA_LIBRE));
        System.out.println("¿Medicamento con código 00000 encontrado? " + encontrado2); // False

        // Buscar medicamentos por nombre (existente y no existente)
        boolean encontrado3 = sistema.getArbolMedicamentos().buscarNombre("Paracetamol");
        System.out.println("¿Medicamento con nombre 'Paracetamol' encontrado? " + encontrado3); // True

        boolean encontrado4 = sistema.getArbolMedicamentos().buscarNombre("Penicilina");
        System.out.println("¿Medicamento con nombre 'Penicilina' encontrado? " + encontrado4); // False*/


    }
}




