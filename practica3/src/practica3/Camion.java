package practica3;

/**
 *
 * @author 
 */
public class Camion extends VehiculoTierra {
	public Camion(){
		combustible=new Combustible(4);
		radar=new Radar(11);
		gps=new GPS();
		escaner=new Escaner();
	}
}
