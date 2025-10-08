package sistema;

import dominio.Conexion;
import dominio.Farmacia;
import dominio.Medicamento;
import interfaz.Categoria;
import interfaz.Retorno;
import tads.ABB;
import interfaz.Sistema;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ImplementacionSistema implements Sistema  {

    private boolean sistemaInicializado = false;
    private int maxFarmacias;
    private int farmaciasRegistradas = 0;  // Contador de las farmacias registradas
    private Farmacia[] farmacias;

    //Dos árboles, uno por código y otro por nombre
    private ABB<Medicamento> arbolMedicamentosPorCodigo;
    private ABB<Medicamento> arbolMedicamentosPorNombre;

    private Conexion[] conexiones;
    private int maxConexiones = 200;
    private int conexionesRegistradas = 0;

    public ImplementacionSistema(){

    }

    @Override
    public Retorno inicializarSistema(int maxFarmacias) {
        if(maxFarmacias <= 3){
            return Retorno.error1("El numero de farmacias debe ser mayor que 3.");
        }
        this.maxFarmacias = maxFarmacias;
        this.farmaciasRegistradas = 0;
        this.sistemaInicializado = true;

        conexiones = new Conexion[maxFarmacias];

        //Inicializacion de las estructuras
        this.farmacias = new Farmacia[maxFarmacias];
        // El arbol no se inicializa en el constructor para respetar la no existencia
        // del sistema hasta llamar a este metodo.
        this.arbolMedicamentosPorCodigo = new ABB<>(Comparator.comparing(Medicamento::getCodigo));
        this.arbolMedicamentosPorNombre = new ABB<>(Comparator.comparing(Medicamento::getNombre));

        return Retorno.ok();
    }

  /*  @Override // consultar al profesor si se implementa el caso no implementada
    public Retorno registrarMedicamento(String codigo, String nombre, String fechaVencimiento, Categoria categoria) {
        return Retorno.noImplementada();
    }*/

    @Override
    public Retorno registrarMedicamento(String codigo, String nombre, String fechaVencimiento, Categoria categoria) {
        //1. Validación de parámetros
        if(codigo == null || codigo.isEmpty() ||
                nombre == null || nombre.isEmpty() ||
                fechaVencimiento == null || fechaVencimiento.isEmpty() ||
                categoria == null ){
            return Retorno.error1("Alguno de los parámetros es vacío o nulo.");
        }
        //2. Validación de fecha
        if(!dominio.Medicamento.esFechaValida(fechaVencimiento)){
            return Retorno.error2("El formato de fecha no es valido. Debe ser AAAA-MM-DD.");
        }

        Medicamento medicamento = new Medicamento(codigo,nombre,fechaVencimiento,categoria);

        //3. Verificar el duplicado por código
        if(arbolMedicamentosPorCodigo.buscar(medicamento)){
            return Retorno.error3("Ya existe un medicamento con ese nombre");
        }

        //4. Verificar el duplicado del nombre
        if(arbolMedicamentosPorNombre.buscar(medicamento)){
            return Retorno.error4("Ya existe un medicamento con ese nombre");
        }

        //Insertar en ambos arboles
        arbolMedicamentosPorCodigo.insertar(medicamento);
        arbolMedicamentosPorNombre.insertar(medicamento);

        return Retorno.ok("Se ha registrado el medicamento exitosamente.");
    }

    @Override
    public Retorno buscarMedicamentoPorCodigo(String codigo) {
        if(codigo == null || codigo.isEmpty() ){
            return Retorno.error1("El codigo no puede ser vacio o nulo.");
        }

        ABB.Contador contador = new ABB.Contador();

        Medicamento med = arbolMedicamentosPorCodigo.buscarConRecorridos(
                new Medicamento(codigo, null, null, null), contador
        );

        if(med == null){
            return Retorno.error2("No existe un medicamento registrado con ese codigo.");
        }

        // Formatear string de salida
        String valorString = med.getCodigo() + ";" +
                med.getNombre() + ";" +
                med.getFechaVencimiento() + ";" +
                med.getCategoria();

        return Retorno.ok(contador.getValor(), valorString);
    }

    @Override
    public Retorno listarMedicamentosPorCodigoAscendente() {
        if(arbolMedicamentosPorCodigo == null || arbolMedicamentosPorCodigo.isEmpty()){
            return Retorno.ok("No hay medicamentos registrados.");
        }

        //Lista para acumular los medicamentos
        List<Medicamento> lista = new ArrayList<>();
        arbolMedicamentosPorCodigo.listarInOrden(lista);

        // Armar el string con el formato requerido.
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < lista.size(); i++){
            Medicamento m = lista.get(i);
            sb.append(m.getCodigo() + ";")
                    .append(m.getNombre() + ";")
                    .append(m.getFechaVencimiento() + ";")
                    .append(m.getCategoria());

            if(i < lista.size() - 1){
                sb.append("|");
            }
        }

        return Retorno.ok(sb.toString());
    }

    @Override
    public Retorno listarMedicamentosPorCodigoDescendente() {
        if(arbolMedicamentosPorCodigo == null || arbolMedicamentosPorCodigo.isEmpty()){
            return Retorno.ok("No hay medicamentos registrados.");
        }

        List<Medicamento> lista = new ArrayList<>();
        arbolMedicamentosPorCodigo.listarInOrdenDesc(lista);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lista.size(); i++){
            Medicamento m = lista.get(i);
            sb.append(m.getCodigo() + ";")
                    .append(m.getNombre() + ";")
                    .append(m.getFechaVencimiento() + ";")
                    .append(m.getCategoria());
            if(i < lista.size() - 1){
                sb.append("|");
            }
        }
        return Retorno.ok(sb.toString());
    }

    @Override
    public Retorno buscarMedicamentoPorNombre(String nombre) {
        if(nombre == null || nombre.isEmpty()){
            return Retorno.error1("El nombre no puede ser vacio o nulo.");
        }

        ABB.Contador contador = new ABB.Contador();

        //Se busca en el arbol ordenado por nombre
        Medicamento med = arbolMedicamentosPorNombre.buscarConRecorridos(
                new Medicamento(null, nombre, null, null), contador
        );

        if(med == null){
            return Retorno.error2("No existe un medicamento con ese nombre.");
        }

        // Formatear el string de salida
        String valorString = med.getCodigo() + ";" +
                med.getNombre() + ";" +
                med.getFechaVencimiento() + ";" +
                med.getCategoria();

        //Retornar junto con la cantidad de elementos recorridos
        return Retorno.ok(contador.getValor(), valorString);
    }

    @Override
    public Retorno listarMedicamentosPorNombreAscendente() {
        if(arbolMedicamentosPorNombre == null || arbolMedicamentosPorNombre.isEmpty()){
            return Retorno.ok("No hay medicamentos registrados.");
        }
        List<Medicamento> lista = new ArrayList<>();
        arbolMedicamentosPorNombre.listarInOrden(lista);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lista.size(); i++){
            Medicamento m = lista.get(i);
            sb.append(m.getCodigo() + ";")
                    .append(m.getNombre() + ";")
                    .append(m.getFechaVencimiento() + ";")
                    .append(m.getCategoria());

            if(i < lista.size() - 1){
                sb.append("|");
            }
        }
        return Retorno.ok(sb.toString());
    }

    @Override
    public Retorno listarMedicamentosPorNombreDescendente() {
        if(arbolMedicamentosPorNombre == null || arbolMedicamentosPorNombre.isEmpty()){
            return Retorno.ok("No hay medicamentos registrados.");
        }
        List<Medicamento> lista = new ArrayList<>();
        arbolMedicamentosPorNombre.listarInOrdenDesc(lista);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lista.size(); i++){
            Medicamento m = lista.get(i);
            sb.append(m.getCodigo() + ";")
                    .append(m.getNombre() + ";")
                    .append(m.getFechaVencimiento() + ";")
                    .append(m.getCategoria());
            if(i < lista.size() - 1){
                sb.append("|");
            }
        }
        return Retorno.ok(sb.toString());
    }

    @Override
    public Retorno listarMedicamentosPorCategoría(Categoria unaCategoria) {
        if(unaCategoria == null){
            return Retorno.ok("No existe una categoria.");
        }
        if(arbolMedicamentosPorCodigo == null || arbolMedicamentosPorCodigo.isEmpty()){
            return Retorno.ok("No hay medicamentos registrados.");
        }
        List<Medicamento> lista = new ArrayList<>();
        arbolMedicamentosPorCodigo.listarPorCategoria(unaCategoria, lista);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lista.size(); i++){
            Medicamento m = lista.get(i);
            sb.append(m.getCodigo() + ";")
                    .append(m.getNombre() + ";")
                    .append(m.getFechaVencimiento() + ";")
                    .append(m.getCategoria());
            if(i < lista.size() - 1){
                sb.append("|");
            }
        }
        return Retorno.ok(sb.toString());
    }

    @Override
    public Retorno registrarFarmacia(String codigo, String nombre) {
        if(codigo == null || codigo.isEmpty() || nombre == null || nombre.isEmpty()){
            return Retorno.error2("El codigo y el nombre no pueden ser vacios o nulos.");
        }
        if(farmaciasRegistradas >= maxFarmacias){
            return Retorno.error1("Ya se alcanzo el maximo de farmacias registradas.");
        }

        for(int i = 0; i < farmaciasRegistradas; i++){
            if(farmacias[i].getCodigo().equals(codigo)){
                return Retorno.error3("Ya existe una farmacia con ese codigo.");
            }
        }

        Farmacia nueva = new Farmacia(codigo, nombre);
        farmacias[farmaciasRegistradas] = nueva;
        farmaciasRegistradas++;
        return Retorno.ok("Farmacia registrada correctamente.");
    }

    @Override
    public Retorno registrarConexion(String codigoOrigen, String codigoDestino, int tiempo, int distancia, int costo) {
        //1-Validar parametros
        if(codigoOrigen == null || codigoOrigen.isEmpty() || codigoDestino == null || codigoDestino.isEmpty()){
            return Retorno.error2("Los codigos no pueden ser vacios.");
        }

        //2- Validar parametros numericos
        if(tiempo <= 0 || distancia <= 0 || costo <= 0){
            return Retorno.error1("El tiempo, distancia y costo deben ser mayores que 0.");
        }

        //3- Buscar farmacias de origen y destino
        Farmacia origen = buscarFarmaciaPorCodigo(codigoOrigen);
        Farmacia destino = buscarFarmaciaPorCodigo(codigoDestino);

        if(origen == null){
            return Retorno.error2("No existe la farmacia de origen.");
        }

        if(destino == null){
            return Retorno.error2("No existe la farmacia de destino.");
        }

        //4- Verificar si ya existe la conexion
        for (Conexion c : conexiones) {
            if ((c.getCodigoDestino().equals(origen.getCodigo()) && c.getCodigoDestino().equals(destino.getCodigo())) ||
                    (c.getCodigoOrigen().equals(destino.getCodigo()) && c.getCodigoDestino().equals(origen.getCodigo()))) {
                return Retorno.error5("Ya existe una conexión entre esas farmacias.");
            }
        }

        //5- Verificar si hay espacio en e array para dos conexiones
        if(conexionesRegistradas + 2 > maxConexiones){
            return Retorno.error1("no hay espacio para registrar mas conexiones.");
        }

        //6- Crear  y registrar conexiones bidireccionales
        Conexion conexionAB = new Conexion(codigoOrigen, codigoDestino, tiempo, distancia, costo);
        Conexion conexionBA = new Conexion(codigoDestino, codigoOrigen, tiempo, distancia, costo);

        conexiones[conexionesRegistradas] = conexionAB;
        conexiones[conexionesRegistradas + 1] = conexionBA;
        conexionesRegistradas += 2;

        return Retorno.ok("Conexion registrada exitosamente entre " + origen.getNombre() + " y " + destino.getNombre());
    }

    //Metodo auxiliar
    //Buscar una farmacia por codigo en el array de farmacias
    private Farmacia buscarFarmaciaPorCodigo(String codigo) {
        for (int i = 0; i < farmaciasRegistradas; i++) {
            if(farmacias[i].getCodigo().equals(codigo)){
                return farmacias[i];
            }
        }
        return null;
    }

    @Override
    public Retorno redFarmaciasPorCantidadDeConexiones(String codigoOrigen, int cantidad) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno analizarFarmacia(String codigoOrigen) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno calcularRutaMenorTiempo(String codigoOrigen, String codigoDestino) {
        return Retorno.noImplementada();
    }

    @Override
    public Retorno calcularRutaMenorDistancia(String codigoOrigen, String codigoDestino) {
        return Retorno.noImplementada();
    }

    // metodo publico para acceder al arbol desde el main
    public ABB<Medicamento> getArbolMedicamentos() {
        return arbolMedicamentosPorCodigo;
    }


}
