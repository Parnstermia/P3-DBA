package practica3;

/**
 *
 * @author 
 */
public class Camion extends VehiculoTierra {
	public Camion(String nombre, int id){
		super(nombre,id);
		combustible=new Combustible(4);
		radar=new Radar(11);
	}
}
