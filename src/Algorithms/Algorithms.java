package Algorithms;
import java.util.*;

import Entity.Graphe;
import Entity.GrapheTSPLIB;
import Entity.Tas;

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

    /**
     * Implementation java de l'algorithme A*
     * La fonction nommée h(x) sera implémentée de maniere fixe dans ce fichier, dans le cadre d'une application plus complete, la signature de la fonction serait modifiée pour passer la fonction h en parametre
     * 
     * mettre pi de tout sommet a inf
     * mettre T de tout sommet a null
     * nouvelle list  par rapport a dijkstra -> f pour chaque sommet on va calculer sa valeur dans la fonction d'evalution, ici la distance euclienne par rapport a la destination on doit trouver le min dans cette liste donc on la modélisera par un tas qui remplacera le S de dijkstra
     * 
     * pi depart a 0
     * on bind un string s a r
     * tant que s ne vaut pas arrivee faire
     *      marquer s
     *      pour tous les voisins de s
     *          idem dijkstra
     *          f[voisin_actuel] = pi[voisin_actuel] + h(voisin_actuel)
     *      s = minimum de f -> f doit être un tas
     * 
     * @param g grapheTSPLIB contenant pour chaque sommet des coordonnées correspondantes
     * @param depart sommet de depart
     * @param arrivee sommet d'arrivée
     * @return une hashmap contenant deux hashmap : pi et T
     */
    public static HashMap<String,HashMap<?,?>> AEtoile(GrapheTSPLIB g,String depart,String arrivee) throws Exception{

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
        Tas f = new Tas();

        for (String sommet : sommets_graphe) {
            //on utilise maxvalue pour representer l'infini et null pour un -1 car ici on fonctionnera toujours en string
            pi.put(sommet,Integer.MAX_VALUE);
            T.put(sommet,null);            
        }
        
        //le tas utilise des entiers ici le resultat sera un double, on supposera que en cas d'arrondi égal le calcul n'aurait pas été impacté
        //on peut envisager une implantation de tas paramétrable pour le type de donnée passé
        pi.put(depart, 0);
        int h_depart = (int) Math.floor(Utilitaires.h(g, depart, arrivee));
        f.ajouter(depart, h_depart);
        
        String s = depart; //sommet

        while (!s.equals(arrivee)){

            sommets_traites.add(s);
            //si le sommet est marqué on va au suivant


            ArrayList<String> voisins = g.getVoisins(s);
            for(String voisin_actuel : voisins){
                //si le voisin est marqué on va au suivant
                if(sommets_traites.contains(voisin_actuel)){
                    continue;
                }
                if (pi.get(s) == Integer.MAX_VALUE) break; //si le sommet le plus proche est +inf donc pas de sommet voisin on ne traite pas les voisins de ce sommet et on continue l'algo 
                
                Integer distance_s_v = g.getDistance(s, voisin_actuel); //integer au lieu de int pour pouvoir faire la comparaison a null

                if(distance_s_v == null ){
                    System.err.println("[Algorithms.java][Dijkstra] Probleme lors de la verification de distance avec les voisins");
                    continue; //si il y a un probleme on boucle sur le sommet suivant
                }

                
                int nouvelle_distance = pi.get(s) + distance_s_v;

                if (nouvelle_distance < pi.get(voisin_actuel)) 
                {
                    pi.put(voisin_actuel, nouvelle_distance);
                    T.put(voisin_actuel, s);
                    int h_value;
                    try{
                        h_value = (int) Math.floor(Utilitaires.h(g,voisin_actuel,arrivee));
                    }catch(Exception e){
                        throw new Exception("Erreur depuis l'algorithme A* :" + e.getMessage());
                    }
                    if(f.sommetExists(voisin_actuel))
                    {
                        f.modifierPriorite(voisin_actuel, nouvelle_distance+h_value); //f(u) = pi(u) + h(u)
                    }
                    else
                    {
                        f.ajouter(voisin_actuel, nouvelle_distance+h_value); //f(u) = pi(u) + h(u)
                    }
                }            
        }   
            if(f.estVide()) break; //plus de nouveau sommet a explorer, on sort de l'algorithme
            Map.Entry<String, Integer> result = f.retirer();
            String res = result.getKey(); // on prend s non marqué tq f(s) minimum
            while(sommets_traites.contains(res)){
                if(f.estVide()) break; //plus de nouveau sommet a explorer, on sort de l'algorithme
                // si le sommet récuperé est marqué, on en récupere un autre
                result = f.retirer();
                res = result.getKey(); // on prend s non marqué tq f(s) minimum
            }
            s = res;

      
        }

        HashMap<String, HashMap<?,?>> return_value = new HashMap<>();
        return_value.put("pi",pi);
        return_value.put("T",T);
        return return_value;
    }

