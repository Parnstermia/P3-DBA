package practica3;


import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import es.upv.dsic.gti_ia.core.ACLMessage;
import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 *
 * @author Sergio López Ayala / Thomas LESBROS
 */
public class Agente extends SingleAgent{
    protected String host;
    protected String conversationID;
    protected String inReplyTo;
    
    public Agente(AgentID aid, String host) throws Exception{
        super(aid);
        this.host = host;
    }
    
    public boolean recibirMensaje(){
        ACLMessage inbox;
        try {
            inbox = receiveACLMessage();
            JsonObject objeto = Json.parse(inbox.getContent()).asObject();
            if( objeto.get("radar") != null){
                //miRadar.parsearCoordenadas(objeto);
            }else if( objeto.get("gps") != null){
                //miGPS.parsearCoordenadas(objeto);
            }else if( objeto.get("result") != null){
                //gestionResultados(objeto);
            }else if( objeto.get("trace") != null){
                JsonArray ja = objeto.get("trace").asArray();
                byte data[] = new byte[ja.size()];
                for(int i=0; i<data.length; i++){
                    data[i]=(byte) ja.get(i).asInt();
                }
                String title;
                title = String.format("traza de "+"miMapa"+".png");
                FileOutputStream fos = new FileOutputStream(title);
                fos.write(data);
                fos.close();
                System.out.println("Traza Guardada como: "+title);
            }
        }catch(InterruptedException exception){
            System.err.println("Error al percibir");
            System.err.println(exception.toString());
        }catch(IOException ex){
            System.err.println("Excepción al hacer la traza");
        }
        return true;
    }
    //String receiver, int performativa, String contenido){
    public void enviarMensaje(JsonObject objeto, AgentID receiver, int performative, String conversationID, String inReplyTo){
        ACLMessage outbox = new ACLMessage();
        outbox.setSender(this.getAid());
        outbox.setReceiver(receiver);
        outbox.setContent(objeto.toString());
        if( conversationID != null)
            outbox.setConversationId(conversationID);
        if( inReplyTo != null)
            outbox.setInReplyTo(inReplyTo);
        outbox.setPerformative(performative);
        
        this.send(outbox);
    }
    private void gestionResultados(JsonObject obj){
        
    }
}
