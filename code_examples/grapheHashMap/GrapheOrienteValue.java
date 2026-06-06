package grapheHashMap;

import java.util.*;

public class GrapheOrienteValue {
	
	private Map<String, Map<String, Integer>> graphe;
	private String nom = "";
	
	public GrapheOrienteValue() {
		graphe = new HashMap<>();
	}
	
	public GrapheOrienteValue(String nom) {
		graphe = new HashMap<>();
		this.nom = nom;
	}

	public void ajouterSommet(String x) {
		// On insère une entrée x dans le dictionnaire,
		// et on associe à cette entrée un dictionnaire 
		// des successeurs vide. 
		graphe.putIfAbsent(x, new HashMap<>());
		
	}
	
	public void ajouterArc(String source, String destination,
			               int poids) {
		ajouterSommet(source);
		ajouterSommet(destination);
		graphe.get(source).put(destination, poids);
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
