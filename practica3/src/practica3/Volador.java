package practica3;

/**
 *
 * @author 
 */
public class Volador extends VehiculoAire {

    /**
     *
     * @param nombre
     * @param id
     */
    public Volador(String nombre, int id){
		combustible=new Combustible(2);
		radar=new Radar(3);
		gps=new GPS();
		escaner=new Escaner();
	}
}
