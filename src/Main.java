import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
    // Créer les deux graphes
    GrapheTSPLIB att48 = GrapheTSPLIB.lireFichier("tsplib_instances/instances/att48.tsp",15);
    GrapheTSPLIB a280  = GrapheTSPLIB.lireFichier("tsplib_instances/instances/a280.tsp", 6);
    try {
        HashMap<String, HashMap<?,?>> res1 = Algorithms.Dijkstra(att48, "43", "45");
        System.out.println("Dijkstra");
        afficherResultat(res1, "43", "45");        
        HashMap<String, HashMap<?,?>> res2 = Algorithms.AEtoile(att48, "43", "45");
        System.out.println("Astar");
        afficherResultat(res2, "43", "45");        
        HashMap<String, HashMap<?,?>> res3 = Algorithms.Bidirectionnel(att48, "43", "45");
        System.out.println("Bidirectionnel");
        afficherResultat(res3, "43", "45");        
    } catch (Exception e) {
        System.out.println(e);
    }


    // System.out.println(att48);
    // System.out.println(parseSolution("tsplib_instances/solutions/att48.opt.tour"));

    // System.out.println(a280);
    // System.out.println(parseSolution("tsplib_instances/solutions/a280.opt.tour","8","92"));
}

private static String parseSolution(String cheminSolution, String depart, String arrivee) {
    File f = new File(cheminSolution);
    List<String> tour = new ArrayList<>();
    boolean inTour = false;

    try (Scanner sc = new Scanner(f)) {
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.equals("TOUR_SECTION")) { inTour = true; continue; }
            if (line.equals("-1") || line.equals("EOF")) break;
            if (inTour) tour.add(line);
        }
    } catch (FileNotFoundException e) {
        return "Solution introuvable : " + cheminSolution;
    }

    int idxDepart = tour.indexOf(depart);
    if (idxDepart == -1) return "Sommet " + depart + " introuvable dans la solution.";
    int idxArrivee = tour.indexOf(arrivee);
    if (idxArrivee == -1) return "Sommet " + arrivee + " introuvable dans la solution.";

    // Rotation pour commencer au départ, puis on lit jusqu'à l'arrivée
    List<String> tourOriente = new ArrayList<>();
    int i = idxDepart;
    while (true) {
        tourOriente.add(tour.get(i));
        if (i == idxArrivee) break;
        i = (i + 1) % tour.size();
    }

    return "Solution optimale de " + depart + " à " + arrivee + " : "
            + String.join(" -> ", tourOriente);
}
    /**
     * Affiche pi (distances), T (prédécesseurs)
     * et reconstitue le chemin de départ à arrivée.
     */
    @SuppressWarnings("unchecked")
    static void afficherResultat(HashMap<String, HashMap<?,?>> res, String depart, String arrivee) {
        if (res == null) {
            System.out.println("Aucun résultat (null).");
            return;
        }

        HashMap<String, Integer> pi = (HashMap<String, Integer>) res.get("pi");
        HashMap<String, String>  T  = (HashMap<String, String>)  res.get("T");

        // System.out.println("Distances (pi)     : " + pi);
        // System.out.println("Prédécesseurs (T)  : " + T);

        // Reconstitution du chemin via T
        Integer distFinale = pi.get(arrivee);
        if (distFinale == null || distFinale == Integer.MAX_VALUE) {
            System.out.println("Chemin " + depart + " -> " + arrivee + " : INATTEIGNABLE");
            return;
        }

        LinkedList<String> chemin = new LinkedList<>();
        String courant = arrivee;
        // garde-fou contre les cycles ou T mal formé
        int securite = 0;
        while (courant != null && securite < 1000) {
            chemin.addFirst(courant);
            courant = T.get(courant);
            securite++;
        }

        System.out.println("Chemin optimal     : " + String.join(" -> ", chemin)
                + "  (coût total : " + distFinale + ")");
    }
}
