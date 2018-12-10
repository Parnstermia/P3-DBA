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
	
	void setX(int x){
		this.x=x;
	}
	
	int getX(){
		return x;
	}
	
	void setY(int y){
		this.y=y;
	}
	
	int getY(){
		return y;
	}
}
