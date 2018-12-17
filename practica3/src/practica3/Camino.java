/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica3;

import java.util.ArrayList;


/**
 *
 * @author Miguel Keane
 *
 * Clase que calcula el camino óptimo desde una posición A a otra B en un mapa
 * utilizando el algoritmo A*.
 *
 */
public class Camino {

    private ArrayList<Integer> camino_objetivo = new ArrayList<>();
    private ArrayList<Nodo> open_list = new ArrayList<>();
    private ArrayList<Nodo> close_list = new ArrayList<>();
    private int objetivo_id;
    private int start_id;
    private int[][] mapa;
    private int mapa_tam;

    /**
     * Constructor
     *
     * @param mapa Mapa
     * @param agente_id ID posición actual
     * @param objetivo_id ID posición destino
     */
    public Camino(int[][] mapa, int agente_id, int objetivo_id) {
        this.mapa_tam = mapa.length;
        this.mapa = mapa;
        this.mapa[objetivo_id / this.mapa_tam][objetivo_id % this.mapa_tam] = 2;
        this.start_id = agente_id;
        this.objetivo_id = objetivo_id;
    }

    /**
     * Actualiza el ID del comienzo
     *
     * @param nodo_id ID nuevo
     */
    public void changeStart(int nodo_id) {
        this.start_id = nodo_id;
    }

    /**
     * Actualiza el ID del objetivo
     *
     * @param nodo_id ID nuevo
     */
    public void changeObjetive(int nodo_id) {
        this.objetivo_id = nodo_id;
    }

    /**
     * Actualiza el mapa
     *
     * @param map Mapa nuevo
     */
    public void changeMap(int[][] map) {
        this.mapa = map;
        this.mapa_tam = map.length;
    }

    /**
     * Devuelve el tamaño del mapa
     *
     * @return mapa_tam
     */
    public int getTamMapa() {
        return this.mapa_tam;
    }

    /**
     * Calcula el coste del nodo actual hasta la posición del nodo inicial
     *
     * @param nodo_id ID del nodo a saber su coste
     * @return 1 Si el nodo está en la cercanía de la posición inicial 2 Si el
     * nodo está más alejado
     */
    private int costeNodoActual(int nodo_id) {
        int coord_i = nodo_id % this.mapa_tam;
        int coord_j = nodo_id % this.mapa_tam;
        int obj_i = this.start_id % this.mapa_tam;
        int obj_j = this.start_id % this.mapa_tam;

        return Math.abs(coord_i - obj_i) + Math.abs(coord_j - obj_j);
    }

    /**
     * Calcula el coste del nodo actual hasta la posición del nodo objetivo
     *
     * @param nodo_id ID del nodo
     * @return Coste 
     */
    private int costeNodoObjetivo(int nodo_id) {
        int coord_i = nodo_id / this.mapa_tam;
        int coord_j = nodo_id % this.mapa_tam;
        int obj_i = this.objetivo_id / this.mapa_tam;
        int obj_j = this.objetivo_id % this.mapa_tam;

        return Math.abs(coord_i - obj_i) + Math.abs(coord_j - obj_j);
    }

    /**
     * Calcula el coste total de moverse a un Nodo dado
     *
     * @param nodo_id ID del nodo a saber su coste
     * @return Coste total
     */
    private int calcularCosteNodo(int nodo_id) {
        return costeNodoActual(nodo_id) + costeNodoObjetivo(nodo_id);
    }

    /**
     * Comprueba si un Nodo se encuentra dentro de open_list
     *
     * @param nodo_id ID del nodo a comprobar.
     * @return true si se encuentra, false en caso contrario
     */
    private boolean containsNodoOpenList(int nodo_id) {
        boolean contiene = false;
        for (Nodo nodo_actual : open_list) {
            if (nodo_actual.getId() == nodo_id) {
                contiene = true;
            }
        }
        return contiene;
    }

    /**
     * Compreuba si un Nodo se encuentra dentor de close_list
     *
     * @param nodo_id ID del nodo a comprobar
     * @return exist true si se encuentra, false en caso contrario
     */
    private boolean containsNodoCloseList(int nodo_id) {
        boolean contiene = false;
        for (Nodo nodo_actual : close_list) {
            if (nodo_actual.getId() == nodo_id) {
                contiene = true;
            }
        }
        return contiene;
    }

    /**
     * Comprueba si un Nodo se encuentra en alguna lista open_list o
     * close_list
     *
     * @param nodo_id ID del nodo a comprobar
     * @return true si se encuentra en alguna, false en caso contrario
     */
    private boolean containsNodo(int nodo_id) {
        return containsNodoOpenList(nodo_id) || containsNodoCloseList(nodo_id);
    }

