package practica3;

/**
 *
 * @author 
 */
public class Coche extends VehiculoTierra {
	public Coche(String nombre, int id){
		super(nombre,id);
		combustible=new Combustible(1);
		radar=new Radar(5);
	}
}
