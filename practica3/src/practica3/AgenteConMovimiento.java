/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica3;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;

/**
 *
 * @author Awake / Thomas LESBROS
 */
public class AgenteConMovimiento extends Agente{
    private static final int ESTADO_INICIO = 0;
    private Vehiculo miVehiculo;
    private Radar miRadar;
    private GPS miGPS;
    private Escaner miEscaner;
    private Mapa miMapa;
    private Bateria miBateria;
    
    private int estado;
    private boolean ejecutar = true;
    private String nextOrder;
    private int performative;
    public AgenteConMovimiento(AgentID aid, String host) throws Exception {
        super(aid, host);
        estado = ESTADO_INICIO;
        nextOrder = null;
    }
    
    
    @Override
    public boolean recibirMensaje(){
        boolean exito = false;
        ACLMessage inbox;
        try {
            inbox = receiveACLMessage();
            conversationID = inbox.getConversationId();
            System.out.println("convID recibido");
            System.out.println(conversationID);
            JsonObject objeto = Json.parse(inbox.getContent()).asObject();
            System.out.println(objeto);
            if( objeto.get("result") != null){
                inReplyTo=inbox.getReplyWith();
                exito = true;
            }else if(objeto.get("details") != null){
                exito = false;
            }else if(objeto.get("orden") != null){
                String orden =  objeto.get("orden").asString();
                nextOrder = orden;
                conversationID = inbox.getConversationId();
                System.out.println("convID recibido si hay un result");
                System.out.println(conversationID);
                exito = true;
            }
            
        }catch(InterruptedException e){
            System.err.println(e.toString());
        }
        
        return exito;
    }
    
    @Override
    public void execute(){
        boolean exito;
        JsonObject objeto;
        while(ejecutar){
            switch(estado){
                case ESTADO_INICIO:
                    System.out.println("Estado inicio del agente");
                    exito = recibirMensaje();
                    if(exito){
                        if(nextOrder != null){
                            System.out.println("Va a hacer el checkin");
                            performative = ACLMessage.REQUEST;
                            objeto = new JsonObject();
                            objeto.add("command", nextOrder);
                            System.out.println(objeto);
                            enviarMensaje(objeto, new AgentID(host), performative, conversationID, null);
                        }
                        else{
                            System.out.println("Informa al admin");
                            objeto = new JsonObject();
                            objeto.add("result", "OK");
                            System.out.println(objeto);
                            performative = ACLMessage.INFORM;
                            enviarMensaje(objeto, new AgentID(host), performative, conversationID, null);
                            System.out.println("Todo bien hasta aqui");
                            //performative = ACLMessage.CANCEL;
                            //enviarMensaje(objeto, new AgentID(host), performative, conversationID, null);
                        }
                    }
                    break;
            }
        }
    }
}
