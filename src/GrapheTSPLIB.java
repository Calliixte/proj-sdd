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