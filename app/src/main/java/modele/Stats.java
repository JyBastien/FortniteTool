package modele;

import java.time.LocalDate;
import java.util.HashMap;

public class Stats {
    private int statsID;
    private int interval;
    private String nom;
    private double ratio;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private int qteEntrees;
    private HashMap<String,Integer> entrees;

    public Stats(int interval, String nom, double ratio, LocalDate dateDebut, LocalDate dateFin, HashMap<String,Integer> entrees,int qteEntrees) {

        this.interval = interval;
        this.nom = nom;
        this.ratio = ratio;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.entrees = entrees;
        this.qteEntrees = qteEntrees;
    }

    public int getQteEntrees() {
        return qteEntrees;
    }

    public void setQteEntrees(int qteEntrees) {
        this.qteEntrees = qteEntrees;
    }

    public HashMap<String, Integer> getEntrees() {
        return entrees;
    }

    public void setEntrees(HashMap<String, Integer> entrees) {
        this.entrees = entrees;
    }

    public int getStatsID() {
        return statsID;
    }

    public void setStatsID(int statsID) {
        this.statsID = statsID;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getNomPoint() {
        return nom;
    }

    public void setNomPoint(String nomPoint) {
        this.nom = nomPoint;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }
}
