package interfaz;

/**
 * Provee una interfaz para interactuar con el sistema
 */
public interface Sistema {


    Retorno inicializarSistema(int maxFarmacias);
    Retorno registrarMedicamento(String codigo, String nombre, String fechaVencimiento, Categoria categoria);
    Retorno buscarMedicamentoPorCodigo(String codigo);
    Retorno listarMedicamentosPorCodigoAscendente();
    Retorno listarMedicamentosPorCodigoDescendente();
    Retorno buscarMedicamentoPorNombre(String nombre);
    Retorno listarMedicamentosPorNombreAscendente();
    Retorno listarMedicamentosPorNombreDescendente();
    Retorno listarMedicamentosPorCategoría(Categoria unaCategoria);
    Retorno registrarFarmacia(String codigo, String nombre);
    Retorno registrarConexion(String codigoOrigen, String codigoDestino, int tiempo, int distancia, int costo);
    Retorno redFarmaciasPorCantidadDeConexiones(String codigoOrigen, int cantidad);
    Retorno analizarFarmacia(String codigoOrigen);
    Retorno calcularRutaMenorTiempo(String codigoOrigen, String codigoDestino);
    Retorno calcularRutaMenorDistancia(String codigoOrigen, String codigoDestino);



}
