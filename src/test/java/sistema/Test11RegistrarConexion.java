package sistema;

import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test11RegistrarConexion {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(4);
    }

    @Test
    public void registrarConexionOk(){
        s.registrarFarmacia("FA01", "Farmacia Uno");
        s.registrarFarmacia("FA02", "Farmacia Dos");
        retorno = s.registrarConexion("FA01", "FA02", 30, 15, 300);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test void registrarConexionError1(){
        s.registrarFarmacia("FA01", "Farmacia Uno");
        s.registrarFarmacia("FA02", "Farmacia Dos");
        retorno = s.registrarConexion("FA01", "", 30, 15, 300);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test void registrarConexionError2(){
        s.registrarFarmacia("FA01", "Farmacia Uno");
        retorno = s.registrarConexion("FA02", "FA01", 30, 15, 300);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test void registrarConexionError3(){
        s.registrarFarmacia("FA01", "Farmacia Uno");
        retorno = s.registrarConexion("FA01", "FA02", 30, 15, 300);
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

    @Test void registrarConexionError4(){
        s.registrarFarmacia("FA01", "Farmacia Uno");
        s.registrarFarmacia("FA02", "Farmacia Dos");
        retorno = s.registrarConexion("FA01", "FA02", 0, 15, 300);
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
    }

    @Test void registrarConexionError5(){
        s.registrarFarmacia("FA01", "Farmacia Uno");
        s.registrarFarmacia("FA02", "Farmacia Dos");
        s.registrarConexion("FA01", "FA02", 20, 10, 200);
        retorno = s.registrarConexion("FA01", "FA02", 15, 15, 300);
        assertEquals(Retorno.Resultado.ERROR_5, retorno.getResultado());
    }
}
