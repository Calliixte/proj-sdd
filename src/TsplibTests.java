import java.util.*;

/**
 * Tests demandés dans le sujet, sur les instances de la TSPLIB att48 (pour p = 15% et p = 12%) et a280 (p = 6%).
 * Ne possédant pas de fichier solution préalable, on part du principe qu'il n'existe qu'un plus court chemin entre les deux sommets donnés sur les instances. Pour un graphe donné, la vérification du résultat se fait en comparant les résultats d'un algorithme aux deux autres. 
 * 
 * Les résultats obtenus seront examinés dans le rapport fourni avec le projet.
 * */
public class TsplibTests {

    public static void main(String[] args) {
        // Cas de test : (fichier, pourcentage, depart, arrivee)
        testerCas("tsplib_instances/instances/a280.tsp", 6, "8", "92");
        testerCas("tsplib_instances/instances/att48.tsp", 15, "43", "45");
        testerCas("tsplib_instances/instances/att48.tsp", 12, "43", "45");
    }

    private static void affichageTitre(String message){
        afficherGrosseLigne();
        System.out.println(message);
        afficherGrosseLigne();
    }

    private static void afficherGrosseLigne(){
        System.out.println("=================================================================");
    }

    private static void afficherLigne(){
        System.out.println("-----------------------------------------------------------------");
    }

    /**
     * Charge le graphe, lance les 3 algorithmes, affiche les résultats
     * détaillés puis la comparaison finale.
     */
    private static void testerCas(String cheminFichier, int pourcentage, String depart, String arrivee) {
        affichageTitre("Instance : " + cheminFichier + " | p = " + pourcentage + "% | " + depart + " -> " + arrivee);

        GrapheTSPLIB graphe = GrapheTSPLIB.lireFichier(cheminFichier, pourcentage);
        if (graphe == null) {
            System.out.println("ECHEC : le graphe n'a pas pu être chargé.");
            return;
        }

        Map<String, Integer> distancesFinales = new LinkedHashMap<>();

        try {
            HashMap<String, HashMap<?, ?>> resDijkstra = Algorithms.Dijkstra(graphe, depart, arrivee);
            executerEtAfficher("Dijkstra", resDijkstra, graphe, depart, arrivee, distancesFinales);

            HashMap<String, HashMap<?, ?>> resAStar = Algorithms.AEtoile(graphe, depart, arrivee);
            executerEtAfficher("A*", resAStar, graphe, depart, arrivee, distancesFinales);

            HashMap<String, HashMap<?, ?>> resBidir = Algorithms.Bidirectionnel(graphe, depart, arrivee);
            executerEtAfficher("Bidirectionnel", resBidir, graphe, depart, arrivee, distancesFinales);

        } catch (Exception e) {
            System.out.println("ECHEC : exception levée pendant l'exécution des algorithmes : " + e);
            return;
        }

        // Comparaison finale des distances trouvées par chaque algo
        afficherLigne();
        if (distancesFinales.size() < 3) {
            System.out.println("Comparaison impossible : un ou plusieurs algorithmes n'ont pas fourni de distance valide.");
            return;
        }

        Iterator<Map.Entry<String, Integer>> it = distancesFinales.entrySet().iterator();
        Map.Entry<String, Integer> reference = it.next();
        boolean toutesEgales = true;

        while (it.hasNext()) {
            Map.Entry<String, Integer> courant = it.next();
            if (!Objects.equals(reference.getValue(), courant.getValue())) {
                toutesEgales = false;
                System.out.println("DIFFERENCE : " + reference.getKey() + " = " + reference.getValue()  + "  vs  " + courant.getKey() + " = " + courant.getValue());
            }
        }

        if (toutesEgales) {
            System.out.println("OK : les 3 algorithmes renvoient la même distance (" + reference.getValue() + ") pour " + depart + " -> " + arrivee);
        } else {
            System.out.println("ECHEC : les algorithmes ne renvoient pas tous la même distance.");
        }
        System.out.println();
    }

    /**
     * Exécute la vérification d'un résultat d'algorithme :
     *  vérifie que pi et T existent
     *  vérifie que l'arrivée est atteignable
     *  reconstruit le chemin via T et vérifie sa cohérence
     *  affiche le chemin et le coût
     * Enregistre la distance finale dans distancesFinales si tout est valide.
     */
    private static void executerEtAfficher(String nomAlgo,
                                            HashMap<String, HashMap<?, ?>> res,
                                            GrapheTSPLIB graphe,
                                            String depart, String arrivee,
                                            Map<String, Integer> distancesFinales) {

        System.out.println("--- " + nomAlgo + " ---");

        if (res == null) {
            System.out.println("Aucun résultat (null).");
            return;
        }

        HashMap<String, Integer> pi = (HashMap<String, Integer>) res.get("pi");
        HashMap<String, String> T = (HashMap<String, String>) res.get("T");

        if (pi == null || T == null) {
            System.out.println("Résultat invalide : pi ou T est null.");
            return;
        }

        Integer distFinale = pi.get(arrivee);
        if (distFinale == null || distFinale == Integer.MAX_VALUE) {
            System.out.println("Chemin " + depart + " -> " + arrivee + " : INATTEIGNABLE");
            return;
        }

        // Reconstruction du chemin via T
        LinkedList<String> chemin = new LinkedList<>();
        String courant = arrivee;
        int securite = 0;
        while (courant != null && securite < 1000) {
            chemin.addFirst(courant);
            courant = T.get(courant);
            securite++;
        }

        //valeur arbitraire de 1000, nos cas sont limités donc on laisse ainsi, on pourrait calculer un pourcentage du nombre de sommets dans une potentielle évolution
        if (securite >= 1000) {
            System.out.println("ERREUR : boucle infinie détectée dans la reconstruction du chemin.");
            return;
        }

        if (!chemin.getFirst().equals(depart)) {
            System.out.println("ERREUR : le chemin reconstruit ne commence pas par " + depart + " (commence par " + chemin.getFirst() + ").");
            return;
        }

        if (!chemin.getLast().equals(arrivee)) {
            System.out.println("ERREUR : le chemin reconstruit ne termine pas par " + arrivee + " (termine par " + chemin.getLast() + ").");
            return;
        }

        // Vérification du coût recalculé
        int coutChemin = calculerCoutChemin(graphe, chemin);
        System.out.println("Chemin optimal     : " + String.join(" -> ", chemin) + "  (coût annoncé : " + distFinale + ", coût recalculé : " + coutChemin + ")");

        if (coutChemin != distFinale) {
            System.out.println("ATTENTION : le coût recalculé ne correspond pas à pi[" + arrivee + "].");
            return;
        }

        distancesFinales.put(nomAlgo, distFinale);
    }

    /**
     * Calcule le coût total d'un chemin en sommant les poids des arêtes
     * via Graphe.getDistance(s1, s2).
     */
    private static int calculerCoutChemin(GrapheTSPLIB graphe, List<String> chemin) {
        int cout = 0;
        for (int i = 0; i < chemin.size() - 1; i++) {
            Integer poids = graphe.getDistance(chemin.get(i), chemin.get(i + 1));
            if (poids == null) {
                System.out.println("ERREUR : aucune arête entre " + chemin.get(i) + " et " + chemin.get(i + 1) + ".");
                return Integer.MIN_VALUE;
            }
            cout += poids;
        }
        return cout;
    }
}