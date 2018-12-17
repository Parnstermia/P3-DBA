package practica3;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

/**
 *
 * @author 
 */
public class Radar {
	private int matriz[][];
	private int tamanio;
	
        
	public Radar(int tamanio){
		matriz = new int[tamanio][tamanio];
		this.tamanio = tamanio;
	}
	
	public int getTamanio(){
		return tamanio;
	}
	
	public int[][] getMatriz(){
		return matriz;
	}
        
         /**
          * Método para convertir la información del radar de Json a una matriz. 
        * @param objeto mensaje devuelto por el agente externo con información
        * relativa a el sistema radar
        * @author Miguel Keane Cañizares
        */
        public void parsearCoordenadas(JsonObject objeto){
          /*
            for (int i=0; i<tamanio; i++){
                for (int j=0; i<tamanio; j++){
                    JsonValue V = objeto.get("radar").asArray();
                }
            }
            */
            
            int i=0, k=0; 
            for (JsonValue j : objeto.get("radar").asArray()){
                if(k==tamanio){
                    i++;
                    k=0;
                }
                matriz[i][k]= j.asInt();
                k++;
            } 

        }
}
