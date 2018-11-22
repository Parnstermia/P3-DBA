/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TestsMapa;

import java.util.ArrayList;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import practica3.Mapa;

/**
 *
 * @author Awake
 */
public class mapaTest {
    @Test
    public void pruebaEquals(){
        Mapa mapa = new Mapa();
        Mapa mapa2 = new Mapa();
        Assert.assertEquals(mapa.getDimension(), mapa2.getDimension());
    }
    
    @Test
    public void pruebaEquals2(){
        Mapa mapa1 = new Mapa();
        Mapa mapa2 = new Mapa();
        mapa1.set(1, 1, 'D');
        mapa2.set(1, 1, 'D');
        Assert.assertEquals(mapa1.getDimension(), mapa2.getDimension());
    }
    
    @Test
    public void pruebaRedimension(){
        Mapa mapa = new Mapa();
        mapa.redimensionar(20);
        Assert.assertEquals(mapa.getDimension(), 20);
    }
    
    @Test
    public void pruebaCompresion1(){
        Mapa mapa = new Mapa();
        
        ArrayList<Mapa.ValorRLE> arr = mapa.compresionRLE();
        Assert.assertEquals(1, arr.size());
    }
    
    @Test
    public void pruebaCompresion2(){
        Mapa mapa1 = new Mapa();
        mapa1.set(1, 1, 'L');
        ArrayList<Mapa.ValorRLE> arr = mapa1.compresionRLE();
        
        Mapa mapa2 = new Mapa();
        mapa2.descompresionRLE(arr);
        
        Assert.assertEquals(mapa1, mapa2);
        Assert.assertEquals(3,arr.size());
    }
}
