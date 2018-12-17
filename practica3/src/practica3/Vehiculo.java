package practica3;

/**
 *
 * @author 
 */
public abstract class Vehiculo {
	protected Radar radar;
	protected Combustible combustible;
        protected Bateria bateria; 
	protected GPS gps;
	protected Escaner escaner;
        protected boolean volar;  //TODO: Añadir volar a constructor. 
        protected int pasos;
	
        public Vehiculo(){
            //TODO: Inicializar vehículo correctamente
            int pasos=0; 
        }
        
	public abstract Direccion llegarMeta();
	
	public void consumirCombustible(){
		combustible.consumir();
	}
        public Radar getRadar(){
            return this.radar;
        }
        public GPS getGPS(){
            return this.gps;
        }
        public Escaner getEscaner(){
            return this.escaner;
        }
        
        public Combustible getCombustible(){
            return this.combustible;
        }
        
        public int getRol() {
       
            if(combustible.getConsumo()==2 && radar.getTamanio()==3 && volar) 
                return 0; //AVion
            else if(combustible.getConsumo()==1 && radar.getTamanio()==5 && !volar) 
                return 1; //COChe
            else
                return 2; //CAMION
        
         }  
        public int getPasos(){
            return this.pasos;
        }
        public void darPasos(){
            pasos++;
        }
        public boolean getVolar(){
            return this.volar;
        }
        
        public Bateria getBateria(){
            return this.bateria; 
        }
                
        
        
}