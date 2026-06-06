package grapheHashMap;

public class Main {
	public static void main(String[] args) {
		
		GrapheOrienteValue g = new GrapheOrienteValue("G");
		
		g.ajouterArc("A", "B", 5);
		g.ajouterArc("A", "C", 3);
		g.ajouterArc("B", "C", 2);
		g.ajouterArc("C", "D", 4);
		
		System.out.println(g);
		
		
		
	}

}
