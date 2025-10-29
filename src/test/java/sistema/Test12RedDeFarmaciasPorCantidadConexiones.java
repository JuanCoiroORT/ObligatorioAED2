package sistema;

import interfaz.Retorno;
import interfaz.Sistema;
import org.junit.jupiter.api.BeforeEach;

public class Test12RedDeFarmaciasPorCantidadConexiones {
    private Retorno retorno;
    private final Sistema s = new ImplementacionSistema();

    @BeforeEach
    public void setUp() {
        s.inicializarSistema(4);
    }

    
}
