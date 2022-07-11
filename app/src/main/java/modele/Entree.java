package modele;
/*Classe qui est utilisée pour permettre de trier l'arraylist qui contiendra les instances
* par son comparable qu'elle implémente*/
public class Entree implements Comparable{
    private String texte;
    private int qte;

    public Entree(String texte, int qte) {
        this.texte = texte;
        this.qte = qte;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    @Override
    public int compareTo(Object o) {
        Entree entreeComparee = (Entree) o;
        return entreeComparee.qte - this.qte;
    }
}
