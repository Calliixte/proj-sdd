import java.util.*;

public class Algorithms {
    /**
     * 
     * Implementation de l'algorithme de dijkstra
     * On suppose les poids positif sur les arêtes
     * Déroulement de l'algorithme (extrait de ma compréhension du sujet)
     * 
     * associer a tout les sommets une distance infinie via une hashmap (pi)
     * associer a tous les sommets un predecesseur via une autre hashmap (T)
     * mettre la valeur du sommet de départ dans pi à 0
     * S est le type tas qu'on a implémenté en partie 1 du projet, pour l'instant il contient juste r 0 
     * tant que S non vide
     *  en prend la distance minimum dans pi (premiere iteration ça sera r qui est le seul sommet, et à 0)
     *  on retire le sommet selectionné de S
     *  on marque s comme déja traité (établir une liste des sommets traités)
     *  parcourir les voisins de s et faire : 
     *      pi[voisin actuel] = min(pi[voisin actuel],pi[s] + poids )
     *      T[u] = s
     *      Ajouter u au tas S
     * fin while    
     * 
     * @param g
     * @param depart
     * @param arrivee
     * @return une hashmap contenant deux hashmap : pi et T
     */
    public static HashMap<String,HashMap<?,?>> Dijkstra(Graphe g,String depart,String arrivee){

        //liste des sommets marqués
        //hashset pour avoir une complexité meilleure qu'avec une arraylist
        HashSet<String> sommets_traites = new HashSet<>();
        ArrayList<String> sommets_graphe = g.getSommets();

        //si le sommet de depart ou le sommet d'arrivée n'est pas dans le graphe, on sort du programme
        if(!sommets_graphe.contains(depart) || !sommets_graphe.contains(arrivee)){
            return null;
        }

        HashMap<String,Integer> pi = new HashMap<>();
        HashMap<String,String> T = new HashMap<>();

        for (String sommet : sommets_graphe) {
            //on utilise maxvalue pour representer l'infini et null pour un -1 car ici on fonctionnera toujours en string
            pi.put(sommet,Integer.MAX_VALUE);
            T.put(sommet,null); 
        }

        pi.replace(depart, 0);

        Tas S = new Tas();
    
        S.ajouter(depart,0);
        
        while(!S.estVide()){
            Map.Entry<String, Integer> result = S.retirer();
            String sommet = result.getKey();
            Integer distance = result.getValue();

            //si le sommet est marqué on va au suivant
            if(sommets_traites.contains(sommet)){
                continue;
            }

            sommets_traites.add(sommet);

            ArrayList<String> voisins = g.getVoisins(sommet);
            for(String voisin_actuel : voisins){
                //si le voisin est marqué on va au suivant
                if(sommets_traites.contains(voisin_actuel)){
                    continue;
                }
                
                Integer distance_s_v = g.getDistance(sommet, voisin_actuel); //integer au lieu de int pour pouvoir faire la comparaison a null

                if(distance_s_v == null ){
                    System.err.println("[Algorithms.java][Dijkstra] Probleme lors de la verification de distance avec les voisins");
                    continue; //si il y a un probleme on boucle sur le sommet suivant
                }

                
                int nouvelle_distance = pi.get(sommet) + distance_s_v;

                if (nouvelle_distance < pi.get(voisin_actuel)) 
                {
                    pi.put(voisin_actuel, nouvelle_distance);
                    T.put(voisin_actuel, sommet);
                    S.ajouter(voisin_actuel, nouvelle_distance);
                }            
        }

        }
    
        HashMap<String, HashMap<?,?>> return_value = new HashMap<>();
        return_value.put("pi",pi);
        return_value.put("T",T);
        return return_value;
    }
}
