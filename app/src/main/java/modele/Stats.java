package modele;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
        Partie partie = partiesOrderedParJour.get(0);
        LocalDate date = partie.getDate();
        LocalDate dateDebut = date.minusDays(1);
        ArrayList<Stats> stats = getStatsParInterval(partiesOrderedParJour, dateDebut, 1);
        return stats;
    }
    public static ArrayList<Stats> getStatsParSemaine(ArrayList<Partie> partiesOrderedParJour) {
        Partie partie = partiesOrderedParJour.get(0);
        LocalDate date = partie.getDate();
        LocalDate dateDebut = date.with(DayOfWeek.MONDAY);
        dateDebut = dateDebut.isEqual(date)? dateDebut.minusDays(7):dateDebut;
        ArrayList<Stats> stats =  getStatsParInterval(partiesOrderedParJour, dateDebut, 7);
        //Pour commencer avec le premier du mois
        //dateFin = dateDebut.withDayOfMonth(1);

        return stats;
    }

    private static ArrayList<Stats> getStatsParInterval(ArrayList<Partie> partiesOrderedParJour, LocalDate dateDebut, int interval) {
        ArrayList<Stats> stats = new ArrayList<>();
        Partie partie;
        LocalDate date;
        LocalDate dateFin = dateDebut.plusDays(interval);
        HashMap<String,Integer> statsBuilder = new HashMap<>();

        String pointAmeliorer;
        int qte = 0;

        for (int i = 0; i < partiesOrderedParJour.size() && stats.size() < 5; i++)
        {
            partie = partiesOrderedParJour.get(i);
            date = partie.getDate();

            //stats building
            pointAmeliorer = partie.getPointAmeliorer();
            if (statsBuilder.containsKey(pointAmeliorer)){
                qte = statsBuilder.get(partie.getPointAmeliorer()) + 1;
            } else {
                qte = 1;
            }
            //si la deta depasse la periode observée
            if (date.isBefore(dateDebut) || date.isEqual(dateDebut)) {
                //on compile les resultats et cré le stat
                Stats stat = getStats(statsBuilder, dateDebut, dateFin);
                stats.add(stat);
                //date de debut de la prochaine statistique
                dateFin = dateDebut;
                dateDebut = date.minusDays(interval);
                statsBuilder = new HashMap<>();
            }
            statsBuilder.put(pointAmeliorer, qte);
        }
        if (statsBuilder.size() > 0){
            Stats stat = getStats(statsBuilder, dateDebut, dateFin);
            stats.add(stat);
        }
        return stats;
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
