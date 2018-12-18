/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica3;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 *  Clase que encapsule todo el uso de JSON para facilitar su uso y entendimiento
 * 
 * @author Miguel Keane Cañizares
 */
public class JSON {
    
    private String llave; 
    
     /**
     * Funcion para registrarnos en el mundo
     * 
     * @author Miguel Keane Cañizares
     * 
     * @param mundo Nombre del mapa.
     * @return Cadena con el Json codificado.
     */
    public static String suscribirse(String mundo) {
        JsonObject objeto = new JsonObject();
        objeto.add("world", mundo);
        return objeto.toString();
    }
    
    
     /**
     * Función que guarda la llave proporcionada por el servidor. 
     * @author Miguel Keane
     * 
     * @param llave Contenido de la llave en JSON
     */
    public  void leerLlave(String llave) {
        JsonObject objeto = Json.parse(llave).asObject();
        this.llave = objeto.getString("result", null);
        //System.out.println("Key obtenida: " + key);
        //return key;
    }
    
    /**
     * Función que genera la cadena necesaria para hacer login en el mundo.
     * @author Miguel Keane
     * @return Cadena con el Json descodificado.
     */
    public static String checkin() { // checkin
        JsonObject objeto = new JsonObject();
        objeto.add("command", "checkin");
        return objeto.toString();
    }
       
    
    /**
     * Funcion que genera la cadena necesaria para hacer un movimiento.
     * @param direccion Movimiento que se quiere realizar.
     * @author Miguel Keane Cañizares
     * @return Cadena con el Json descodificado.
     */
    public static String mover(String direccion) {
        JsonObject objeto = new JsonObject();
        objeto.add("command", direccion);
        return objeto.toString();
    }
    
    
    /**
     * Función que genera la cadena necesaria para repostar.
     * @author Miguel Keane
     * @return Cadena con el Json codificado.
     */
    public static String repostar() {
        JsonObject objeto = new JsonObject();
        objeto.add("command", "refuel");
        return objeto.toString();
    }
    
    
     /**
     * Coge los datos del sensor en JSON y los parsea a una matriz
     * @param vector JsonArray con la info del radar.
     * @author Miguel Keane
     * @return Matriz de enteros correspondiente al radar.
     */
    public static int[][] parseSensor(JsonArray vector) {
        final int TAM = (int) Math.sqrt(vector.size());
        int[][] matriz = new int[TAM][TAM];
        int i=0, j=0;
        for(JsonValue valor : vector){
            matriz[i][j] = valor.asInt();
            j++;
            if(j == TAM) {
                j=0;
                i++;
            }
        }
        return matriz;
    }
    
  
       
    
    
}
