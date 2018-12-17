package practica3;

/**
 *
 * @author 
 */
public class Volador extends VehiculoAire {
	public Volador(){
		combustible=new Combustible(2);
		radar=new Radar(3);
		gps=new GPS();
		escaner=new Escaner();
	}
}
