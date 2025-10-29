package sistema;

import interfaz.Categoria;
import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Test02RegistrarMedicamento {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(10);
    }

    @Test
    void registrarMedicamentoOk() {
        retorno = s.registrarMedicamento("COD01", "Medicamento01", "2026-09-01", Categoria.VENTA_LIBRE);
        assertEquals(Retorno.Resultado.OK, retorno.getResultado());
    }

    @Test
    void registrarMedicamentoError1() {
        retorno = s.registrarMedicamento("", "Medicamento01", "2026-09-01", Categoria.VENTA_LIBRE);
        assertEquals(Retorno.Resultado.ERROR_1, retorno.getResultado());
    }

    @Test
    void registrarMedicamentoError2() {
        retorno = s.registrarMedicamento("COD01", "Medicamento01", "01-09-2026", Categoria.VENTA_LIBRE);
        assertEquals(Retorno.Resultado.ERROR_2, retorno.getResultado());
    }

    @Test
    void registrarMedicamentoError3() {
        s.registrarMedicamento("COD01", "Medicamento01", "2026-09-01", Categoria.VENTA_LIBRE);
        retorno = s.registrarMedicamento("COD01", "Medicamento01", "2026-09-01", Categoria.VENTA_LIBRE);
        assertEquals(Retorno.Resultado.ERROR_3, retorno.getResultado());
    }

    @Test
    void registrarMedicamentoError4() {
        s.registrarMedicamento("COD01", "Medicamento01", "2026-09-01", Categoria.VENTA_LIBRE);
        retorno = s.registrarMedicamento("COD02", "Medicamento01", "2026-09-01", Categoria.VENTA_LIBRE);
        assertEquals(Retorno.Resultado.ERROR_4, retorno.getResultado());
    }

}