/**
 * Implementation de l'algorithme bidirectionnel en java qui "en gros" lance deux dijkstra, un au départ normal, et un depuis l'arrivée, sur le graphe inverse.
 * Nous travaillons avec des graphes non orientés donc le graphe inverse est le meme que 
 * 
 *  Au lieu d'avoir un vecteur pi qui fait les distances, on en crée 2 (pi avant piav, et pi arriere piar), puisque on a 2 recherches simultanées
 * quand on fait une recherche avant, on arrive sur une arete quelconque, on connait nécéssairement la distance début <-> extremité 1 de l'arete 
 * et la distance extermité 2 de l'arete <-> fin.
 * 
 * Si la somme de ces deux distances + le poids de l'arrete extremité 1 <-> extermité 2  est inférieure à la distance actuelle pour piav de p alors on le met à jour
 * 
 * ce calcul fonctionne meme a la premiere itération puisque de base piav(v) vaut maxint
 * 
 * le meme raisonnement peut être établi en prenant le point de vue d'une recherche arrière, ce qui rend ce algorithme réellement efficace. Sans cela il nécéssiterait une recherche arrière complete avant la recherche avant, perdant son efficacité. 
 *
 * @param g Graphe dans lequel on effectuera la recherche
 * @param depart sommet de départ
 * @param arrivee sommet d'arrivée
 * @return une hashmap avec 2 hashmap : pi (distances) et T (predecesseurs)
 * @throws Exception
 */
