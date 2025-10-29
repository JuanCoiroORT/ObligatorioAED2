package sistema;

import dominio.Conexion;
import dominio.Farmacia;
import dominio.Medicamento;
import interfaz.Categoria;
import interfaz.Retorno;
import tads.ABB;
import interfaz.Sistema;
import tads.Cola;
import tads.Lista;
import tads.Nodo;

import java.util.*;

public class ImplementacionSistema implements Sistema  {

    private boolean sistemaInicializado = false;
    private int maxFarmacias;
    private Lista<Farmacia> farmacias;

    //Dos árboles, uno por código y otro por nombre
    private ABB<Medicamento> arbolMedicamentosPorCodigo;
    private ABB<Medicamento> arbolMedicamentosPorNombre;

    // Grafo de farmacias: clave = código de farmacia, valor = lista de conexiones
    private Map<String, Lista<Conexion>> conexiones;
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
        this.sistemaInicializado = true;

        //Inicializacion de las estructuras
        this.farmacias = new Lista<Farmacia>();
        // Se crea grafo vacio de adyacencias
        this.conexiones = new HashMap<>();
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
        if (arbolMedicamentosPorCodigo == null || arbolMedicamentosPorCodigo.isEmpty()) {
            return Retorno.ok("No hay medicamentos registrados.");
        }

        // Lista para acumular los medicamentos usando tu TAD Lista
        Lista<Medicamento> lista = new Lista<>();
        arbolMedicamentosPorCodigo.listarInOrden(lista);

