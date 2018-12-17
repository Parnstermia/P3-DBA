package practica3;

/**
 *
 * @author 
 */
public class Coche extends VehiculoTierra {
	public Coche(){
		combustible=new Combustible(1);
		radar=new Radar(5);
		gps=new GPS();
		escaner=new Escaner();
	}
}
