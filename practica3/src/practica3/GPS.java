package practica3;

/**
 *
 * @author Diego Alfonso Candelaria Rodríguez
 */
public class GPS {
	private int x;
	private int y;
	
	public GPS(){
		x=0;
		y=0;
	}
	
	public GPS(int x, int y){
		this.x=x;
		this.y=y;
	}
	
	public void setX(int x){
		this.x=x;
	}
	
	public int getX(){
		return x;
	}
	
	public void setY(int y){
		this.y=y;
	}
	
	public int getY(){
		return y;
	}
}