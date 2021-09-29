package utils;

import fragment.PartieFragment;

public class Validateur {
    public static final int PAS_POINT_AMELIORER = 4;
    public static final int PAS_DE_SCORE = 3;
    public static final int PAS_DE_JOUEUR = 2;
    public static final int SUCCES = 1;

    public static int verifierFormulaire(PartieFragment partieFragment) {
        if (partieFragment.getPointAmeliorer() == null){
            return PAS_POINT_AMELIORER;
        }else if (partieFragment.getPoints() == PartieFragment.PAS_SCORE){
            return PAS_DE_SCORE;
        }else if (partieFragment.getNomjoueur() == null){
            return PAS_DE_JOUEUR;
        }else{
            return SUCCES;
        }
    }
}
