
package sistema;

import interfaz.Categoria;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test03BuscarMedicamentoPorCodigo {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
    }

    @Test
    void buscarMedicamentoOk() {

        s.registrarMedicamento("COD01", "Medicamento01", "2026-09-01", Categoria.VENTA_LIBRE);
        retorno = s.buscarMedicamentoPorCodigo("COD01");
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    void buscarMedicamentoERROR_1() {

        s.registrarMedicamento("COD01", "Medicamento01", "2026-09-01", Categoria.VENTA_LIBRE);
        retorno = s.buscarMedicamentoPorCodigo("");
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    void buscarMedicamentoERROR_2() {

        s.registrarMedicamento("COD01", "Medicamento01", "2026-09-01", Categoria.VENTA_LIBRE);
        retorno = s.buscarMedicamentoPorCodigo("COD02");
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }



}
