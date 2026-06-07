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
        int h_depart = (int) Math.floor(h(g, depart, arrivee));
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
                        h_value = (int) Math.floor(h(g,voisin_actuel,arrivee));
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


}