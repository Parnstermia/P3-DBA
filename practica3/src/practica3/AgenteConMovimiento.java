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
	private Mapa miMapa;
	
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
			JsonObject objeto = Json.parse(inbox.getContent()).asObject();

			if( objeto.get("result") != null){
				String a = objeto.toString();
				String c;
				c = a.substring(42, 43);
				inReplyTo=inbox.getReplyWith();
				if(c.equals("1")){
					 System.out.println("Coche");
				}else if(c.equals("2")){
					 System.out.println("Avion");
				}else{
					 System.out.println("Camion");
				}
				exito = true;
			}else if(objeto.get("details") != null){
				exito = false;
			}else if(objeto.get("orden") != null){
				String orden =  objeto.get("orden").asString();
				nextOrder = orden;
				conversationID = inbox.getConversationId();
				exito = true;
			}else if( objeto.get("request") != null){
				String peticion = objeto.get("request").toString();
				JsonObject respuesta = new JsonObject();
				int performative;
				switch (peticion){
					case "mapa":
						respuesta.add("mapa", miMapa.toJson());
						performative = ACLMessage.INFORM;
						enviarMensaje(respuesta, inbox.getSender(), performative, conversationID, inReplyTo);
						break;
				}
			}else if ( objeto.get("mapa") != null){
				miMapa.parseJson(objeto.get("mapa").asArray());
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
					exito = recibirMensaje();
					if(exito){
						if(nextOrder != null){
							performative = ACLMessage.REQUEST;
							objeto = new JsonObject();
							objeto.add("command", nextOrder);
							enviarMensaje(objeto, new AgentID(host), performative, conversationID, null);
							nextOrder = null;
							estado = ESTADO_INICIO;
						}
						else{
							objeto = new JsonObject();
							objeto.add("result", "OK");
							performative = ACLMessage.INFORM;
							enviarMensaje(objeto, new AgentID("Agente"), performative, conversationID, null);
							break;
						}
						break;
					}
			}
		}
	}
}
