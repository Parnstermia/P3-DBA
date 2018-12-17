
package practica3;

import com.eclipsesource.json.JsonObject;

/**
 *
 * @author 
 */
public class GPS {
    protected int x;
    protected int y;
    
     /**
     *
     * @author Sergio López Ayala
     */
    public GPS(){
        x = 0;
        y = 0;
    }
    /**
    * @param objeto Mensaje recibido por el servidor
    * @author Miguel Keane Cañizares
    */
    public void parsearCoordenadas(JsonObject objeto){
        x = objeto.get("gps").asObject().get("x").asInt();
        y = objeto.get("gps").asObject().get("y").asInt();
    }
    

    /**
     * Metodo get para acceder a la x.
     * @author Miguel Keane Cañizares 
    */
    public int getX(){
        return this.x;
    }
    
     /**
     * Metodo get para acceder a la y.
     * @author Miguel Keane Cañizares
    */
    public int getY(){
        return this.y;
    }
   
	/**
     * Método para devolver los datos del GPS en un String
    * 
    * @author Miguel Keane
    */ 
	 @Override
	 public String toString(){
		 String str="["+x+","+y+"]";
		 return str;
	 }
}
