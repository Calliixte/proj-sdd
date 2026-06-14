package Algorithms;

import Entities.GrapheTSPLIB;

public class Utilitaires {
    /**
     * 
     * Calcule la valeur de h pour un graphe TSPLIB donné
     * @param g graphe choisi
     * @param x sommet
     * @param destination 2eme sommet, dans le cadre de l'algo suivant, la destination
     * @return valeur 
     */
    public static double h(GrapheTSPLIB g,String x, String destination) throws Exception{
        Double res = g.getDistanceSommets(x, destination);
        if (res == Double.MAX_VALUE){
            throw new Exception("Tentative de calcul de distance sur des sommets sans coordonnées ! Sommets concernés : " + x + ", "+destination+".");
        }
        return res;
    }

}
