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
 * @author Awake
 */
public class Agente extends SingleAgent{
    private Vehiculo miVehiculo;
    private Radar miRadar;
    private GPS miGPS;
    private Escaner miEscaner;
    private Mapa miMapa;
    private Bateria miBateria;
    
    public Agente(AgentID aid, String host) throws Exception{
        super(aid);
    }
    
    public void recibirMensaje(){
        ACLMessage inbox;
        try {
            inbox = receiveACLMessage();
            JsonObject objeto = Json.parse(inbox.getContent()).asObject();
            if( objeto.get("radar") != null){
                //miRadar.parsearCoordenadas(objeto);
            }else if( objeto.get("gps") != null){
                //miGPS.parsearCoordenadas(objeto);
            }else if( objeto.get("result") != null){
                gestionResultados(objeto);
            }else if( objeto.get("trace") != null){
                JsonArray ja = objeto.get("trace").asArray();
                byte data[] = new byte[ja.size()];
                for(int i=0; i<data.length; i++){
                    data[i]=(byte) ja.get(i).asInt();
                }
                String title;
                title = String.format("traza de "+miMapa+".png");
                FileOutputStream fos = new FileOutputStream(title);
                fos.write(data);
                fos.close();
                System.out.println("Traza Guardada como 'Traza.png'");
            }
        }catch(InterruptedException exception){
            System.err.println("Error al percibir");
            System.err.println(exception.toString());
        }catch(IOException ex){
            System.err.println("Excepción al hacer la traza");
        }
    }
    
    private void gestionResultados(JsonObject obj){
        
    }
}
