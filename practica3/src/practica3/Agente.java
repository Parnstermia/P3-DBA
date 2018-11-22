package practica3;


import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.SingleAgent;


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
    
    
}
