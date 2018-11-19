import es.upv.dsic.gti_ia.core.AgentID;
import es.upv.dsic.gti_ia.core.AgentsConnection;
import org.apache.log4j.BasicConfigurator;

import java.util.ArrayList;
import org.apache.log4j.BasicConfigurator;

/**
 *
 * @author 
 */
public class Main {

    public static void main(String[] args) {
        Agente roboto;
        BasicConfigurator.configure();
        ////////////////////////////////////////////
        // No hacer push a los datos del grupo
        String virtualhost = "";
        String username = "";
        String pass = "";
        ////////////////////////////////////////////



        String mapa = "";
        
        BasicConfigurator.configure();
		  
        AgentsConnection.connect("isg2.ugr.es",6000, virtualhost, username, pass, false);
        
        try {

            roboto = new Agente(new AgentID("Agente"), virtualhost);

            roboto.start();
        } catch (Exception ex) {
            System.err.println("Error creando agentes");
            System.exit(1);
        }
    }
    
}