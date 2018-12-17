package practica3;


import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.Math.floor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Thomas LESBROS
 */
public class AgenteAdministrador extends Agente{

    private static final int ESTADO_SUBSCRIPCION =1;
    private static final int ESTADO_ERROR =2;
        private static final int ESTADO_CHECK=3;
    private static final int ESTADO_BUSCANDO =4;
    private static final int ESTADO_ENCONTRADO=5;
    private static final int ESTADO_FIN=6;


    private int subEstados;

    private static final int ELECCION_VEHICULO=1;
    private static final int MOVER=2;
    private static final int REPOSTAR=3;
    private static final int OBSERVAR=4;
    private static final int ENCONTRADO=5;



    Mapa matrizCamino;

    private ArrayList<AgentID> agentes;
    private String nivel;
    private int performative;
    private int estado;
    private int i;


    private HashMap<String, Vehiculo> tripulacion; //Aquí guardaremos todos los vehículos que tengamos en funcionamiento.
    private boolean vehiculoSeleccionado = false;
    private String vehiculoElegido;
    ArrayList<int[]> objetivos = new ArrayList<>();
    ArrayList<String> vehiculosExploradores = new ArrayList<>();
    ArrayList<String> vehiculosEsperando = new ArrayList<>();
    ArrayList<String> vehiculosFinalizados = new ArrayList<>();
    ArrayList<int[]> posicionAgenteFinalizado  = new ArrayList<>();
    private boolean cambiamosVehiculo =false;

    private int[][] caminosPosibles;
    private ArrayList<Integer> camino_local = new ArrayList<>();
    private boolean camino_intentado=false;
    private boolean hay_camino=false;
    private int contMovimientos=0;
    private Camino camino;

    private boolean ejecutar = true;
    private boolean fallo = false;

    /**
     *
     * @param aid
     * @param host
     * @param nivel
     * @param subditos
     */
    public AgenteAdministrador(AgentID aid, String host, String nivel, ArrayList<AgentID> subditos) throws Exception{

        super(aid, host);
        this.nivel = nivel;
        estado = ESTADO_SUBSCRIPCION;
        agentes = subditos;
        tripulacion = new HashMap<>();
        tripulacion.put("Vehiculo0", null);
        tripulacion.put("Vehiculo1", null);
        tripulacion.put("Vehiculo2", null);
        tripulacion.put("Vehiculo3", null);
    }

