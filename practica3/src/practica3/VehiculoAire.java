package practica3;

/**
 *
 * @author 
 */
public abstract class VehiculoAire extends Vehiculo {
	protected Radar radar;
	protected Combustible combustible;
	protected GPS gps;
	protected Escaner escaner;

    public VehiculoAire(String nombreVehiculo, int vehiculoID) {
        super(nombreVehiculo, vehiculoID);
    }
	
	@Override
	public Direccion llegarMeta(){
		Direccion direccion=Direccion.N;
		double menor=escaner.getMatriz()[0][0];
		int xMenor=0;
		int yMenor=0;
		for (int i=0 ; i<escaner.getTamanio() ; i++)
			for(int j=0 ; j<escaner.getTamanio() ; j++)
				if(escaner.getMatriz()[i][j] < menor){
					menor=escaner.getMatriz()[i][j];
					xMenor=i;
					yMenor=j;
				}
		
		//Esquina superior izquierda
		if(xMenor==0 && yMenor==0)
			direccion=Direccion.NW;
		
		//Arriba
		if(xMenor==0 && yMenor==1)
			direccion=Direccion.N;
		
		//Esquina superior derecha
		if(xMenor==0 && yMenor==2)
			direccion=Direccion.NE;
		
		//Izquierda
		if(xMenor==1 && yMenor==0)
			direccion=Direccion.W;
		
		//Derecha
		if(xMenor==1 && yMenor==2)
			direccion=Direccion.E;
		
		//Esquina inferior izquierda
		if(xMenor==2 && yMenor==0)
			direccion=Direccion.SW;
		
		//Abajo
		if(xMenor==2 && yMenor==1)
			direccion=Direccion.S;
		
		//Esquina inferior derecha
		if(xMenor==2 && yMenor==2)
			direccion=Direccion.SW;
		
		return direccion;
	}
}