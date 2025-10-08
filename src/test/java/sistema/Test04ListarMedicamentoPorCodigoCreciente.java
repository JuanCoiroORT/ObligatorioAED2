
package sistema;

import interfaz.Categoria;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test04ListarMedicamentoPorCodigoCreciente {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
    }

    @Test
    void ListarMedicamentoVacio() {
        retorno = s.listarMedicamentosPorCodigoAscendente();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("", retorno.getValorString());
    }

    @Test
    void ListarMedicamentoUnoSolo() {
        s.registrarMedicamento("COD01", "Medicamento01", "2026-09-01", Categoria.VENTA_LIBRE);
        retorno = s.listarMedicamentosPorCodigoAscendente();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(Salidas.MED01, retorno.getValorString());
    }

    @Test
    void ListarMedicamentoIngresoOrdenado() {
        s.registrarMedicamento("COD01", "Medicamento01", "2026-09-01", Categoria.VENTA_LIBRE);
        s.registrarMedicamento("COD02", "Medicamento02", "2026-09-02", Categoria.VENTA_LIBRE);
        s.registrarMedicamento("COD03", "Medicamento03", "2026-09-03", Categoria.VENTA_LIBRE);
        retorno = s.listarMedicamentosPorCodigoAscendente();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(Salidas.MED01 + "|" + Salidas.MED02 + "|" + Salidas.MED03, retorno.getValorString());
    }

    @Test
    void ListarMedicamentoIngresoNoOrdenado() {
        s.registrarMedicamento("COD03", "Medicamento03", "2026-09-03", Categoria.VENTA_LIBRE);
        s.registrarMedicamento("COD01", "Medicamento01", "2026-09-01", Categoria.VENTA_LIBRE);
        s.registrarMedicamento("COD02", "Medicamento02", "2026-09-02", Categoria.VENTA_LIBRE);
        retorno = s.listarMedicamentosPorCodigoAscendente();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals(Salidas.MED01 + "|" + Salidas.MED02 + "|" + Salidas.MED03, retorno.getValorString());
    }

}
