package utils;

import fragment.PartieFragment;
/*Classe qui s'assure que le point à améliorer est inclus dans le fragment*/
public class Validateur {
    public static final int PAS_POINT_AMELIORER = 0;
    public static final int SUCCES = 1;

    public static int verifierFormulaire(PartieFragment partieFragment) {
        if (partieFragment.getPointAmeliorer() == null){
            return PAS_POINT_AMELIORER;
        }
            return SUCCES;
    }
}
