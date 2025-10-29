package sistema;

import interfaz.Categoria;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test10RegistrarFarmacia {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(4);
    }

    @Test
    void registrarFarmaciaOk() {
        retorno = s.registrarFarmacia("FA01", "Farmacia Salinas");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    void registrarFarmaciaERROR1() {
        s.registrarFarmacia("FA01", "Farmacia Salinas");
        s.registrarFarmacia("FA02", "Farmacia Tuyuti");
        s.registrarFarmacia("FA03", "Farmacia El Pinar");
        s.registrarFarmacia("FA04", "Farmacia Rio Negro");
        retorno = s.registrarFarmacia("FA05", "Farmacia Carlitos");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    void registrarFarmaciaERROR2() {
        retorno = s.registrarFarmacia("", "Farmacia Salinas");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    void registrarFarmaciaERROR3() {
        s.registrarFarmacia("FA01", "Farmacia Salinas");
        retorno = s.registrarFarmacia("FA01", "Farmacia Tuyuti");
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

}
