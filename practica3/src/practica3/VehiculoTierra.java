package practica3;

/**
 *
 * @author 
 */
public abstract class VehiculoTierra extends Vehiculo {
	public VehiculoTierra(String nombreVehiculo, int vehiculoID) {
		super(nombreVehiculo, vehiculoID);
	}
	
	public Direccion llegarMeta(){
		Direccion direccion=Direccion.N;
		
		
		
		return direccion;
	}
}