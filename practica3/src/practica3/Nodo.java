/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica3;

/**
 * @author Miguel Keane
 * 
 * Clase que se comporta como un nodo. Cada nodo tiene un identificador ID, el 
 * identificador ID de su padre y lo costoso que sería desplazarse hacía él.
 */


public class Nodo {
    private int id_nodo;
    private int id_padre;
    private int coste;
    /**
     * Constructor
     * 
     * @param id_nodo ID del nodo 
     * @param id_padre ID de su padre
     * @param coste Como de costoso sería moverse al nodo
     */
    public Nodo( int id_nodo, int id_padre, int coste){
        this.id_nodo = id_nodo;
        this.id_padre = id_padre;
        this.coste = coste;
    }
    /**
     * Obtiene el ID del nodo
     * @return id_nodo
     */
    public int getId(){
        return id_nodo;
    }
    /**
     * Obtiene el ID del padre del nodo
     * @return id_padre
     */
    public int getParentId(){
        return id_padre;
    }
    /**
     * Obtiene el coste del nodo
     * @return coste
     */
    public int getCost(){
        return coste;
    }
    /**
     * Calcula la coordenada X que le correspondería en la matriz de mapa
     * @return Devuelve la coordenada X como int
     */
    public int getCoorX(){
        return id_nodo % 5;
    }
    /**
     * Calcula la coordenada Y que le correspondería en la matriz de mapa
     * @return Devuelve la coordenada Y como int
     */
    public int getCoorY(){
        return id_nodo % 5;
    }
}