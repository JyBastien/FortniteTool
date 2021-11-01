package modele;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public static ArrayList<Stats> getStatsParJour(ArrayList<Partie> partiesOrderedParJour) {
        ArrayList<Stats> stats = getStatsParIntervalJour(partiesOrderedParJour);
        return stats;
    }
    public static ArrayList<Stats> getStatsParSemaine(ArrayList<Partie> partiesOrderedParJour) {
        ArrayList<Stats> stats =  getStatsParIntervalSemaine(partiesOrderedParJour);
        //Pour commencer avec le premier du mois
        //dateFin = dateDebut.withDayOfMonth(1);

        return stats;
    }
    private static ArrayList<Stats> getStatsParIntervalJour(ArrayList<Partie> partiesOrderedParJour) {
        ArrayList<Stats> stats = new ArrayList<>();
        Partie partie;
        LocalDate ancienneDate = null;
        LocalDate date = null;
        HashMap<String,Integer> statsBuilder = new HashMap<>();

        String pointAmeliorer;
        int qte = 0;

        for (int i = 0; i < partiesOrderedParJour.size() && stats.size() < 5; i++)
        {
            partie = partiesOrderedParJour.get(i);
            date = partie.getDate();
            if (ancienneDate == null){
                ancienneDate = date;
            }

            //si la deta depasse la periode observée
            if (date.isBefore(ancienneDate)) {
                //on compile les resultats et cré le stat
                Stats stat = getStats(statsBuilder, date, date);
                stats.add(stat);
                //date de debut de la prochaine statistique
                ancienneDate = date;
                statsBuilder = new HashMap<>();
            }
            //stats building
            pointAmeliorer = partie.getPointAmeliorer();
            if (statsBuilder.containsKey(pointAmeliorer)){
                qte = statsBuilder.get(partie.getPointAmeliorer()) + 1;
            } else {
                qte = 1;
            }
            statsBuilder.put(pointAmeliorer, qte);
        }
        if (statsBuilder.size() > 0){
            Stats stat = getStats(statsBuilder, date, date);
            stats.add(stat);
        }
        return stats;
    }

    private static ArrayList<Stats> getStatsParIntervalSemaine(ArrayList<Partie> partiesOrderedParJour) {

        Partie partie;
        LocalDate dateDebut = null;
        LocalDate date = null;
        LocalDate dateFin = null;
        ArrayList<Stats> stats = new ArrayList<>();
        HashMap<String,Integer> statsBuilder = new HashMap<>();
        String pointAmeliorer;
        int qte = 0;

        for (int i = 0; i < partiesOrderedParJour.size() && stats.size() < 5; i++)
        {
            partie = partiesOrderedParJour.get(i);
            date = partie.getDate();
            if (dateDebut == null){
                dateDebut = date.with(DayOfWeek.MONDAY);
                dateFin = dateDebut.plusDays(6);
            }

            //si la deta depasse la periode observée
            if (date.isBefore(dateDebut)) {
                //on compile les resultats et cré le stat
                Stats stat = getStats(statsBuilder, dateDebut, dateFin);
                stats.add(stat);
                //date de debut de la prochaine statistique
                dateDebut = date.with(DayOfWeek.MONDAY);
                dateFin = dateDebut.plusDays(6);
                statsBuilder = new HashMap<>();
            }
            //stats building
            pointAmeliorer = partie.getPointAmeliorer();
            if (statsBuilder.containsKey(pointAmeliorer)){
                qte = statsBuilder.get(partie.getPointAmeliorer()) + 1;
            } else {
                qte = 1;
            }
            statsBuilder.put(pointAmeliorer, qte);
        }
        if (statsBuilder.size() > 0){
            Stats stat = getStats(statsBuilder, dateDebut, dateFin);
            stats.add(stat);
        }
        return stats;
    }

    private static void initialiserDateStatsIntervalSemaine(LocalDate dateDebut, LocalDate date, LocalDate dateFin) {
        dateDebut = date.with(DayOfWeek.MONDAY);
        dateFin = dateDebut.plusDays(6);
    }


    public static Stats getStats(HashMap<String, Integer> statsBuilder, LocalDate dateDebut, LocalDate dateFin) {
        int highScore = 0;
        String pointHighScore = null;
        int qteTotal = 0;
        double ratio;

        for (Map.Entry<String,Integer> resultat : statsBuilder.entrySet()){
            if (resultat.getValue() > highScore ){
                highScore = resultat.getValue();
                pointHighScore = resultat.getKey();
            }
            qteTotal += resultat.getValue();
        }
        Stats stat = null;
        if (qteTotal > 0) {
            ratio = ((double) highScore) / qteTotal;
            stat = new Stats(7, pointHighScore, ratio, dateDebut, dateFin, statsBuilder,qteTotal);
        }
        return stat;
    }
}
