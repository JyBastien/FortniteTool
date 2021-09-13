package modele;

import java.sql.Timestamp;

public class Score {
    private Timestamp temps;
    private String joueur;
    private int score;

    public Score(Timestamp temps, String joueur, int score) {
        this.temps = temps;
        this.joueur = joueur;
        this.score = score;
    }

    public Timestamp getTemps() {
        return temps;
    }

    public void setTemps(Timestamp temps) {
        this.temps = temps;
    }

    public String getJoueur() {
        return joueur;
    }

    public void setJoueur(String joueur) {
        this.joueur = joueur;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
