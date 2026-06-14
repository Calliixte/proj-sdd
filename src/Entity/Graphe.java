package Entity;
import java.util.*;

public class Graphe {
	/**
	 * 
	 * Le sujet nous indique : "On codera un graphe non orienté par une table de hachage qui associe à chaque sommet x la liste des
	 *	couples (y, cxy )." on reprend donc ici l'implémentation du cours des graphes orientés avec les tables de hachage mais on s'assurera à chaque modification d'arc d'ajouter l'arc dans l'autre sens 
	 * 
	 */
	
	private Map<String, Map<String, Integer>> graphe;
	private String nom = "";
	
	public Graphe() {
		graphe = new HashMap<>();
	}
	
	public Graphe(String nom) {
		graphe = new HashMap<>();
		this.nom = nom;
	}

	/**
	 * 
	 * Crée une arraylist a partir des clefs d'une hashmap
	 * @param map
	 * @return l'arraylist crée
	 */
	private ArrayList<String> keysToList(Map<String, ?> map) {
		return new ArrayList<>(map.keySet());
	}
	/**
	 * 
	 * Renvoie les sommets du graphe
	 * @return l'arraylist des sommets du graphe
	 */
	public ArrayList<String> getSommets() {
		return keysToList(this.graphe);
	}

	/**
	 * Renvoie les sommets voisins d'un sommet identifié par son nom
	 * @param sommet sommet dont on veut les voisins
	 * @return l'arraylist des voisins du sommet
	 */
	public ArrayList<String> getVoisins(String sommet) {
		return keysToList(this.graphe.get(sommet));
	}

	/**
	 * Renvoie la distance entre le sommet 1 et le sommet 2, null si la distance n'existe pas
	 * @param s1 sommet 1 
	 * @param s2 sommet 2
	 * @return la distance entre les deux sommets
	 */
	public Integer getDistance(String s1, String s2) {
		if (graphe.containsKey(s1) && graphe.get(s1).containsKey(s2)) {
			return graphe.get(s1).get(s2);
		}
		return null;
	}
	public void ajouterSommet(String x) {
		// On insère une entrée x dans le dictionnaire,
		// et on associe à cette entrée un dictionnaire 
		// des successeurs vide. 
		graphe.putIfAbsent(x, new HashMap<>());
		
	}

	public void ajouterArc(String source, String destination, int poids) {
		ajouterSommet(source);
		ajouterSommet(destination);
		graphe.get(source).put(destination, poids);
		graphe.get(destination).put(source, poids);
	}
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append("Graphe "+this.nom+" : \n");
		for (String x : graphe.keySet()) {
			s.append("    Successeurs de "+x+" : ");
			// On récupère les couples (y, poids), où y
			// est un successeur de x, donc une entrée du
			// dictionnaire des successeurs de x
			for (Map.Entry<String, Integer>couple : graphe.get(x).entrySet()) {
				s.append(couple.getKey());
				s.append(" (poids = "+couple.getValue()+"), ");
			}
			s.append("\n");
		}
		
		return s.toString();
	}
	
}
