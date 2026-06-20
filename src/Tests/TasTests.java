package Tests;
import java.util.*;

import Entities.Tas;

/**
 * Tests pour la classe Tas (tas binaire min).
 *
 * On vérifie :
 * - estVide sur un tas vide
 * - ajout simple et récupération du minimum
 * - ajout dans le désordre -> le minimum doit toujours être à la racine
 * - retirer renvoie les éléments dans l'ordre croissant (tri par tas)
 * - sommetExists
 * - modifierPriorite (diminution et augmentation de valeur)
 * - cohérence interne : sommets/valeurs de même taille, et propriété de tas
 * respectée (chaque parent <= ses enfants) après chaque opération
 * - cas limites : tas avec un seul élément, valeurs dupliquées
 */
public class TasTests {

    public static void main(String[] args) {
        testTasVide();
        testAjoutSimpleEtMinimum();
        testOrdreInsertionAleatoire();
        testRetirerOrdreTri();
        testSommetExists();
        testModifierPrioriteDiminution();
        testModifierPrioriteAugmentation();
        testUnSeulElement();
        testValeursDupliquees();
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

    // Cas 1 : tas vide
    private static void testTasVide() {
        affichageTitre("Tas - Cas 1 : Tas Vide");

        Tas t = new Tas();

        verifier("estVide() doit renvoyer true sur un tas vide", t.estVide());
        verifier("sommetExists(\"X\") doit renvoyer false sur un tas vide", !t.sommetExists("X"));

        System.out.println();
    }

    // Cas 2 : ajout simple, vérification du minimum
    private static void testAjoutSimpleEtMinimum() {
        affichageTitre("Tas - Cas 2 : ajout simple et minimum");

        Tas t = new Tas();
        t.ajouter("A", 10);

        verifier("le tas ne doit pas être vide après un ajout", !t.estVide());

        Map.Entry<String, Integer> min = t.minimum();
        verifier("minimum() doit renvoyer (A, 10)", min.getKey().equals("A") && min.getValue() == 10);

        t.ajouter("B", 5);
        min = t.minimum();
        verifier("après ajout de B (5), le minimum doit être (B, 5)",
                min.getKey().equals("B") && min.getValue() == 5);

        t.ajouter("C", 20);
        min = t.minimum();
        verifier("après ajout de C (20), le minimum doit toujours être (B, 5)",
                min.getKey().equals("B") && min.getValue() == 5);

        verifierProprieteTas("après les 3 ajouts", t);
        verifierTaillesCoherentes("après les 3 ajouts", t, 3);

        System.out.println();
    }

    // Cas 3 : insertion dans un ordre aléatoire -> minimum toujours correct
    private static void testOrdreInsertionAleatoire() {
        affichageTitre("Tas - Cas 3 : insertion dans le désordre");

        Tas t = new Tas();
        int[] valeurs = {15, 3, 27, 1, 9, 42, -5, 8};
        String[] noms = {"E15", "E3", "E27", "E1", "E9", "E42", "Em5", "E8"};

        for (int i = 0; i < valeurs.length; i++) {
            t.ajouter(noms[i], valeurs[i]);
        }

        Map.Entry<String, Integer> min = t.minimum();
        verifier("le minimum doit être (Em5, -5)", min.getKey().equals("Em5") && min.getValue() == -5);

        verifierProprieteTas("après insertions désordonnées", t);
        verifierTaillesCoherentes("après insertions désordonnées", t, valeurs.length);

        System.out.println();
    }

    // Cas 4 : retirer renvoie les éléments triés par ordre croissant
    private static void testRetirerOrdreTri() {
        affichageTitre("Tas - Cas 4 : retirer() -> ordre croissant (tri par tas)");

        Tas t = new Tas();
        int[] valeurs = {15, 3, 27, 1, 9, 42, -5, 8, 8};
        String[] noms = {"E15", "E3", "E27", "E1", "E9", "E42", "Em5", "E8a", "E8b"};

        for (int i = 0; i < valeurs.length; i++) {
            t.ajouter(noms[i], valeurs[i]);
        }

        List<Integer> resultats = new ArrayList<>();
        boolean propriétéToujoursValide = true;

        while (!t.estVide()) {
            Map.Entry<String, Integer> r = t.retirer();
            resultats.add(r.getValue());
            if (!t.estVide()) {
                propriétéToujoursValide &= proprieteTasValide(t);
            }
            verifierTaillesCoherentes("après un retirer", t, valeurs.length - resultats.size());
        }

        verifier("la propriété de tas est respectée après chaque retirer",
                propriétéToujoursValide);

        List<Integer> trie = new ArrayList<>(resultats);
        Collections.sort(trie);
        verifier("retirer() successifs renvoient les valeurs dans l'ordre croissant : "
                        + resultats,
                resultats.equals(trie));

        verifier("le tas est vide après avoir tout retiré", t.estVide());
        verifier("sommetExists ne renvoie plus rien après suppression complète",
                !t.sommetExists("E15") && !t.sommetExists("Em5"));

        System.out.println();
    }

    // Cas 5 : sommetExists
    private static void testSommetExists() {
        affichageTitre("Tas - Cas 5 : sommetExists");

        Tas t = new Tas();
        t.ajouter("A", 10);
        t.ajouter("B", 5);

        verifier("sommetExists(\"A\") doit renvoyer true", t.sommetExists("A"));
        verifier("sommetExists(\"B\") doit renvoyer true", t.sommetExists("B"));
        verifier("sommetExists(\"Z\") doit renvoyer false", !t.sommetExists("Z"));

        t.retirer(); // retire B (minimum)
        verifier("après retrait du minimum (B), sommetExists(\"B\") doit renvoyer false",
                !t.sommetExists("B"));
        verifier("après retrait du minimum (B), sommetExists(\"A\") doit toujours renvoyer true",
                t.sommetExists("A"));

        System.out.println();
    }

    // Cas 6 : modifierPriorite - diminution de valeur
    private static void testModifierPrioriteDiminution() {
        affichageTitre("Tas - Cas 6 : modifierPriorite - diminution");

        Tas t = new Tas();
        t.ajouter("A", 10);
        t.ajouter("B", 20);
        t.ajouter("C", 30);
        t.ajouter("D", 40);

        Map.Entry<String, Integer> min = t.minimum();
        verifier("minimum initial = (A, 10)", min.getKey().equals("A") && min.getValue() == 10);

        // On diminue D de 40 à 1, il doit devenir le minimum
        t.modifierPriorite("D", 1);
        min = t.minimum();
        verifier("après modifierPriorite(D, 1), le minimum doit être (D, 1)",
                min.getKey().equals("D") && min.getValue() == 1);

        verifierProprieteTas("après diminution de D", t);
        verifierTaillesCoherentes("après diminution de D", t, 4);

        // Vérification finale par tri complet
        verifierOrdreComplet(t, "diminution");

        System.out.println();
    }

    // Cas 7 : modifierPriorite - augmentation de valeur
    private static void testModifierPrioriteAugmentation() {
        affichageTitre("Tas - Cas 7 : modifierPriorite - augmentation");

        Tas t = new Tas();
        t.ajouter("A", 1);
        t.ajouter("B", 20);
        t.ajouter("C", 30);
        t.ajouter("D", 40);

        Map.Entry<String, Integer> min = t.minimum();
        verifier("minimum initial = (A, 1)", min.getKey().equals("A") && min.getValue() == 1);

        // On augmente A de 1 à 100, le minimum doit changer (devenir B = 20)
        t.modifierPriorite("A", 100);
        min = t.minimum();
        verifier("après modifierPriorite(A, 100), le minimum doit être (B, 20)",
                min.getKey().equals("B") && min.getValue() == 20);

        verifierProprieteTas("après augmentation de A", t);
        verifierTaillesCoherentes("après augmentation de A", t, 4);

        verifierOrdreComplet(t, "augmentation");

        System.out.println();
    }

    // Cas 8 : tas avec un seul élément
    private static void testUnSeulElement() {
        affichageTitre("Tas - Cas 8 : tas à un seul élément");

        Tas t = new Tas();
        t.ajouter("Solo", 42);

        Map.Entry<String, Integer> min = t.minimum();
        verifier("minimum() = (Solo, 42)", min.getKey().equals("Solo") && min.getValue() == 42);

        Map.Entry<String, Integer> retire = t.retirer();
        verifier("retirer() = (Solo, 42)", retire.getKey().equals("Solo") && retire.getValue() == 42);

        verifier("le tas est vide après avoir retiré son unique élément", t.estVide());
        verifier("sommetExists(\"Solo\") = false après retrait", !t.sommetExists("Solo"));

        System.out.println();
    }

    // Cas 9 : valeurs dupliquées
    private static void testValeursDupliquees() {
        affichageTitre("Tas - Cas 9 : valeurs dupliquées");

        Tas t = new Tas();
        t.ajouter("A", 5);
        t.ajouter("B", 5);
        t.ajouter("C", 5);

        verifierProprieteTas("avec 3 valeurs identiques (5)", t);
        verifierTaillesCoherentes("avec 3 valeurs identiques", t, 3);

        // Tous les retraits doivent renvoyer la valeur 5, peu importe l'ordre des noms
        List<String> nomsRetires = new ArrayList<>();
        while (!t.estVide()) {
            Map.Entry<String, Integer> r = t.retirer();
            verifier("retirer() doit renvoyer la valeur 5 (élément " + r.getKey() + ")",
                    r.getValue() == 5);
            nomsRetires.add(r.getKey());
        }

        Set<String> attendu = new HashSet<>(Arrays.asList("A", "B", "C"));
        verifier("les 3 sommets A, B, C ont bien été retirés (peu importe l'ordre) : " + nomsRetires,
                new HashSet<>(nomsRetires).equals(attendu) && nomsRetires.size() == 3);

        System.out.println();
    }

    /**
     * 
     * Methodes utilitaires aux tests ci dessus
     *  
     */

    /**
     * Affiche un message OK/ECHEC selon la condition donnée.
     */
    private static void verifier(String description, boolean condition) {
        if (condition) {
            System.out.println("OK     : " + description);
        } else {
            System.out.println("ECHEC  : " + description);
        }
    }

    /**
     * Vérifie que sommets.size() == valeurs.size() == tailleAttendue.
     */
    private static void verifierTaillesCoherentes(String contexte, Tas t, int tailleAttendue) {
        int tailleSommets = t.getSommets().size();
        int tailleValeurs = t.getValeurs().size();
        verifier(contexte + " : tailles cohérentes (sommets=" + tailleSommets + ", valeurs=" + tailleValeurs + ", attendu=" + tailleAttendue + ")", tailleSommets == tailleValeurs && tailleSommets == tailleAttendue);
    }

    /**
     * Vérifie la propriété de tas-min : pour chaque indice i ayant des enfants,
     * valeurs[i] <= valeurs[enfant].
     */
    private static void verifierProprieteTas(String contexte, Tas t) {
        verifier(contexte + " : propriété de tas (min-heap) respectée", proprieteTasValide(t));
    }

    private static boolean proprieteTasValide(Tas t) {
        ArrayList<Integer> valeurs = t.getValeurs();
        int n = valeurs.size();
        for (int i = 0; i < n; i++) {
            int fg = 2 * i + 1;
            int fd = 2 * i + 2;
            if (fg < n && valeurs.get(i) > valeurs.get(fg)) {
                return false;
            }
            if (fd < n && valeurs.get(i) > valeurs.get(fd)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Vide une COPIE du tas via des retraits successifs et vérifie que les
     * valeurs sortent dans l'ordre croissant. Comme Tas ne propose pas de
     * méthode de clonage, on reconstruit un nouveau tas avec les mêmes
     * couples (sommet, valeur) à partir des listes internes.
     */
    private static void verifierOrdreComplet(Tas original, String contexte) {
        Tas copie = new Tas();
        ArrayList<String> sommets = original.getSommets();
        ArrayList<Integer> valeurs = original.getValeurs();
        for (int i = 0; i < sommets.size(); i++) {
            copie.ajouter(sommets.get(i), valeurs.get(i));
        }

        List<Integer> resultats = new ArrayList<>();
        while (!copie.estVide()) {
            resultats.add(copie.retirer().getValue());
        }

        List<Integer> trie = new ArrayList<>(resultats);
        Collections.sort(trie);

        verifier(contexte + " : ordre de sortie croissant sur une copie du tas : " + resultats, resultats.equals(trie));
    }
}