package practica3;


import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * @author Sergio López Ayala
 */
public class Mapa {
    private char[] matriz;
    private int miDimension; //10 = 10x10
    private int nCeldas;
    
    public Mapa(int dimension){
        miDimension = dimension;
        nCeldas = miDimension*miDimension;
        matriz = new char[miDimension];
        Arrays.fill(matriz, 'D');
    }
    
    public Mapa(){
        miDimension = 100;
        nCeldas = miDimension*miDimension;
        matriz = new char[miDimension];
        Arrays.fill(matriz, 'D');
    }
    
    public char get(int x, int y)throws IndexOutOfBoundsException{
        if(x > miDimension || y > miDimension)
            throw new IndexOutOfBoundsException();
        else{
            return(matriz[x+x*y]);
        }
    }
    
    public void set(int x, int y, char val)throws IndexOutOfBoundsException{
        if(x > miDimension || y > miDimension)
            throw new IndexOutOfBoundsException();
        else{
            matriz[x+x*y] = val;
        }
    }
    
    public void redimensionar(int nuevaDimension){
        if(nuevaDimension > miDimension){
            int nuevasCeldas = nuevaDimension*nuevaDimension;
            char[] nuevaMatriz = new char[nuevasCeldas];
            Arrays.fill(nuevaMatriz, 'D');
            
            int offset = 0;
            for(int i = 0; i < nCeldas; i++){
                offset = (i%miDimension)*(nuevaDimension-miDimension);
                nuevaMatriz[i+offset] = matriz[i];
            }
            matriz = nuevaMatriz;
            miDimension = nuevaDimension;
            nCeldas = nuevasCeldas;
        }
    }
    
    public int getDimension(){
        return miDimension;
    }
    public int getCeldas(){
        return nCeldas;
    }
    
    
    public ArrayList<ValorRLE> compresionRLE(){
        ArrayList<ValorRLE> mapaComprimido = new ArrayList<>();
        char anterior = matriz[0];
        int repeticiones = 0;
        for(int i = 0; i < nCeldas; i++){
            if(anterior == matriz[i]){
                repeticiones++;
            }else{
                mapaComprimido.add(new ValorRLE(anterior,repeticiones));
                anterior = matriz[i];
                repeticiones = 1;
            }
        }
        mapaComprimido.add(new ValorRLE(anterior,repeticiones));
        
        return mapaComprimido;
    }
    
    public void descompresionRLE(ArrayList<ValorRLE> mapaComprimido){
        int veces = 0;
        for(int i = 0 ; i < mapaComprimido.size(); i++){
            veces+= mapaComprimido.get(i).veces;
        }
        
        char nuevaMatriz[] = new char[veces];
        int indice = 0;
        for(int i = 0 ; i < mapaComprimido.size(); i++){
            ValorRLE valor = mapaComprimido.get(i);
            for(int j = 0; j < valor.veces; j++, indice++)
                nuevaMatriz[indice] = valor.caracter;
        }
        matriz = nuevaMatriz;
        nCeldas = veces;
        miDimension = (int) Math.sqrt(veces);
    }
    
    public class ValorRLE{
        public char caracter;
        public int veces;
        
        public ValorRLE(char c, int v){
            caracter = c;
            veces = v;
        }
        
        @Override
        public boolean equals(Object obj){
            if (obj == null)
                return false;
            if (obj == this)
                return true;
            if (!(obj instanceof ValorRLE))
                return false;
            ValorRLE other = (ValorRLE)obj;
            return other.caracter == caracter && 
                   other.veces == veces;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 23 * hash + this.caracter;
            hash = 23 * hash + this.veces;
            return hash;
        }
    }
}
