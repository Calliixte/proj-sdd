import java.util.ArrayList;

public class Main{
    public static void main(String args[]){
        Tas t = new Tas();
        t.valeurs.add(17);
        t.valeurs.add(41);
        t.valeurs.add(30);
        // t.valeurs.add(50);
        // t.valeurs.add(45);
        t.sommets.add("17");
        t.sommets.add("41");
        t.sommets.add("30");
        // t.sommets.add("50");
        // t.sommets.add("45");
        System.out.println(t);

        Tas t2 = new Tas();
        t2.ajouter("30",30);
        t2.ajouter("17", 17);
        t2.ajouter("41", 41);
        System.out.println(t2);

        Graphe g = new Graphe("Mon graphe");

        g.ajouterSommet("A");
        g.ajouterArc("A", "B", 4);
        g.ajouterArc("C", "D", 9);
        g.ajouterArc("B", "C", 3);
        System.out.println(g);
    }
}