        // Armar el string con el formato requerido
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < lista.getLargo(); i++) {
            Medicamento m = lista.obtenerPorIndice(i);
            sb.append(m.getCodigo()).append(";")
                    .append(m.getNombre()).append(";")
                    .append(m.getFechaVencimiento()).append(";")
                    .append(m.getCategoria());

            if (i < lista.largo() - 1) {
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

        Lista<Medicamento> lista = new Lista<Medicamento>();
        arbolMedicamentosPorCodigo.listarInOrdenDesc(lista);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lista.getLargo(); i++){
            Medicamento m = lista.obtenerPorIndice(i);
            sb.append(m.getCodigo() + ";")
                    .append(m.getNombre() + ";")
                    .append(m.getFechaVencimiento() + ";")
                    .append(m.getCategoria());
            if(i < lista.largo() - 1){
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
        Lista<Medicamento> lista = new Lista<Medicamento>();
        arbolMedicamentosPorNombre.listarInOrden(lista);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lista.getLargo(); i++){
            Medicamento m = lista.obtenerPorIndice(i);
            sb.append(m.getCodigo() + ";")
                    .append(m.getNombre() + ";")
                    .append(m.getFechaVencimiento() + ";")
                    .append(m.getCategoria());

            if(i < lista.getLargo() - 1){
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
        Lista<Medicamento> lista = new Lista<Medicamento>();
        arbolMedicamentosPorNombre.listarInOrdenDesc(lista);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lista.getLargo(); i++){
            Medicamento m = lista.obtenerPorIndice(i);
            sb.append(m.getCodigo() + ";")
                    .append(m.getNombre() + ";")
                    .append(m.getFechaVencimiento() + ";")
                    .append(m.getCategoria());
            if(i < lista.getLargo() - 1){
                sb.append("|");
            }
        }
        return Retorno.ok(sb.toString());
    }

    @Override
    public Retorno listarMedicamentosPorCategoria(Categoria unaCategoria) {
        if(unaCategoria == null){
            return Retorno.ok("No existe una categoria.");
        }
        if(arbolMedicamentosPorCodigo == null || arbolMedicamentosPorCodigo.isEmpty()){
            return Retorno.ok("No hay medicamentos registrados.");
        }
        Lista<Medicamento> lista = new Lista<Medicamento>();
        arbolMedicamentosPorCodigo.listarPorCategoria(unaCategoria, lista);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lista.getLargo(); i++){
            Medicamento m = lista.obtenerPorIndice(i);
            sb.append(m.getCodigo() + ";")
                    .append(m.getNombre() + ";")
                    .append(m.getFechaVencimiento() + ";")
                    .append(m.getCategoria());
            if(i < lista.getLargo() - 1){
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
        if(farmacias.getLargo() >= maxFarmacias){
            return Retorno.error1("Ya se alcanzo el maximo de farmacias registradas.");
        }

        Nodo<Farmacia> nodo = farmacias.getInicio();
        while (nodo != null) {
            if (nodo.getDato().getCodigo().equals(codigo)) {
                return Retorno.error3("Ya existe una farmacia con ese código.");
            }
            nodo = nodo.getSiguiente();
        }

        Farmacia nueva = new Farmacia(codigo, nombre);
        farmacias.agregarInicio(nueva);  // o usar agregarFinal si querés mantener orden
        return Retorno.ok("Farmacia registrada correctamente.");
    }

    @Override
    public Retorno registrarConexion(String codigoOrigen, String codigoDestino, int tiempo, int distancia, int costo) {
        //1--Validaciones
        if (codigoOrigen == null || codigoOrigen.isEmpty()) {
            return Retorno.error2("Los codigos no pueden ser vacios o  nulos.");
        }
        if (tiempo <= 0 || distancia <= 0 || costo <= 0) {
            return Retorno.error1("Los valores de tiempo, distancia y costo deben ser mayores que cero.");
        }

        //2--Verificar que los vertices existan
        if(!conexiones.containsKey(codigoOrigen)){
            return Retorno.error2("No existe la farmacia de origen.");
        }
        if(!conexiones.containsKey(codigoDestino)){
            return Retorno.error2("No existe la farmacia de destino.");
        }

        Lista<Conexion> listaOrigen = conexiones.get(codigoOrigen);
        Lista<Conexion> listaDestino = conexiones.get(codigoDestino);

        //3--Verificar que no exista ya la conexion
        Nodo<Conexion> nodo = listaOrigen.getInicio();
        while (nodo != null) {
            if (nodo.getDato().getCodigoDestino().equals(codigoDestino)) {
                return Retorno.error1("Ya existe una conexión entre estas farmacias.");
            }
            nodo = nodo.getSiguiente();
        }

        //4--Crear arista y agregar a ambos lados(grafo no dirigido)
        Conexion aristaOrigen = new Conexion(codigoDestino, codigoDestino, tiempo, distancia, costo);
        Conexion aristaDestino = new Conexion(codigoOrigen, codigoDestino, tiempo, distancia, costo);

        listaOrigen.agregarInicio(aristaOrigen);
        listaDestino.agregarInicio(aristaDestino);

        return Retorno.ok("Conexión registrada correctamente.");
    }

    private Farmacia buscarFarmaciaPorCodigo(String codigo){
        if (codigo == null || codigo.isEmpty()) return null;

        Nodo<Farmacia> nodo = farmacias.getInicio();
        while (nodo != null) {
            if (nodo.getDato().getCodigo().equals(codigo)) {
                return nodo.getDato();
            }
            nodo = nodo.getSiguiente();
        }

        return null;
    }

    @Override
    public Retorno redFarmaciasPorCantidadDeConexiones(String codigoOrigen, int cantidad){
        //1--Validaciones
        if (cantidad < 0) {
            return Retorno.error1("La cantidad no puede ser menor que cero.");
        }
        if (codigoOrigen == null || codigoOrigen.isEmpty()) {
            return Retorno.error2("El código no puede ser vacío o null.");
        }
        if (!conexiones.containsKey(codigoOrigen)) {
            return Retorno.error3("La farmacia de origen no está registrada.");
        }
        //2-- BFS para limitar profundidad por cantidad de conexiones
        Set<String> visitados = new HashSet<>();
        Queue<String> cola = new LinkedList<>();
        Map<String, Integer> profundidad = new HashMap<>();

        visitados.add(codigoOrigen);
        profundidad.put(codigoOrigen, 0);
        cola.add(codigoOrigen);

        List<Farmacia> alcanzables = new ArrayList<>();

        while (!cola.isEmpty()) {
            String actual = cola.poll();
            int nivel = profundidad.get(actual);

            if (nivel == cantidad) continue;

            Lista<Conexion> listaConexion = conexiones.get(actual);
            Nodo<Conexion> aux = listaConexion.getInicio();

            while (aux != null) {
                String codDestino = aux.getDato().getCodigoDestino();

                if (!visitados.contains(codDestino)) {
                    visitados.add(codDestino);
                    profundidad.put(codDestino, nivel + 1);
                    cola.add(codDestino);

                    // Agregar la farmacia encontrada, no incluir la origen
                    if (!codDestino.equals(codigoOrigen)) {
                        alcanzables.add(buscarFarmaciaPorCodigo(codDestino));
                    }
                }
                aux = aux.getSiguiente();
            }
        }
        //3-- Orden lexicografico por codigo
        alcanzables.sort(Comparator.comparing(Farmacia::getCodigo));

        //4-- Formatear el valor String
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < alcanzables.size(); i++) {
            Farmacia f = alcanzables.get(i);
            sb.append(f.getCodigo()).append(";")
                    .append(f.getNombre()).append(";");
            if (i < alcanzables.size() - 1) {
                sb.append("|");
            }
        }

        return Retorno.ok(sb.toString());
    }



    @Override
    public Retorno analizarFarmacia(String codigoOrigen) {

        //1--Validaciones
        if (codigoOrigen == null || codigoOrigen.isEmpty()) {
            return Retorno.error1("El código no puede ser vacío o nulo.");
        }

        if (buscarFarmaciaPorCodigo(codigoOrigen) == null) {
            return Retorno.error2("No existe la farmacia con ese código.");
        }

        //2--Preparar BFS ignorando la farmacia a analizar
        Cola<String> cola = new Cola<>(farmacias.getLargo());
        Lista<String> visitados = new Lista<>();

        //Elegir un nodo inicial distinto al ignorado
        String primerNodo = null;
        Nodo<Farmacia> nodoF = farmacias.getInicio();
        while (nodoF != null) {
            if(!nodoF.getDato().getCodigo().equals(codigoOrigen)) {
                primerNodo = nodoF.getDato().getCodigo();
                break;
            }
            nodoF = nodoF.getSiguiente();
        }

        if(primerNodo == null) return Retorno.ok("NO");

        //3--BFS
        cola.push(primerNodo);
        visitados.agregarInicio(primerNodo);

        while (!cola.isEmpty()) {
            String actual = cola.pop();
            Lista<Conexion> listaConex = conexiones.get(actual);
            Nodo<Conexion> nodoCon = listaConex.getInicio();

            while (nodoCon != null) {
                String destino = nodoCon.getDato().getCodigoDestino();
                if (!destino.equals(codigoOrigen) && !visitados.existe(destino)) {
                    visitados.agregarInicio(destino);
                    cola.push(destino);
                }
                nodoCon = nodoCon.getSiguiente();
            }
        }

        //4--Comparar cantidad de nodos alcanzables
        if (visitados.getLargo() < farmacias.getLargo() - 1) {
            return Retorno.ok("SI"); // Es critica
        } else {
            return Retorno.ok("NO"); // No es critica
        }
    }

    @Override
    public Retorno calcularRutaMenorTiempo(String codigoOrigen, String codigoDestino) {
        //1-- Validaciones
        if (codigoOrigen == null || codigoOrigen.isEmpty() ||
                codigoDestino == null || codigoDestino.isEmpty()) {
            return Retorno.error1("Los códigos no pueden ser vacíos o nulos.");
        }

        Farmacia fOrigen = buscarFarmaciaPorCodigo(codigoOrigen);
        Farmacia fDestino = buscarFarmaciaPorCodigo(codigoDestino);

        if (fOrigen == null) return Retorno.error2("No existe la farmacia de origen.");
        if (fDestino == null) return Retorno.error3("No existe la farmacia de destino.");

        //2--Inicializar estructuras
        Lista<String> visitados = new Lista<>();
        Lista<String> porVisitar = new Lista<>();
        Lista<String> caminoPrevio = new Lista<>(); //mantiene el camino anterior anterior en cada nodo
        Lista<Integer> distancias = new Lista<>(); // distancia acumulada desde el origen

        // Inicializar todos los nodos con distancia infinita
        Nodo<Farmacia> nodoF = farmacias.getInicio();
        while (nodoF != null) {
            String cod = nodoF.getDato().getCodigo();
            porVisitar.agregarInicio(cod);
            distancias.agregarInicio(Integer.MAX_VALUE);
            caminoPrevio.agregarInicio(null);
            nodoF = nodoF.getSiguiente();
        }

        // Distancia del nodo origen = 0
        Nodo<String> n = porVisitar.getInicio();
        Nodo<Integer> d = distancias.getInicio();
        Nodo<String> cPrev = caminoPrevio.getInicio();
        while (n != null) {
            if (n.getDato().equals(codigoOrigen)) {
                d.setDato(0);
                break;
            }
            n = n.getSiguiente();
            d = d.getSiguiente();
            cPrev = cPrev.getSiguiente();
        }

        //3-- Dijkstra
        while (!porVisitar.esVacia()) {
            // Buscar nodo con distancia mínima
            Nodo<String> nActual = porVisitar.getInicio();
            Nodo<Integer> dActual = distancias.getInicio();
            Nodo<String> cActual = caminoPrevio.getInicio();

            Nodo<String> minNodo = nActual;
            Nodo<Integer> minDist = dActual;
            Nodo<String> minPrev = cActual;

            Nodo<String> tempN = nActual;
            Nodo<Integer> tempD = dActual;
            Nodo<String> tempC = cActual;

            while (tempN != null) {
                if (tempD.getDato() < minDist.getDato()) {
                    minNodo = tempN;
                    minDist = tempD;
                    minPrev = tempC;
                }
                tempN = tempN.getSiguiente();
                tempD = tempD.getSiguiente();
                tempC = tempC.getSiguiente();
            }

            String nodoActualCodigo = minNodo.getDato();
            int nodoActualDist = minDist.getDato();

            if (nodoActualCodigo.equals(codigoDestino)) break; // llegamos al destino

            // Recorrer vecinos
            Lista<Conexion> vecinos = conexiones.get(nodoActualCodigo);
            Nodo<Conexion> arista = vecinos.getInicio();
            while (arista != null) {
                String vecino = arista.getDato().getCodigoDestino();

                // Si vecino no visitado
                if (!visitados.existe(vecino)) {
                    // Buscar nodo vecino en porVisitar y distancias
                    Nodo<String> tN = porVisitar.getInicio();
                    Nodo<Integer> tD = distancias.getInicio();
                    Nodo<String> tC = caminoPrevio.getInicio();
                    while (tN != null) {
                        if (tN.getDato().equals(vecino)) {
                            int nuevaDist = nodoActualDist + arista.getDato().getTiempo();
                            if (nuevaDist < tD.getDato()) {
                                tD.setDato(nuevaDist);
                                tC.setDato(nodoActualCodigo);
                            }
                            break;
                        }
                        tN = tN.getSiguiente();
                        tD = tD.getSiguiente();
                        tC = tC.getSiguiente();
                    }
                }
                arista = arista.getSiguiente();
            }

            // Marcar nodoActual como visitado
            visitados.agregarInicio(nodoActualCodigo);
            // Eliminar de porVisitar
            porVisitar.eliminarDato(nodoActualCodigo);
        }

        //4-- Reconstruir camino desde destino
        Lista<Farmacia> camino = new Lista<>();
        String nodo = codigoDestino;
        while (nodo != null) {
            Farmacia f = buscarFarmaciaPorCodigo(nodo);
            if (f == null) break;
            camino.agregarInicio(f);

            // Buscar previo en caminoPrevio
            Nodo<String> tC = caminoPrevio.getInicio();
            String previo = null;
            while (tC != null) {
                if (tC.getDato().equals(nodo)) {
                    previo = tC.getDato();
                    break;
                }
                tC = tC.getSiguiente();
            }
            if (nodo.equals(codigoOrigen)) break;
            nodo = previo;
        }

        if (camino.getInicio() == null || !camino.getInicio().getDato().getCodigo().equals(codigoOrigen)) {
            return Retorno.error4("No existe un camino posible entre las farmacias.");
        }

        //5--Construir valor String
        StringBuilder valorString = new StringBuilder();
        Nodo<Farmacia> nCamino = camino.getInicio();
        while (nCamino != null) {
            Farmacia f = nCamino.getDato();
            if (valorString.length() > 0) valorString.append("|");
            valorString.append(f.getCodigo()).append(";")
                    .append(f.getNombre()).append(";");
            nCamino = nCamino.getSiguiente();
        }

        //6-- Tiempo total
        int totalTiempo = 0;
        // Buscar distancia acumulada del destino
        Nodo<String> nDist = caminoPrevio.getInicio();
        Nodo<Integer> dDist = distancias.getInicio();
        while (nDist != null) {
            if (nDist.getDato().equals(codigoDestino)) {
                totalTiempo = dDist.getDato();
                break;
            }
            nDist = nDist.getSiguiente();
            dDist = dDist.getSiguiente();
        }

        return Retorno.ok(totalTiempo, valorString.toString());
    }

    @Override
    public Retorno calcularRutaMenorDistancia(String codigoOrigen, String codigoDestino) {
        //1--Validaciones
        if (codigoOrigen == null || codigoOrigen.isEmpty() ||
                codigoDestino == null || codigoDestino.isEmpty()) {
            return Retorno.error1("Los códigos no pueden ser vacíos o nulos.");
        }

        Farmacia fOrigen = buscarFarmaciaPorCodigo(codigoOrigen);
        Farmacia fDestino = buscarFarmaciaPorCodigo(codigoDestino);

        if (fOrigen == null) return Retorno.error2("No existe la farmacia de origen.");
        if (fDestino == null) return Retorno.error3("No existe la farmacia de destino.");

        //2-- inicializar estructuras
        Lista<String> visitados = new Lista<>();
        Lista<String> porVisitar = new Lista<>();
        Lista<Integer> distancias = new Lista<>();
        Lista<String> caminoPrevio = new Lista<>();

        // Inicializar: todos los nodos con distancia infinita
        Nodo<Farmacia> nodoF = farmacias.getInicio();
        while (nodoF != null) {
            String cod = nodoF.getDato().getCodigo();
            porVisitar.agregarInicio(cod);
            distancias.agregarInicio(Integer.MAX_VALUE);
            caminoPrevio.agregarInicio(null);
            nodoF = nodoF.getSiguiente();
        }

        // Distancia del origen = 0
        Nodo<String> n = porVisitar.getInicio();
        Nodo<Integer> d = distancias.getInicio();
        Nodo<String> cPrev = caminoPrevio.getInicio();
        while (n != null) {
            if (n.getDato().equals(codigoOrigen)) {
                d.setDato(0);
                break;
            }
            n = n.getSiguiente();
            d = d.getSiguiente();
            cPrev = cPrev.getSiguiente();
        }

        //3--Dijkstra
        while (!porVisitar.esVacia()) {
            // Buscar nodo con distancia mínima
            Nodo<String> nActual = porVisitar.getInicio();
            Nodo<Integer> dActual = distancias.getInicio();
            Nodo<String> cActual = caminoPrevio.getInicio();

            Nodo<String> minNodo = nActual;
            Nodo<Integer> minDist = dActual;
            Nodo<String> minPrev = cActual;

            Nodo<String> tempN = nActual;
            Nodo<Integer> tempD = dActual;
            Nodo<String> tempC = cActual;

            while (tempN != null) {
                if (tempD.getDato() < minDist.getDato()) {
                    minNodo = tempN;
                    minDist = tempD;
                    minPrev = tempC;
                }
                tempN = tempN.getSiguiente();
                tempD = tempD.getSiguiente();
                tempC = tempC.getSiguiente();
            }

            String nodoActualCodigo = minNodo.getDato();
            int nodoActualDist = minDist.getDato();

            if (nodoActualCodigo.equals(codigoDestino)) break; // llegamos al destino

            // Recorrer vecinos
            Lista<Conexion> vecinos = conexiones.get(nodoActualCodigo);
            Nodo<Conexion> arista = vecinos.getInicio();
            while (arista != null) {
                String vecino = arista.getDato().getCodigoDestino();

                // Si vecino no visitado
                if (!visitados.existe(vecino)) {
                    // Buscar nodo vecino en porVisitar y distancias
                    Nodo<String> tN = porVisitar.getInicio();
                    Nodo<Integer> tD = distancias.getInicio();
                    Nodo<String> tC = caminoPrevio.getInicio();
                    while (tN != null) {
                        if (tN.getDato().equals(vecino)) {
                            int nuevaDist = nodoActualDist + arista.getDato().getDistancia(); // ✅ sumamos distancia
                            if (nuevaDist < tD.getDato()) {
                                tD.setDato(nuevaDist);
                                tC.setDato(nodoActualCodigo);
                            }
                            break;
                        }
                        tN = tN.getSiguiente();
                        tD = tD.getSiguiente();
                        tC = tC.getSiguiente();
                    }
                }
                arista = arista.getSiguiente();
            }

            // Marcar nodoActual como visitado
            visitados.agregarInicio(nodoActualCodigo);
            // Eliminar de porVisitar
            porVisitar.eliminarDato(nodoActualCodigo);
        }

        //4--Reconstruir camino desde destino
        Lista<Farmacia> camino = new Lista<>();
        String nodo = codigoDestino;
        while (nodo != null) {
            Farmacia f = buscarFarmaciaPorCodigo(nodo);
            if (f == null) break;
            camino.agregarInicio(f);

            // Buscar previo en caminoPrevio
            Nodo<String> tC = caminoPrevio.getInicio();
            String previo = null;
            while (tC != null) {
                if (tC.getDato().equals(nodo)) {
                    previo = tC.getDato();
                    break;
                }
                tC = tC.getSiguiente();
            }
            if (nodo.equals(codigoOrigen)) break;
            nodo = previo;
        }

        if (camino.getInicio() == null || !camino.getInicio().getDato().getCodigo().equals(codigoOrigen)) {
            return Retorno.error4("No existe un camino posible entre las farmacias.");
        }

        //5-- Construir valor string
        StringBuilder valorString = new StringBuilder();
        Nodo<Farmacia> nCamino = camino.getInicio();
        while (nCamino != null) {
            Farmacia f = nCamino.getDato();
            if (valorString.length() > 0) valorString.append("|");
            valorString.append(f.getCodigo()).append(";")
                    .append(f.getNombre()).append(";");
            nCamino = nCamino.getSiguiente();
        }

        //6--Total distancia
        int totalDistancia = 0;
        // Buscar distancia acumulada del destino
        Nodo<String> nDist = porVisitar.getInicio();
        Nodo<Integer> dDist = distancias.getInicio();
        while (nDist != null) {
            if (nDist.getDato().equals(codigoDestino)) {
                totalDistancia = dDist.getDato();
                break;
            }
            nDist = nDist.getSiguiente();
            dDist = dDist.getSiguiente();
        }

        return Retorno.ok(totalDistancia, valorString.toString());
    }


}