public static HashMap<String,HashMap<?,?>> Bidirectionnel(Graphe g, String depart, String arrivee) throws Exception {

    ArrayList<String> sommets_graphe = g.getSommets();

    // si le sommet de depart ou le sommet d'arrivée n'est pas dans le graphe, on sort du programme
    if (!sommets_graphe.contains(depart) || !sommets_graphe.contains(arrivee)) {
        return null;
    }

    // sommets traités dans chaque direction
    HashSet<String> traites_av = new HashSet<>();
    HashSet<String> traites_ar = new HashSet<>();

    // vecteurs de distances avant et arrière
    HashMap<String, Integer> piav = new HashMap<>();
    HashMap<String, Integer> piar = new HashMap<>();

    // vecteurs de prédécesseurs avant et arrière
    HashMap<String, String> Tav = new HashMap<>();
    HashMap<String, String> Tar = new HashMap<>();

    for (String sommet : sommets_graphe) {
        piav.put(sommet, Integer.MAX_VALUE);
        piar.put(sommet, Integer.MAX_VALUE);
        Tav.put(sommet, null);
        Tar.put(sommet, null);
    }

    piav.put(depart, 0);
    piar.put(arrivee, 0);

    // tas pour la recherche avant et arrière
    Tas Sav = new Tas();
    Tas Sar = new Tas();

    Sav.ajouter(depart, 0);
    Sar.ajouter(arrivee, 0);

    // meilleure distance connue entre depart et arrivee
    int meilleure_distance = Integer.MAX_VALUE;

    // sommet sur lequel les deux fronts se sont rejoints pour obtenir meilleure_distance
    // on en a besoin pour reconstruire le chemin complet lors de la fusion de Tav et Tar
    String sommet_jonction = null;

    while (!Sav.estVide() && !Sar.estVide()) {

        // --- recherche avant ---
        Map.Entry<String, Integer> result_av = Sav.retirer();
        String s_av = result_av.getKey();

        // si ce sommet a déjà été traité dans la recherche avant, on passe
        if (traites_av.contains(s_av)) {
            // on continue quand même la recherche arrière ci-dessous
        } else {
            // condition d'arrêt : s_av a déjà été examiné dans la recherche arrière
            if (traites_ar.contains(s_av)) {
                break;
            }

            traites_av.add(s_av);

            ArrayList<String> voisins_av = g.getVoisins(s_av);
            for (String voisin : voisins_av) {
                if (traites_av.contains(voisin)) {
                    continue;
                }

                Integer distance_s_v = g.getDistance(s_av, voisin);

                if (distance_s_v == null) {
                    System.err.println("[Algorithms.java][Bidirectionnel] Probleme lors de la verification de distance avec les voisins (avant)");
                    continue;
                }

                // mise à jour de piav via relaxation classique
                if (piav.get(s_av) != Integer.MAX_VALUE) {
                    int nouvelle_distance = piav.get(s_av) + distance_s_v;

                    if (nouvelle_distance < piav.get(voisin)) {
                        piav.put(voisin, nouvelle_distance);
                        Tav.put(voisin, s_av);
                        Sav.ajouter(voisin, nouvelle_distance);
                    }

                    // si le voisin a déjà été visité en arrière, on peut potentiellement mettre à jour la meilleure distance
                    if (piar.get(voisin) != Integer.MAX_VALUE) {
                        int distance_totale = nouvelle_distance + piar.get(voisin);
                        if (distance_totale < meilleure_distance) {
                            meilleure_distance = distance_totale;
                            piav.put(arrivee, meilleure_distance);
                            sommet_jonction = voisin;
                        }
                    }
                }
            }
        }

        // recherche arrière 
        Map.Entry<String, Integer> result_ar = Sar.retirer();
        String s_ar = result_ar.getKey();

        if (traites_ar.contains(s_ar)) {
            continue;
        }

        // si s_ar a déjà été examiné dans la recherche avant -> pas besoin de le retraiter on sort
        if (traites_av.contains(s_ar)) {
            break;
        }

        traites_ar.add(s_ar);

        ArrayList<String> voisins_ar = g.getVoisins(s_ar);
        for (String voisin : voisins_ar) {
            if (traites_ar.contains(voisin)) {
                continue;
            }

            Integer distance_s_v = g.getDistance(s_ar, voisin);

            if (distance_s_v == null) {
                System.err.println("[Algorithms.java][Bidirectionnel] Probleme lors de la verification de distance avec les voisins (arriere)");
                continue;
            }

            // mise à jour de piar
            if (piar.get(s_ar) != Integer.MAX_VALUE) {
                int nouvelle_distance = piar.get(s_ar) + distance_s_v;

                if (nouvelle_distance < piar.get(voisin)) {
                    piar.put(voisin, nouvelle_distance);
                    Tar.put(voisin, s_ar);
                    Sar.ajouter(voisin, nouvelle_distance);
                }

                // si le voisin a déjà été visité en avant, on peut potentiellement mettre à jour la meilleure distance
                if (piav.get(voisin) != Integer.MAX_VALUE) {
                    int distance_totale = piav.get(voisin) + nouvelle_distance;
                    if (distance_totale < meilleure_distance) {
                        meilleure_distance = distance_totale;
                        piav.put(arrivee, meilleure_distance);
                        sommet_jonction = voisin;
                    }
                }
            }
        }
    }

    // reconstruction du vecteur T complet en fusionnant Tav et Tar
    // Tav nous donne directement le chemin départ -> sommet_jonction
    // Tar donne les prédécesseurs depuis l'arrivée, donc Tar[x] = y signifie y -> x dans le sens arrière,
    // soit x -> y dans le sens avant : on repart de sommet_jonction et on remonte Tar jusqu'à arrivée
    // en inversant chaque lien pour les intégrer correctement dans T
    HashMap<String, String> T = new HashMap<>(Tav);

    if (sommet_jonction != null) {
        String courant = sommet_jonction;
        while (courant != null && !courant.equals(arrivee)) {
            String suivant = Tar.get(courant);
            if (suivant == null) break;
            T.put(suivant, courant);
            courant = suivant;
        }
    }

    HashMap<String, HashMap<?, ?>> return_value = new HashMap<>();
    return_value.put("pi", piav);
    return_value.put("T", T);
    return return_value;
}

}