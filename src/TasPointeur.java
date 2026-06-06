public class TasPointeur {
    /**
     * Implémentation initiale de la classe tas, basé sur ce qui a été réalisé en cours sur les opérations
     * Abandonné pour les raisons précisées dans le commentaire de tas.java
     */


    
    private String sommet;
    private int valeur;
    private TasPointeur fg;
    private TasPointeur fd;
    /***
     * Constructeur pour créer une feuille
     * @param s sommet auquel la feuille est
     * @param v valeur accumulée par l'algorithme jusque la
     */
    public TasPointeur(String s,int v) {
        this.sommet = s;
        this.valeur =v;
        this.fg = null;
        this.fd = null;
    }

    /***
     * Constructeur pour un noeud non feuille
     * @param s sommet auquel la feuille est
     * @param v valeur accumulée par l'algorithme jusque la
     * @param gauche fils gauche du noeud
     * @param droit fils droit du noeud
     */
    public TasPointeur(String s,int v, TasPointeur gauche, TasPointeur droit) {
        this.sommet = s;
        this.valeur =v;
        this.fg = gauche;
        this.fd = droit;
    }

    public boolean estFeuille() {
        return fd ==  null && fg == null;
    }

    public String getSommet(){
        return this.sommet;
    }

    public int getValeur(){
        return this.valeur;
    }

    public TasPointeur getFD(){
        return this.fd;
    }

    public TasPointeur getFG(){
        return this.fg;
    }

    @Override
    public String toString() {
        String s = "";
        String racine = sommet + " - " + Integer.toString(valeur);
        if (this.estFeuille()) s = racine.toString();
        else {
            s += "(" + fg.toString() ;
            s += racine.toString();
            s += fd.toString() + ")";
        }
        return s;
    }


}

