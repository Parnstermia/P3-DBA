package practica3;

/**
 *
 * @author 
 */
public class Volador extends VehiculoAire {
   public Volador(String nombre, int id){
		super(nombre,id);
		combustible=new Combustible(2);
		radar=new Radar(3);
	}
}
