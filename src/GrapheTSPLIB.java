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

    public void addCoordonnee(String s,double x, double y){
        this.coordonnees.put(s, new Point2D(x,y));
        //au cas ou l'utilisateurice essaye d'ajouter des coordonnées a un sommet inexistant
        super.ajouterSommet(s);
    }

    public double getX(String s){
        return this.coordonnees.get(s).getX();
    }
    public double getY(String s){
        return this.coordonnees.get(s).getY();
    }

    /**
     * Renvoie la distance euclienne entre deux sommets, utilisée pour A*  
     * @param s1 sommet 1
     * @param s2 sommet 2
     * @return distance (double)
     */
    public double getDistanceSommets(String s1,String s2){
        return this.coordonnees.get(s1).distanceEuclidienne(this.coordonnees.get(s2));
    }


}