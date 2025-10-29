
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
        assertEquals("No hay medicamentos registrados.", retorno.getValorString());
    }

    @Test
    void listarMedicamentoUnoSolo() {
        // Registrar un solo medicamento
        s.registrarMedicamento("COD01", "Medicamento01", "2026-09-01", Categoria.VENTA_LIBRE);

        // Llamar al metodo que lista los medicamentos
        retorno = s.listarMedicamentosPorCodigoAscendente();

        // Verificar que el resultado sea OK
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());

        // Verificar que el string devuelto sea exactamente igual al esperado
        String esperado = "COD01;Medicamento01;2026-09-01;VENTA_LIBRE";
        assertEquals(esperado, retorno.getValorString());
    }


    @Test
    void listarMedicamentoIngresoOrdenado() {
        // Registrar medicamentos ya en orden ascendente
        s.registrarMedicamento("COD01", "Medicamento01", "2026-09-01", Categoria.VENTA_LIBRE);
        s.registrarMedicamento("COD02", "Medicamento02", "2026-09-02", Categoria.VENTA_LIBRE);
        s.registrarMedicamento("COD03", "Medicamento03", "2026-09-03", Categoria.VENTA_LIBRE);

        // Llamar al metodo
        retorno = s.listarMedicamentosPorCodigoAscendente();

        // Verificaciones
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("COD01;Medicamento01;2026-09-01;VENTA_LIBRE|COD02;Medicamento02;2026-09-02;VENTA_LIBRE|COD03;Medicamento03;2026-09-03;VENTA_LIBRE",
                retorno.getValorString());
    }


    @Test
    void ListarMedicamentoIngresoNoOrdenado() {
        // insertar medicamentos de forma desordenada
        s.registrarMedicamento("COD03", "Medicamento03", "2026-09-03", Categoria.VENTA_LIBRE);
        s.registrarMedicamento("COD01", "Medicamento01", "2026-09-01", Categoria.VENTA_LIBRE);
        s.registrarMedicamento("COD02", "Medicamento02", "2026-09-02", Categoria.VENTA_LIBRE);
        retorno = s.listarMedicamentosPorCodigoAscendente();
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
        assertEquals("COD01;Medicamento01;2026-09-01;VENTA_LIBRE|COD02;Medicamento02;2026-09-02;VENTA_LIBRE|COD03;Medicamento03;2026-09-03;VENTA_LIBRE",
                retorno.getValorString());
    }

}
