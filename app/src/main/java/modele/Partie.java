package modele;

import java.sql.Timestamp;

public class Partie {
    private Timestamp temps;
    private String pointAmeliorer;

    public Partie(Timestamp temps, String pointAmeliorer) {
        this.temps = temps;
        this.pointAmeliorer = pointAmeliorer;
    }

    public Timestamp getTemps() {
        return temps;
    }

    public void setTemps(Timestamp temps) {
        this.temps = temps;
    }

    public String getPointAmeliorer() {
        return pointAmeliorer;
    }

    public void setPointAmeliorer(String pointAmeliorer) {
        this.pointAmeliorer = pointAmeliorer;
    }
}