    /**
     * Añade a open_list todos los nodos adyacentes al mismo
     *
     * @param nodo_id ID del Nodo dle que se quiere obtener adyacencias
     */
    private void addNodosOpenList(int nodo_id) {
        int[] dif_coord_i = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dif_coord_j = {-1, 0, 1, -1, 1, -1, 0, 1};
        // coordenadas del nodo actual
        int coord_i;
        int coord_j;
        int id_sig_nodo;

        for (int i = 0; i < 8; i++) {
            id_sig_nodo = (nodo_id / this.mapa_tam + dif_coord_i[i]) * this.mapa_tam + (nodo_id % this.mapa_tam + dif_coord_j[i]);
            coord_i = nodo_id / this.mapa_tam + dif_coord_i[i];
            coord_j = nodo_id % this.mapa_tam + dif_coord_j[i];
            // no está en la matriz
            boolean esta_en_Matriz = coord_i >= 0 && coord_i < this.mapa_tam && coord_j >= 0 && coord_j < this.mapa_tam;
            // no es el nodo original
            boolean no_es_Origen = id_sig_nodo != start_id;
            // Para no añadir nodos repetidos
            boolean no_esta_Lista = !containsNodo(id_sig_nodo);
            // es alcanzable
            boolean alcanzable = false;
            if (esta_en_Matriz) {
                alcanzable = mapa[id_sig_nodo / this.mapa_tam][id_sig_nodo % this.mapa_tam] != -1;
            }
            // Si el nodo está en la matriz, no es el raiz, no se encuentra en open_list y es accesible
            if (no_es_Origen && no_esta_Lista && alcanzable) {
                open_list.add(new Nodo(id_sig_nodo, nodo_id, calcularCosteNodo(id_sig_nodo)));
            }
        }
    }

    
    /**
     * Selecciona el Nodo con el menor coste dentro de la lista
     *
     * @return nodo Devuelve el nodo con el coste mejor
     */
    private Nodo getMejorNodo() {
        int bajoCoste = (int) Math.pow(10, 10);
        Nodo nodo = null;
        for (Nodo nodo_actual : open_list) {
            if (nodo_actual.getCost() < bajoCoste) {
                nodo = nodo_actual;
                bajoCoste = nodo.getCost();
            }
        }
        return nodo;
    }

    /**
     * 
     * Comprueba si el nodo objetivo está en open_list
     *
     * @return true si objetivo_id está en open_list
     */
    private boolean isObjetive() {
        return containsNodoOpenList(objetivo_id);
    }

    /**
     * Obtiene un nodo concreto de Open List
     *
     * @param nodo_id ID del nodo que queremos obtener
     * @return nodo Nodo que queríamos obtener
     */
    private Nodo getNodoOpenList(int nodo_id) {
        Nodo nodo = null;
        for (Nodo nodo_actual : open_list) {
            if (nodo_actual.getId() == nodo_id) {
                nodo = nodo_actual;
            }
        }
        return nodo;
    }

    /**
     * Obtiene un nodo concreto de Close List
     *
     * @param nodo_id ID del nodo que queremos obtener
     * @return nodo Nodo que queríamos obtener
     */
    private Nodo getNodoCloseList(int nodo_id) {
        Nodo nodo = null;
        for (Nodo nodo_actual : close_list) {
            if (nodo_actual.getId() == nodo_id) {
                nodo = nodo_actual;
            }
        }
        return nodo;
    }

    /**
     * Calcula la ruta óptima entre la posición inicial start_id y la final
     * objetive_id. Guarda los ID´s de las casillas en camino_objetivo.
     *
     */
    private void caminoObjetivo() {
        boolean encontrado = false;
        boolean origen = false;

        Nodo nodo = null;
        camino_objetivo.clear();
        open_list.clear();
        close_list.clear();

        close_list.add(new Nodo(start_id, -1, -1));
        addNodosOpenList(start_id);
        if (isObjetive()) {
            encontrado = true;
        }
        while (!encontrado && !open_list.isEmpty()) {
            nodo = getMejorNodo();
            open_list.remove(nodo);
            close_list.add(nodo);
            addNodosOpenList(nodo.getId());
            encontrado = isObjetive();
        }

        if (open_list.isEmpty() && !encontrado) {
            camino_objetivo.add(-1);
        } else {
            camino_objetivo.add(objetivo_id);
            nodo = getNodoOpenList(objetivo_id);
            while (!origen) {
                nodo = getNodoCloseList(nodo.getParentId());
                camino_objetivo.add(0, nodo.getId());
                if (nodo.getId() == start_id) {
                    origen = true;
                }
            }
        }
    }

    /**
     * Devuelve el camino óptimo encontrado. 
     *
     * @return camino_objetivo
     */
    public ArrayList<Integer> getCamino() {
        this.caminoObjetivo();
        return camino_objetivo;
    }
    
}
