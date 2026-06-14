package Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Tas {
    /**
     * Implémentation du tas avec une liste pour assurer la complétude du tas, chose non possible avec les pointeurs
     * 
     * Listes parallèles au lieu d'une classe Noeud qui ferait la paire par volonté de simplification, pourrait causer probleme
     * 
     * La hashmap associe a chaque string son index dans le tableau pour pouvoir modifier en O(log n) les valeurs
     */
    public ArrayList<String> sommets;
    public ArrayList<Integer> valeurs;
    private HashMap<String, Integer> indexMap;

    public Tas(){
        this.sommets = new ArrayList<String>();
        this.valeurs = new ArrayList<Integer>();
        this.indexMap = new HashMap<String, Integer>();
    }

    public Boolean sommetExists(String s){
        return indexMap.containsKey(s); //containskey sur indexmap car O(1) alors que contains sur ArrayList O(n)
    }

    public ArrayList<String> getSommets(){
        return this.sommets;
    }

    public ArrayList<Integer> getValeurs(){
        return this.valeurs;
    }

    public boolean estVide(){
        //OU au lieu de ET en cas de desynchro des deux arraylist
        return this.sommets.isEmpty() || this.valeurs.isEmpty();
    }

    //indice fils gauche
    private int fd(int i){
            return 2*i+2;        
    }
    //indice fils droit
    private int fg(int i){
            return 2*i+1;
    }
    //indice pere
    private int pere(int i) {
        return (i-1) / 2; //pas besoin de math.floor, java le fait par défaut
    }

    //echange deux sommets dont on donne l'indice
    private void echanger(int i,int j){
        String tmpS = sommets.get(i); 
        sommets.set(i,sommets.get(j)); 
        sommets.set(j,tmpS);
        int tmpV = valeurs.get(i); 
        valeurs.set(i,valeurs.get(j)); 
        valeurs.set(j,tmpV);

        // mise a jour de la hashmap apres l'echange
        indexMap.put(sommets.get(i), i);
        indexMap.put(sommets.get(j), j);
    }

    /**
     * Ajoute un sommet au tas (fr.wikipedia.org/wiki/Tas_binaire pour l'algorithme d'ajout)
     * @param s String : sommet a ajouter a l'arbre
     * @param v int : valeur a ajouter a l'arbre (associée au sommet)
     */
    public void ajouter(String s,int v)
    {
        int indiceInsertion = this.valeurs.size();
        this.valeurs.add(v);
        this.sommets.add(s);
        indexMap.put(s, indiceInsertion);

        int indicePere = pere(indiceInsertion);
        int valPere = this.valeurs.get(indicePere);

        //si l'élément ajouté est le premier du tas, pas la peine de boucler
        if (indiceInsertion == 0) {
            return;
        }


        //une fois le noeud ajouté a l'arbre, on va vérifier s'il respecte la contrainte de positionnement (pour un arbre min -> être une valeur supérieure à son père, car le noeud le plus petit doit se retrouver au sommet de l'arbre)
        while(valPere>v && indiceInsertion>0){
            echanger(indiceInsertion, indicePere);

            //l'indice de notre valeur est désormais a l'indice que le pere occupait
            indiceInsertion = indicePere;

            //si on est arrivé a la racine on sort de la boucle
            if (indiceInsertion == 0) {
                break;
            }

            //le pere qu'on va vérifier est le pere du pere qu'on venait de modifier
            indicePere = pere(indicePere);
            //on recupere la valeur du nouveau pere
            valPere = this.valeurs.get(indicePere);

            // System.out.println("val a ajouter : " + v + " pere : "+valPere);
        }
     }

    /**
     * Recuperation du minimum
     * @return Une map qui possede en clef,valeur la paire :  sommet,valeur
     */
    public Map.Entry<String, Integer> minimum() {
        return Map.entry(this.sommets.get(0), this.valeurs.get(0));
    }
    /**
     * Retire la racine du tas (fr.wikipedia.org/wiki/Tas_binaire pour l'algorithme)
     */
    public Map.Entry<String, Integer> retirer() {
        String tmpS = this.sommets.get(0);
        int tmpV = this.valeurs.get(0);
        //sauvegarde du resultat en map pour pouvoir return la valeur et le nom du sommet
        Map.Entry<String, Integer> resultat = Map.entry(tmpS, tmpV);

        int dernierIndice = this.valeurs.size() - 1;

        //pour éviter de décaler la liste et impacter l'ordre des sommets de l'arbre, on va remplacer le premier sommet par le dernier et supprimer le dernier au lieu de supprimer le premier
        this.valeurs.set(0, this.valeurs.get(dernierIndice));
        this.sommets.set(0, this.sommets.get(dernierIndice));

        // le sommet qui vient d'etre deplace en position 0 doit etre mis a jour dans la map,
        // et le sommet retire doit en etre supprime
        indexMap.put(this.sommets.get(0), 0);
        indexMap.remove(tmpS);
        
        //suppression du dernier élément, maintenant en doublon
        this.valeurs.remove(dernierIndice);
        this.sommets.remove(dernierIndice);

        int tabSize = this.valeurs.size();
        int indiceNouvelleRacine = 0;
        boolean bienPlace = false;

        while (!bienPlace) {
            int indiceFg = fg(indiceNouvelleRacine);
            int indiceFd = fd(indiceNouvelleRacine);
            int indiceMin = indiceNouvelleRacine;

            //comparaison fils gauche fils droit, va échanger la racine avec le plus petit des deux (on veut récuperer le plus petit chiffre à la racine)
            if (indiceFg < tabSize && this.valeurs.get(indiceFg) < this.valeurs.get(indiceMin)) {
                indiceMin = indiceFg;
            }
            if (indiceFd < tabSize && this.valeurs.get(indiceFd) < this.valeurs.get(indiceMin)) {
                indiceMin = indiceFd;
            }

            if (indiceMin != indiceNouvelleRacine) {
                echanger(indiceNouvelleRacine, indiceMin);
                indiceNouvelleRacine = indiceMin;
            } else {
                // La racine est plus petite que ses deux fils : elle est bien placée
                bienPlace = true;
            }
        }

        return resultat;
    }

    /**
     * Modifie la priorité d'un sommet identifié par som nom, part du principe que les sommets ont tous un nom unique.
     * @param sommet nom du sommet dont la priorité doit être modifiée
     * @param nouvelleValeur
     */
    public void modifierPriorite(String sommet,int nouvelleValeur){
        Integer indice = indexMap.get(sommet);
        if (indice == null) return;

        int ancienneValeur = this.valeurs.get(indice);
        this.valeurs.set(indice, nouvelleValeur);

        //repositionnement du sommet en fonction de sa nouvelle valeur
        
        //si la nouvelle valeur du sommet est plus petite que son ancienne, on la remonte autant que possible, comme dans ajouter 
        if (nouvelleValeur < ancienneValeur) 
        {
            int indicePere = pere(indice);
            while (indice > 0 && this.valeurs.get(indicePere) > nouvelleValeur) {
                echanger(indice, indicePere);
                indice = indicePere;
                indicePere = pere(indice);
            }
        }
        //si le nouvelle valeur est plus grande que l'ancienne, on la descend jusque à sa place, comme dans retirer
        //TODO : refacto monter et descendre car actuellement il y a des dulicatas 
        else if (nouvelleValeur > ancienneValeur) 
        {
            boolean bienPlace = false;
            int tabSize = this.valeurs.size();
            while (!bienPlace) {
                int indiceFg = fg(indice);
                int indiceFd = fd(indice);
                int indiceMin = indice;

                if (indiceFg < tabSize && this.valeurs.get(indiceFg) < this.valeurs.get(indiceMin)) {
                    indiceMin = indiceFg;
                }
                if (indiceFd < tabSize && this.valeurs.get(indiceFd) < this.valeurs.get(indiceMin)) {
                    indiceMin = indiceFd;
                }

                if (indiceMin != indice) {
                    echanger(indice, indiceMin);
                    indice = indiceMin;
                } else {
                    bienPlace = true;
                }
            }
        }

    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        
        int size = this.sommets.size();
        for (int i = 0; i < size; i++) {
            sb.append(this.sommets.get(i)).append(":").append(this.valeurs.get(i));
            
            if (i < size - 1) {
                sb.append(",");
            }
        }
        
        sb.append("]");
        return sb.toString();
    }
}