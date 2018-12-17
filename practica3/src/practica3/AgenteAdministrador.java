package practica3;


import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Thomas LESBROS
 */
public class AgenteAdministrador extends Agente{

    private static final int ESTADO_SUBSCRIPCION =1;
    private static final int ESTADO_ERROR =2;
    private static final int ESTADO_PROPAGACION =3;
    private ArrayList<AgentID> agentes;
    private String nivel;
    private int performative;
    private int estado;
    private int i;
    private HashMap<String, Vehiculo> tripulacion; //Aquí guardaremos todos los vehículos que tengamos en funcionamiento.
    private boolean ejecutar = true;
    private boolean fallo = false;


    public AgenteAdministrador(AgentID aid, String host, String nivel, ArrayList<AgentID> subditos) throws Exception{
        super(aid, host);
        this.nivel = nivel;
        estado = ESTADO_SUBSCRIPCION;
        agentes = subditos;
    }

    /**
     * @author Thomas LESBROS
     * @return
     */
    @Override
    public boolean recibirMensaje(){
        ACLMessage inbox;
        try {
            inbox = receiveACLMessage();
            JsonObject objeto = Json.parse(inbox.getContent()).asObject();
            if( objeto.get("result") != null){
                conversationID = inbox.getConversationId();
                return true;
            }else if( objeto.get("trace") != null){
                this.fallo=true;
                return false;
            }else if(objeto.get("details") != null){
                System.out.println("Fallo");
                this.fallo=false;
                return false;
            }else{
                System.out.println("Fallo");
                this.fallo=false;
                return false;
            }

        }catch(InterruptedException e){
            System.err.println(e.toString());
            return false;
        }
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
                        System.out.println("Exito en el subscribe");
                        estado = ESTADO_PROPAGACION;
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
                case ESTADO_PROPAGACION:
                    objeto = new JsonObject();
                    objeto.add("orden","checkin");
                    performative = ACLMessage.REQUEST;
                    for(i = 0; i < agentes.size(); i++){
                        System.out.println("Checkin...");
                        enviarMensaje(objeto, agentes.get(i), performative, conversationID, null);
                        System.out.print("Agente-"+i+": ");
                        exito = recibirMensaje();
                        if(!exito){
                            System.out.println("Un agente no quiere hacer check");
                        }
                    }
                    if(i!=4){
                        ejecutar=true;
                    }else{
                        ejecutar=false;
                    }


                    break;
            }

        }
    }
}