    /**
     * @author Thomas LESBROS
     *
     */
    @Override
    public boolean recibirMensaje(){
        ACLMessage inbox;
        try {
            inbox = receiveACLMessage();
            JsonObject objeto = Json.parse(inbox.getContent()).asObject();
            if(objeto.get("result") != null){
                conversationID = inbox.getConversationId();
                return true;
            }else if(objeto.get("trace") != null){
                this.fallo=true;
                JsonArray ja = objeto.get("trace").asArray();
                byte data[] = new byte[ja.size()];
                for(int i=0; i<data.length; i++){
                    data[i]=(byte) ja.get(i).asInt();
                }
                String title;
                title = String.format("traza de "+nivel+".png");
                FileOutputStream fos = new FileOutputStream(title);
                fos.write(data);
                fos.close();
                System.out.println("Traza Guardada como: "+title);
                return false;
            }else if(objeto.get("details") != null){
                System.out.println("Fallo");
                this.fallo=false;
                return false;
            }else if (objeto.get("mapa") != null){
                JsonArray arr = objeto.get("Mapa").asArray();

                matrizCamino.parseJson(objeto.get("Mapa").asArray());
                return true;
            }
            else{
                System.out.println("Fallo");
                this.fallo=false;
                return false;
            }

        }catch(InterruptedException e){
            System.err.println(e.toString());
            return false;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AgenteAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AgenteAdministrador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }




    @Override
    public void execute(){
        JsonObject objeto;
        boolean exito = false;
        while(ejecutar){
            switch(estado){
                case ESTADO_SUBSCRIPCION:
                    objeto = new JsonObject();
                    objeto.add("world", nivel);
                    performative = ACLMessage.SUBSCRIBE;
                    enviarMensaje(objeto, new AgentID(host), performative, null, null);
                    exito = recibirMensaje();
                    if(conversationID==null){
                        exito = recibirMensaje();
                    }
                    if(exito){
                        System.out.println("\nExito en el subscribe");
                        estado = ESTADO_CHECK;
                    }else{
                        if(fallo==false){
                           System.out.println("Fallo en el subscribe");
                            estado = ESTADO_ERROR;
                        }else{
                            System.out.println("Fallo en el subscribe, nuevo intento");
                            estado = ESTADO_SUBSCRIPCION;
                            fallo=false;
                        }
                    }
                    break;
                case ESTADO_ERROR:
                    System.out.println("Se ha producido un error");
                    System.out.println("Host: " + host);
                    System.out.println("Mundo: " + nivel);
                    System.out.println("Performativa: "  + performative);
                    break;

                case ESTADO_CHECK:
                   // for (String vehiculo : tripulacion.keySet()) {
                        objeto = new JsonObject();
                        objeto.add("orden","checkin");
                        performative = ACLMessage.REQUEST;
                //    }
                    for(i = 0; i < agentes.size(); i++){
                        enviarMensaje(objeto, agentes.get(i), performative, conversationID, null);
                        System.out.print("Agente-"+i+": ");
                        exito = recibirMensaje();
                        if(!exito){
                            System.out.println("Un agente no quiere hacer check");
                        }
                    }
                    if(i!=4){
                        estado=ESTADO_BUSCANDO;
                    }else{
                        estado=ESTADO_FIN;
                    }


                case ESTADO_BUSCANDO:

                    switch (subEstados) {
                        case ELECCION_VEHICULO:
                            eleccionVehiculo();
                            subEstados = MOVER;
                            break;
                        case MOVER:
                            faseMover();
                            subEstados = OBSERVAR;
                            break;
                        case REPOSTAR:
                            //faseRepostar();  //TODO
                            // no se cambia de estado porque se llama desde faseMover
                            break;
                        case OBSERVAR:
                           // faseObservar(); //TODO después de moverse, añade al mapa lo que vea
                            subEstados = ELECCION_VEHICULO;
                            break;
                        case ENCONTRADO:
                           // faseObjetivoEncontrado();  //TODO
                            break;
                    }
                    break;
                case ESTADO_ENCONTRADO:
                    //TODO
                    estado=ESTADO_FIN;
                    break;
                case ESTADO_FIN:
                    System.out.println("CANCELANDO...");
                    objeto = new JsonObject();
                    objeto.add("", "");
                    performative = ACLMessage.CANCEL;
                    enviarMensaje(objeto, new AgentID(host), performative, conversationID, inReplyTo);
                    recibirMensaje();
                    recibirMensaje();
                    ejecutar=false;
                    break;
            }
               break;
        }
    }
<<<<<<< HEAD
    
    
    
    
   
=======




>>>>>>> 7d22e9d384b81911faf924afd8c6ef8af8235fcf
    /**
     * Elegimos el vehículo mas adecuado para la ocasión.
     * @author Miguel Keane
     */
    private void eleccionVehiculo() {
        System.out.println("\n\tELECCION VEHICULO.");

        // Si ya existe un vehiculo seleccionado:
        if (vehiculoSeleccionado) {

            Radar radar = tripulacion.get(vehiculoElegido).getRadar();
            int [][] matriz= radar.getMatriz();


            int posCentral = (matriz.length - 1) / 2;


            if (matriz[posCentral][posCentral] == 3) {

                System.out.println("Ha llegado al objetivo. Size de exploradores total: " + vehiculosExploradores.size());
                if (vehiculosExploradores.size() > 1) {
                    //tripulacion.get(vehiculoElegido).updateMap();
                    int[] pos = new int[2];
                    pos[0] = tripulacion.get(vehiculoElegido).getGPS().getX();
                    pos[1] = tripulacion.get(vehiculoElegido).getGPS().getY();
                    posicionAgenteFinalizado.add(pos);
                    //tripulacion.get(vehiculoElegido).updateMap();    //TODO: Terminar update map
                    vehiculosExploradores.remove(vehiculoElegido);
                    vehiculoElegido = vehiculosExploradores.get(0);
                    hay_camino = false;
                    camino_intentado = false;
                } else if (vehiculosExploradores.size() == 1) {
                   // TODO: Añadir un finalizar aquí
                }
            }
        } else {
            //Aquí elegiríamos el vehículo siguiente.
            vehiculoElegido = vehiculosExploradores.get(0);
            //Elegiremos el que tenga mayor rango de vision
            for(int i = 1 ; i < vehiculosExploradores.size() ; i++){
                Vehiculo p1 = tripulacion.get(vehiculosExploradores.get(i));
                Vehiculo p2 = tripulacion.get(vehiculoElegido);
                if(p1.getCombustible().getConsumo() < p2.getCombustible().getConsumo())
                    vehiculoElegido = vehiculosExploradores.get(i);
            }
            hay_camino = false;
            vehiculoSeleccionado = true;
            camino_intentado = false;
        }

        System.out.println("Fin de elección Vehículo.");

    }
    /*
    *
    * Author: Sergio López Ayala
    */
    private void pedirMapaVehiculo(Vehiculo v){
        // TODO cambiar id por ID del agente

        //Construimos el mensaje
        AgentID id = new AgentID("Agente");
        int performative = ACLMessage.QUERY_REF;
        JsonObject json = new JsonObject();
        json.add("request","mapa");
        enviarMensaje(json, id, performative, conversationID, inReplyTo);
        try{
            recibirMensaje();
        }catch(Exception e){

        }

    }
    
    /*
    *
    * Author: Sergio López Ayala
    */
    private void actualizarMapaVehiculo(Vehiculo v){
        AgentID id = new AgentID("Agente"); //todo cambiar id por el del vehiculo
        int performative = ACLMessage.INFORM;
        JsonObject json = new JsonObject();
        json.add("mapa", matrizCamino.toJson());
        enviarMensaje(json, id, performative, conversationID, inReplyTo);
        
    }
    
     private void faseMover() {
        System.out.println("empieza a mover");

        Vehiculo v = tripulacion.get(vehiculoElegido);
        int tipoVehiculo = v.getRol();  //1=avion, 2= coche, 3=camion



        int[] coord = new int[2];
        coord[0] = v.getGPS().getX();
        coord[1] = v.getGPS().getY();
        int[][] radar = v.getRadar().getMatriz();


        int vision = v.getRadar().getTamanio();


        int pasos = v.getPasos();

        /*
        Nos aseguramos si tenemos que repostar o no antes de movernos
         */

        if (v.getBateria().getCarga() < 10 || camino_local.size() == ((100 / v.getCombustible().getConsumo()) - v.getCombustible().getConsumo())) {
            //faseRepostar();
        }

        /*
        1 - Si el objetivo existe y hay camino, se hace eso
        2 - Si no existe un camino óptimo local, se calcula uno aquí.
        3 - Si ya existe un camino óptimo local, se van cogiendo los movimientos
            hasta que llegue a su casilla destino.
        4 - Actualizar Mapa
        5 - Acción de mover
         */



        String decision;
        contMovimientos++;
        System.out.println("contador: " + contMovimientos);

        /*
          Se decide que movimiento realizar
         */
        if (!camino_intentado && objetivos.size() > 0 && !v.getVolar()) {
            System.out.println("Calculando A*");


            int[] posicion_objetivo = new int[2];
            int[] gps = new int[2];
            gps[0] = tripulacion.get(vehiculoElegido).getGPS().getX();
            gps[0] = tripulacion.get(vehiculoElegido).getGPS().getY();

            posicion_objetivo = calcularObjetivoCercano(gps);

            Mapa matrizCopia = matrizCamino;
            for (int[] ap : this.posicionAgenteFinalizado) {
                matrizCopia.set(ap[0], ap[1], -1); // TODO cambiar al valor que sea
            }

            matrizCamino = matrizCopia;

            /*
            for (int i = 0; i < matrizCamino.length; i++) {
                for (int j = 0; j < matrizCamino[i].length; j++) {
                    System.out.print(matrizCamino[i][j] + " ");
                }
                System.out.println("");
            }
            */
            int id_vehiculo = v.getGPS().getX() * matrizCamino.getDimension() + v.getGPS().getY();
            int id_objetivo = posicion_objetivo[0] * matrizCamino.getDimension() + posicion_objetivo[1];
            camino = new Camino(matrizCamino, id_vehiculo, id_objetivo);
            camino_local.clear();
            camino_local = camino.getCamino();
            camino_intentado = true;
            if (camino_local.get(0) != -1) {
                hay_camino = true;
            }
        }
        if (!hay_camino) { //Si no existe un camino establecido pero tampoco se conoce el espacio entre el vehículo y ningún objetivo,
            System.out.println("Calculando nuevo camino");
            //cambio_de_vehiculo = false;

            System.out.println("vision: " + vision);
            caminosPosibles = new int[vision][vision];
            eliminarObjetivosInaccesibles(radar.clone(), vision);



            // Elegimos cual es la mejor casilla para moverse entre las disponibles

            //TODO: Crear esta funcion
            //int[] objetivo_alcanzar = elegirObjetivoLocal(pasos, coord, v.getNombre(), v.getMatrix());  //TODO añadir a Vehiculo
            //TODO ^
            //int objetivo_id = objetivo_alcanzar[0] * vision + objetivo_alcanzar[1];
            // Se calcula el camino optimo para llegar hasta ella
            //camino = new Camino(caminosPosibles, (int) floor((vision * vision) / 2.0), objetivo_id);
            camino_local.clear();
            camino_local = camino.getCamino();
        }
        cambiamosVehiculo = false;


        /**
         * Si ya existía un path óptimo:
         * -Calcula la posición a la que se debe de mover según el path
         * -Elimina dicha posición del path
         * -Una vez que se haya completado el path se vuelve a calcular uno
         */
        hay_camino = true;


        int primera_casilla = camino_local.get(0);
        int segunda_casilla = camino_local.get(1);
        int obj_prox_mov = primera_casilla - segunda_casilla;
        decision = pathLocalObj(obj_prox_mov);


        System.out.println("Mueve a " + decision);
        //}
        // Se transforman las IDs de las casillas a coordenadas para saber
        // identificar la dirección en la que se debe de mover
        // Se obtiene la dirección en la que moverse
        /**
         * **************
         * Una vez se sabe en que dirección se quiere mover:
         * -Actualiza el mapa de la base de datos
         * -Control sobre número de pasos y batería
         * -Manda la acción del movimiento
         * -Espera a recibir la respuesta
         */

        if (!decision.contains("move")) {
            System.out.println("No se donde moverme.");
        } else {



            if(camino_local.size() == 1 || v.getVolar()){  //Para el A*
               // v.updateMapa();
               //TODO: Update mapa
            }


            System.out.println("enviando mensaje a vehiculoElegido");
            JsonObject objeto = new JsonObject();
            objeto.add("command", decision);
           // enviarMensaje(vehiculoElegido, ACLMessage.REQUEST, objeto.toString());
           //TODO: Poner Enviar Mensaje correctamente

            enviarMensaje(objeto, new AgentID(vehiculoElegido), ACLMessage.REQUEST, conversationID,inReplyTo);

            System.out.println("mensaje enviado");
            try {
                ACLMessage mensaje = receiveACLMessage();
                if (mensaje.getPerformativeInt() != ACLMessage.INFORM) {
                    System.out.println("la performativa no contiene inform");
                    // Si al moverse no nos llega un INFORM posiblemente se haya estrellado.
                    System.out.println(mensaje.getPerformative() + ": " + mensaje.getContent());
                } else {
                    System.out.println("Mensaje recibido en mover(): " + mensaje.getContent());
                }
            } catch (InterruptedException ex) {
                System.err.println(ex.toString());
                estado = ESTADO_FIN;
            }
        }

        if (camino_local.size() > 0) {
            camino_local.remove(0);
            System.out.println("Borramos el camino local ");
        }
        // Si solo hay un camino local, es la del agente. Lo borramos.

        if (camino_local.size() == 1) {
            hay_camino = false;

        }

        v.darPasos();

        System.out.println("\t\tPaso numero: " + v.getPasos()); //Para ver cuantos pasos hemos dado con el vehículo


        System.out.println("Fin Mover");

    }

     /**
     *
     * @author Miguel Keane
     *
     * Devuelve el objetivo más cercano a una posición dada
     *
     * @return objetivo Devuelve las coordenadas del objetivo más cercano como
     * int[].
     */
    private int[] calcularObjetivoCercano(int[] gps) {
        int[] posicion_objetivo = new int[2];
        int[] minimo_objetivo = {100000, 100000};

        // Si se han encontrado 2 objetivos:
        for (int i = 0; i < objetivos.size(); i++) {


            // Calcula el gradiente en la posición del agente para cada objetivo
            int dist1 = Math.abs(objetivos.get(i)[0] - gps[0]) + Math.abs(objetivos.get(i)[1] - gps[1]);
            int dist2 = Math.abs(minimo_objetivo[0] - gps[0]) + Math.abs(minimo_objetivo[1] - gps[1]);

            // Si el objetivo nuevo es menor que el mínimo actual lo cogemos como destino
            if (dist1 < dist2) {
                minimo_objetivo[0] = objetivos.get(i)[0];
            }
            minimo_objetivo[1] = objetivos.get(i)[1];
        }
        posicion_objetivo[0] = minimo_objetivo[0];
        posicion_objetivo[1] = minimo_objetivo[1];
        return posicion_objetivo;
    }


    /**
     * Pone los objetivos inaccesibles que rodea al vehiclo a -1 y los accesibles a 0.
     *
     * @author Miguel Keane
     */
    private void eliminarObjetivosInaccesibles(int[][] radar, int vision) {
        int pos_inicial = (int) floor(vision / 2.0);
        eliminarObjetivosInaccesiblesRec(radar.clone(), vision, pos_inicial, pos_inicial);
        caminosPosibles[pos_inicial][pos_inicial] = -1;          //Pone la posicion actual a -1, no se deberia deliberar sobre ella.
        for (int i = 0; i < vision; i++) {                        //Pone los objetivos no alcanzados a -1, tampoco son accesibles.
            for (int j = 0; j < vision; j++) {
                if (caminosPosibles[i][j] == 0) {
                    caminosPosibles[i][j] = -1;
                }
            }
        }
        for (int i = 0; i < vision; i++) {                         //Pone los 1's a 0's dejando finalmente los accesibles a 0 y los inaccesibles a -1.
            for (int j = 0; j < vision; j++) {
                if (caminosPosibles[i][j] == 1) {
                    caminosPosibles[i][j] = 0;
                }
            }
        }
    }

    /**
     * Funcion recursiva de eliminarObjetivosInaccesibles
     *
     * @author Miguel Keane
     */
    private void eliminarObjetivosInaccesiblesRec(int[][] radar, int vision, int fila, int columna) {


        if (fila < 0 || fila > vision - 1 || columna < 0 || columna > vision - 1) {                         //Se encuentra fuera de los límites
        }
        else if (caminosPosibles[fila][columna] == -1 || caminosPosibles[fila][columna] == 1) {         //Aunque dentro de los límites ya ha sido recorrida

        } else {
            int pos_inicial = (int) floor(vision / 2.0);
            if ((fila != pos_inicial || columna != pos_inicial) && ((radar[fila][columna] == 1 && vision != 3) || radar[fila][columna] == 2 || radar[fila][columna] == 4 || (radar[fila][columna] == 3 /* TODO: && Hay algun agente en esa posicion */ && posicionAgenteFinalizado.contains(new int[]{fila,columna})))) {
                caminosPosibles[fila][columna] = -1;                                                   //Aunque alcanzable posee un obstáculo en este momento
                //System.out.println("inaccesible "+fila+columna);
            } else {
                //System.out.println("accesible "+fila+columna);
                caminosPosibles[fila][columna] = 1;                                                    //Es libre, alcanzable, y dentro de los límites
                eliminarObjetivosInaccesiblesRec(radar, vision, fila - 1, columna - 1);	//Superior izquierdo.
                eliminarObjetivosInaccesiblesRec(radar, vision, fila - 1, columna);         //Superior centro.
                eliminarObjetivosInaccesiblesRec(radar, vision, fila - 1, columna + 1);	//Superior derecho.
                eliminarObjetivosInaccesiblesRec(radar, vision, fila, columna - 1);         //Centro izquierdo.
                eliminarObjetivosInaccesiblesRec(radar, vision, fila, columna + 1);         //Centro derecho.
                eliminarObjetivosInaccesiblesRec(radar, vision, fila + 1, columna - 1);	//Inferior izquierdo.
                eliminarObjetivosInaccesiblesRec(radar, vision, fila + 1, columna);     	//Inferior centro.
                eliminarObjetivosInaccesiblesRec(radar, vision, fila + 1, columna + 1);	//Inferior derecho.
            }
        }
    }


    /**
     * *************************************************************************
     * @author Miguel Keane
     *
     * @param objetivo Recibe la diferencia entre el ID de la casilla en la que
     * se encuentra y la casilla a la que se moverá en el siguiente turno. El ID
     * 0 correspondería a la esquina superior izquierda. El ID 25
     * correspondería. a la esquina inferior derecha.
     *
     * @return mov Devuelve un String que contiene la dirección a la que
     * moverse.
     *
     */
    /*
    De esta función no hay que cambiar nada, funcionaría para cualquier tipo de matriz ya que
    lo que hace es desde la posición del agente y la siguiente casilla ID a moverse, calcular
    que dirección es. Trabaja con Id´s da igual el tamaño de la matriz.
     */
    private String pathLocalObj(int objetivo) {
        //System.out.println("TAMAÑO DEL MAPA > " + camino.getSizeMap());
        int[] diff_ids = {
            camino.getTamMapa(),
            camino.getTamMapa() - 1,
            camino.getTamMapa() + 1,
            -1,
            +1,
            -camino.getTamMapa() + 1,
            -camino.getTamMapa() - 1
        };
        String mov;

        if (objetivo == diff_ids[0]) {
            mov = "moveN";
        } else if (objetivo == diff_ids[1]) {
            mov = "moveNE";
        } else if (objetivo == diff_ids[2]) {
            mov = "moveNW";
        } else if (objetivo == diff_ids[3]) {
            mov = "moveE";
        } else if (objetivo == diff_ids[4]) {
            mov = "moveW";
        } else if (objetivo == diff_ids[5]) {
            mov = "moveSW";
        } else if (objetivo == diff_ids[6]) {
            mov = "moveSE";
        } else {
            mov = "moveS";
        }
        return mov;
    }










}
