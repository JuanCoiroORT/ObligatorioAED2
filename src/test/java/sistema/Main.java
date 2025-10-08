package sistema;



import sistema.ImplementacionSistema.*;
import interfaz.Sistema;
import interfaz.Retorno;

public class Main {
    public static void main(String[] args) {
        // Crear una instancia del sistema
        Sistema sistema = new ImplementacionSistema();

        // Prueba de la funcionalidad
        Retorno retorno = sistema.inicializarSistema(4);
        System.out.println("Inicializar sistema: " + retorno.toString());

        retorno = sistema.registrarFarmacia("FARM01", "Farmacia Central");
        System.out.println("Registrar farmacia: " + retorno.toString());
    }
}
