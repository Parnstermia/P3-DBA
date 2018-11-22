package TestsMapa;


import practica3.Mapa;
import java.util.ArrayList;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Awake
 */
public class PruebaMapa extends TestCase{
    Mapa m = new Mapa();
    Mapa m2 = new Mapa();
    ArrayList<Mapa.ValorRLE> mapaComprimido = new ArrayList<>();
    ArrayList<Mapa.ValorRLE> mapaComprimido2 = new ArrayList<>();
    
    @Before
    public void setUp(){
        m.set(1, 10, 'L');
        m.set(2, 20, 'L');
        
    }
    
    @Test
    public void compressionTest() {
        mapaComprimido = m.compresionRLE();
        for(int i = 0; i < mapaComprimido.size(); i++){
            Mapa.ValorRLE v = mapaComprimido.get(i);
            System.out.println("Caracter : " + v.caracter +
                    ", Veces : " + v.veces);
        }
        
        m.descompresionRLE(mapaComprimido);
        
        mapaComprimido2 = m.compresionRLE();
        for(int i = 0; i < mapaComprimido2.size(); i++){
            Mapa.ValorRLE v = mapaComprimido2.get(i);
            System.out.println("Caracter : " + v.caracter +
                    ", Veces : " + v.veces);
        }
        assertEquals(mapaComprimido, mapaComprimido2);
    }

}
