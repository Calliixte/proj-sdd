package Tests;
import java.util.*;

import Algorithms.Algorithms;
import Entities.GrapheTSPLIB;

/**
 * Pour chaque cas de test, on construit un GrapheTSPLIB (avec
 * coordonnées, pour que A* puisse calculer h), on exécute Dijkstra, A*
 * et Bidirectionnel, puis on vérifie :
 * - le comportement attendu (résultat null, chemin inatteignable, etc.)
 * - que les 3 algorithmes renvoient bien la même distance lorsqu'un
 * chemin existe
 * - que le chemin reconstruit via T a un coût cohérent avec pi[arrivee]
 *
 * Utiliser un grapheTSPLIB n'est pas nécéssaire pour Dijkstra et Birdirectionnel mais on l'utilise tout de même pour ces deux la afin de tester le même graphe sur les 3 algos
 * 
 * Cas couverts : chemin simple, départ == arrivée, sommet absent du
 * graphe, sommet isolé/inatteignable, plusieurs chemins de coût égal,
 * et un graphe où le chemin direct n'est pas le plus court (détour
 * obligatoire).
 */
public class AlgorithmsTests {

    public static void main(String[] args) {
        testCheminSimple();
        testDepartEgalArrivee();
        testSommetInexistant();
        testSommetIsole();
        testCheminsCoutEgal();
        testDetourPlusCourt();
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

    // Cas 1 : chemin simple A - B - C, un seul chemin possible
    private static void testCheminSimple() {
        affichageTitre("Algorithmes - Cas 1 : chemin simple A - B - C");

        GrapheTSPLIB g = new GrapheTSPLIB();
        // Coordonnées arbitraires alignées pour que h reste cohérent
        g.addCoordonnee("A", 0, 0);
        g.addCoordonnee("B", 1, 0);
        g.addCoordonnee("C", 2, 0);

        g.ajouterArc("A", "B", 5);
        g.ajouterArc("B", "C", 3);

        // Distance attendue A -> C = 8 (seul chemin possible)
        executerEtComparer(g, "A", "C", 8);
    }

    // Cas 2 : départ == arrivée -> distance 0, chemin = [depart]
    private static void testDepartEgalArrivee() {
        affichageTitre("Algorithmes - Cas 2 : départ == arrivée");

        GrapheTSPLIB g = new GrapheTSPLIB();
        g.addCoordonnee("A", 0, 0);
        g.addCoordonnee("B", 1, 0);

        g.ajouterArc("A", "B", 5);

        executerEtComparer(g, "A", "A", 0);
    }

    // Cas 3 : sommet de départ (ou arrivée) absent du graphe -> null attendu
    private static void testSommetInexistant() {
        affichageTitre("Algorithmes - Cas 3 : sommet inexistant dans le graphe");

        GrapheTSPLIB g = new GrapheTSPLIB();
        g.addCoordonnee("A", 0, 0);
        g.addCoordonnee("B", 1, 0);
        g.ajouterArc("A", "B", 5);

        String depart = "A";
        String arrivee = "Z"; // n'existe pas dans le graphe

        try {
            HashMap<String, HashMap<?, ?>> resDijkstra = Algorithms.Dijkstra(g, depart, arrivee);
            verifierNull("Dijkstra", resDijkstra);

            HashMap<String, HashMap<?, ?>> resAStar = Algorithms.AEtoile(g, depart, arrivee);
            verifierNull("A*", resAStar);

            HashMap<String, HashMap<?, ?>> resBidir = Algorithms.Bidirectionnel(g, depart, arrivee);
            verifierNull("Bidirectionnel", resBidir);

        } catch (Exception e) {
            System.out.println("ECHEC : exception inattendue : " + e);
        }
        System.out.println();
    }

    private static void verifierNull(String nomAlgo, HashMap<String, HashMap<?, ?>> res) {
        if (res == null) {
            System.out.println("OK : " + nomAlgo + " renvoie bien null pour un sommet inexistant.");
        } else {
            System.out.println("ECHEC : " + nomAlgo + " devrait renvoyer null mais a renvoyé un résultat.");
        }
    }

    // Cas 4 : sommet isolé, sans arête -> inatteignable
    private static void testSommetIsole() {
        affichageTitre("Algorithmes - Cas 4 : sommet isolé (inatteignable)");

        GrapheTSPLIB g = new GrapheTSPLIB();
        g.addCoordonnee("A", 0, 0);
        g.addCoordonnee("B", 1, 0);
        g.addCoordonnee("C", 10, 10); // isolé : aucune arête

        g.ajouterArc("A", "B", 5);
        // C n'est relié à rien

        String depart = "A";
        String arrivee = "C";

        try {
            HashMap<String, HashMap<?, ?>> resDijkstra = Algorithms.Dijkstra(g, depart, arrivee);
            verifierInatteignable("Dijkstra", resDijkstra, arrivee);

            HashMap<String, HashMap<?, ?>> resAStar = Algorithms.AEtoile(g, depart, arrivee);
            verifierInatteignable("A*", resAStar, arrivee);

            HashMap<String, HashMap<?, ?>> resBidir = Algorithms.Bidirectionnel(g, depart, arrivee);
            verifierInatteignable("Bidirectionnel", resBidir, arrivee);

        } catch (Exception e) {
            System.out.println("ECHEC : exception inattendue : " + e);
        }
        System.out.println();
    }

    private static void verifierInatteignable(String nomAlgo, HashMap<String, HashMap<?, ?>> res, String arrivee) {
        if (res == null) {
            System.out.println("ECHEC : " + nomAlgo + " a renvoyé null alors que depart/arrivee existent.");
            return;
        }
        HashMap<String, Integer> pi = (HashMap<String, Integer>) res.get("pi");
        Integer distFinale = pi.get(arrivee);
        if (distFinale == null || distFinale == Integer.MAX_VALUE) {
            System.out.println("OK : " + nomAlgo + " considère " + arrivee + " comme INATTEIGNABLE, comme attendu.");
        } else {
            System.out.println("ECHEC : " + nomAlgo + " annonce une distance de " + distFinale  + " vers " + arrivee + " alors qu'il devrait être inatteignable.");
        }
    }

    // Cas 5 : plusieurs chemins de coût égal (carré A-B-D et A-C-D, même coût)
    private static void testCheminsCoutEgal() {
        affichageTitre("Algorithmes - Cas 5 : plusieurs chemins de coût égal (carré)");

        GrapheTSPLIB g = new GrapheTSPLIB();
        // Carré : A -- B -- D et A -- C -- D, chaque arête de poids 4
        g.addCoordonnee("A", 0, 0);
        g.addCoordonnee("B", 4, 0);
        g.addCoordonnee("C", 0, 4);
        g.addCoordonnee("D", 4, 4);

        g.ajouterArc("A", "B", 4);
        g.ajouterArc("B", "D", 4);
        g.ajouterArc("A", "C", 4);
        g.ajouterArc("C", "D", 4);
        // diagonale plus chère, ne doit pas être choisie
        g.ajouterArc("A", "D", 100);

        // Les deux chemins A-B-D et A-C-D coûtent 8 ; on attend 8, peu importe lequel est choisi
        executerEtComparer(g, "A", "D", 8);
    }

    // Cas 6 : le chemin direct n'est pas le plus court (détour obligatoire)
    private static void testDetourPlusCourt() {
        affichageTitre("Algorithmes - Cas 6 : détour plus court que le chemin direct");

        GrapheTSPLIB g = new GrapheTSPLIB();
        g.addCoordonnee("A", 0, 0);
        g.addCoordonnee("B", 1, 0);
        g.addCoordonnee("C", 2, 0);

        // Chemin direct A -> C coûte 50, mais A -> B -> C coûte 2 + 2 = 4
        g.ajouterArc("A", "C", 50);
        g.ajouterArc("A", "B", 2);
        g.ajouterArc("B", "C", 2);

        // Distance attendue : 4 (via le détour A-B-C), pas 50
        executerEtComparer(g, "A", "C", 4);
    }

    /**
     *  Fonctions utilitaires aux tests, logique commune a tous les cas 
     */

    /**
     * Exécute les 3 algorithmes entre depart et arrivee, vérifie la cohérence
     * de chaque résultat (chemin/coût), affiche le détail, puis compare les
     * distances finales entre elles et avec distanceAttendue (si >= 0).
     * distanceAttendue == -1 signifie "pas de vérification de valeur précise".
     */
    private static void executerEtComparer(GrapheTSPLIB g, String depart, String arrivee, int distanceAttendue) {
        Map<String, Integer> distancesFinales = new LinkedHashMap<>();

        try {
            HashMap<String, HashMap<?, ?>> resDijkstra = Algorithms.Dijkstra(g, depart, arrivee);
            executerEtAfficher("Dijkstra", resDijkstra, g, depart, arrivee, distancesFinales);

            HashMap<String, HashMap<?, ?>> resAStar = Algorithms.AEtoile(g, depart, arrivee);
            executerEtAfficher("A*", resAStar, g, depart, arrivee, distancesFinales);

            HashMap<String, HashMap<?, ?>> resBidir = Algorithms.Bidirectionnel(g, depart, arrivee);
            executerEtAfficher("Bidirectionnel", resBidir, g, depart, arrivee, distancesFinales);

        } catch (Exception e) {
            System.out.println("ECHEC : exception levée pendant l'exécution des algorithmes : " + e);
            System.out.println();
            return;
        }

        afficherLigne();
        if (distancesFinales.size() < 3) {
            System.out.println("Comparaison impossible : un ou plusieurs algorithmes n'ont pas fourni de distance valide.");
            System.out.println();
            return;
        }

        Iterator<Map.Entry<String, Integer>> it = distancesFinales.entrySet().iterator();
        Map.Entry<String, Integer> reference = it.next();
        boolean toutesEgales = true;

        while (it.hasNext()) {
            Map.Entry<String, Integer> courant = it.next();
            if (!Objects.equals(reference.getValue(), courant.getValue())) {
                toutesEgales = false;
                System.out.println("DIFFERENCE : " + reference.getKey() + " = " + reference.getValue() + "  vs  " + courant.getKey() + " = " + courant.getValue());
            }
        }

        if (toutesEgales) {
            System.out.println("OK : les 3 algorithmes renvoient la même distance (" + reference.getValue() + ") pour " + depart + " -> " + arrivee);
        } else {
            System.out.println("ECHEC : les algorithmes ne renvoient pas tous la même distance.");
        }

        if (distanceAttendue >= 0) {
            if (toutesEgales && reference.getValue() == distanceAttendue) {
                System.out.println("OK : la distance correspond à la valeur attendue (" + distanceAttendue + ")");
            } else {
                System.out.println("ECHEC : distance attendue = " + distanceAttendue + ", distance obtenue = " + reference.getValue());
            }
        }

        System.out.println();
    }

    /**
     * Vérifie un résultat d'algorithme : pi/T non nulles, arrivée atteignable,
     * chemin reconstruit cohérent (commence par depart, termine par arrivee,
     * coût recalculé == pi[arrivee]). Enregistre la distance si tout est valide.
     */
    private static void executerEtAfficher(String nomAlgo,
                                            HashMap<String, HashMap<?, ?>> res,
                                            GrapheTSPLIB g,
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

        HashMap<String, Integer> compteur = (HashMap<String, Integer>) res.get("compteur");
        if (compteur != null) {
            System.out.println("Sommets parcourus  : " + compteur.get("valeur"));
        }

        Integer distFinale = pi.get(arrivee);
        if (distFinale == null || distFinale == Integer.MAX_VALUE) {
            System.out.println("Chemin " + depart + " -> " + arrivee + " : INATTEIGNABLE");
            return;
        }

        // Cas particulier départ == arrivée
        if (depart.equals(arrivee)) {
            if (distFinale == 0) {
                System.out.println("OK : distance " + depart + " -> " + arrivee + " = 0");
                distancesFinales.put(nomAlgo, distFinale);
            } else {
                System.out.println("ECHEC : distance " + depart + " -> " + arrivee + " devrait être 0 mais vaut " + distFinale);
            }
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

        int coutChemin = calculerCoutChemin(g, chemin);
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
    private static int calculerCoutChemin(GrapheTSPLIB g, List<String> chemin) {
        int cout = 0;
        for (int i = 0; i < chemin.size() - 1; i++) {
            Integer poids = g.getDistance(chemin.get(i), chemin.get(i + 1));
            if (poids == null) {
                System.out.println("ERREUR : aucune arête entre " + chemin.get(i) + " et " + chemin.get(i + 1) + ".");
                return Integer.MIN_VALUE;
            }
            cout += poids;
        }
        return cout;
    }
}