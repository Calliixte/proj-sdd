package Entity;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
public class GrapheTSPLIB extends Graphe{
    /**
     * Map qui associe a chaque sommet des coordonnées, pour les grapes utilisés en exemple on prendra deux coordonnées dans la partie double
     * Le nom des sommets doit être unique
     */
    private Map<String,Point2D> coordonnees;

    /**
     * Crée un grapheTSPLIB vide
     */
    public GrapheTSPLIB(){
        super();
        coordonnees = new HashMap<>();
    }

    /**
     * Crée un graphe TSPLIB a partir d'une instance donnée (chemin de fichier) et d'un pourcentage p
     * @param Instance instance des sommets à utiliser pour le graphe
     * @param p seuil donné de construction des arêtes. Si la distance entre 2 points est inférieure à p% de la plus grande distance entre 2 points du graphe, l'arete existe
     */
    public static GrapheTSPLIB lireFichier(String Instance, int p){
        
        
        /**
         * Créer le graphe initial
         */
        GrapheTSPLIB g = new GrapheTSPLIB();
        
        
        
        /**
         * Parser le fichier
         * code de recuperation provenant de https://www.w3schools.com/java/java_files_read.asp
         */

        File myObj = new File(Instance.trim());
        boolean start_splitting  = false;
        // try-with-resources: Scanner will be closed automatically
        try (Scanner myReader = new Scanner(myObj)) {

            while (myReader.hasNextLine()) {

                String data = myReader.nextLine(); ///data contient la ligne en string
                if (data.trim().equals("NODE_COORD_SECTION"))    
                {
                    start_splitting = true;
                    continue; // eviter de parser node_coord_section
                }
                if (data.trim().equals("EOF"))
                {
                    start_splitting = false; //par bonne mesure
                    break; //eof déclaré, on sort de la boucle
                    //eof n'est pas systématique selon la doc tsplib, il l'est dans les instances données donc tant mieux
                }
                if (start_splitting)
                {
                    String[] parts = data.trim().split("\\s+");
                    if (parts.length >= 3) {
                        //creer un sommet avec le premier
                        String s = parts[0]; //sommet, pas a mettre en int car les sommets sont des strings dans notre implementation
                        //associer le point 2d au sommet ensuite
                        double x = Double.parseDouble(parts[1]);
                        double y = Double.parseDouble(parts[2]);
                        g.addCoordonnee(s, x, y);
                    }
                }    
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        /**
         * Recherche de la plus grande distance dans le graphe obtenu
         */
        ArrayList<String> sommets = new ArrayList<>(g.coordonnees.keySet());
        int n = sommets.size();

        double maxDist = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double d = g.getDistanceSommets(sommets.get(i), sommets.get(j));
                if (d > maxDist) maxDist = d;
            }
        }

        /**
         * Créer les aretes si leur distance est inférieure à p% de la plus grande distance
         */

        double seuil = maxDist * (p / 100.0);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double d = g.getDistanceSommets(sommets.get(i), sommets.get(j));
                if (d <= seuil) {
                    g.ajouterArc(sommets.get(i), sommets.get(j), (int) Math.floor(d)); //le poids d'un arc sera la distance entre les deux sommets arrondie à l'inférieur, comme fait dans A*
                }
            }
        }

        return g;
    }

    
    /**
     * Ajoute des coordonnées à un graphe tsplib, crée le sommet s'il n'existe pas
     * @param s sommet a ajouter 
     * @param x coordonnée x du sommet 
     * @param y coordonnée y du sommet 
     */
    public void addCoordonnee(String s,double x, double y){
        this.coordonnees.put(s, new Point2D(x,y));
        //au cas ou l'utilisateurice essaye d'ajouter des coordonnées a un sommet inexistant
        super.ajouterSommet(s);
    }

    /**
     * Ajoute des coordonnées à un graphe tsplib, crée le sommet s'il n'existe pas
     * @param s sommet a ajouter 
     * @param p point2d (x,y) qui représente l'emplacement de la coordonnée
    */
    public void addCoordonnee(String s,Point2D p){
        this.coordonnees.put(s,p);
        //au cas ou l'utilisateurice essaye d'ajouter des coordonnées a un sommet inexistant
        super.ajouterSommet(s);
    }


    /**
     * getter de x
     * @param s sommet dont on veut x
     * @return x, si erreur -> double.max value
     */
    public double getX(String s) {
        if (!this.coordonnees.containsKey(s)) return Double.MAX_VALUE;
        return this.coordonnees.get(s).getX();
    }

    /**
     * getter de y
     * @param s sommet dont on veut y
     * @return y, si erreur -> double.max value
     */
    public double getY(String s) {
        if (!this.coordonnees.containsKey(s)) return Double.MAX_VALUE;
        return this.coordonnees.get(s).getY();
    }

    /**
     * Renvoie la distance euclienne entre deux sommets, utilisée pour A*  
     * @param s1 sommet 1
     * @param s2 sommet 2
     * @return Si execution normale : distance (double) Si erreur : maxvalue du double 
     */
    public double getDistanceSommets(String s1, String s2) {
    if (!this.coordonnees.containsKey(s1) || !this.coordonnees.containsKey(s2)) 
        return Double.MAX_VALUE;
    return this.coordonnees.get(s1).distanceEuclidienne(this.coordonnees.get(s2));
    }


}