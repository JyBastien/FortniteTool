package fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.example.fortnitetool.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import activity.MainActivity;
import modele.Partie;
import modele.Point;
import utils.Validateur;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PartieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
/*Permet de recueillir les données statistiques d'une Partie
* le nom du point et la date de la partie*/
public class PartieFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int PAS_SCORE = -1;
    private PowerSpinnerView cmbRaison;
    private Button btnEnregistrer;
    private String pointAmeliorer;
    private int points = PAS_SCORE;
    private ArrayList<String> pointsAmeliorer;
    private AdView pub;
    private String mParam1;
    private String mParam2;

    public PartieFragment() {/* Required empty public constructor */}
    public static PartieFragment newInstance(String param1, String param2) {
        PartieFragment fragment = new PartieFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_partie, container, false);
        MainActivity activity = (MainActivity) getActivity();
        pointsAmeliorer = Point.toArrayString(activity.getPoints());
        setWidgets(view);
        setListeners(view);
        initialiserPub(view);
        return view;
    }
    /*associe les objets graphiques du layout au ojets de la classe et
    * initialise les composants*/
    private void setWidgets(View view) {
        cmbRaison = view.findViewById(R.id.cmbRaisons);
        btnEnregistrer = view.findViewById(R.id.btnEnregistrer);
        //2. Adapter
        ArrayAdapter<String> adapteurRaison = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner, pointsAmeliorer);
        adapteurRaison.setDropDownViewResource(R.layout.spinner_drop_down);
        //3. Ler l'adapter avec lsiting
        cmbRaison.setItems(pointsAmeliorer);
        cmbRaison.selectItemByIndex(0);
        pointAmeliorer = pointsAmeliorer.get(0);
        //cmbRaison.setAdapter(adapteurRaison);
    }
    /*attache des listeners aux bouton enregistré et spinner qui contient les points
    * à améliorer*/
    private void setListeners(View view) {
        btnEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enregistrerFormulaire();
            }
        });
        cmbRaison.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            @Override
            public void onItemSelected(int i, @Nullable String s, int i1, String selectedItem) {
                pointAmeliorer = selectedItem;
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cmbRaison.dismiss();
            }
        });
    }
    /*initialise la bannière publicitaire*/
    private void initialiserPub(View view) {
        MobileAds.initialize(view.getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        pub = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        pub.loadAd(adRequest);
    }
    /*récupère l'information du spinner cmbParties avant d'enregistrer la partie*/
    private void enregistrerFormulaire() {
        int reponse = Validateur.verifierFormulaire(this);
        if (reponse == Validateur.SUCCES) {
            enregistrerPoint();
            Toast.makeText(getActivity(), R.string.PartieEnregistree, Toast.LENGTH_SHORT).show();
        } else if (reponse == Validateur.PAS_POINT_AMELIORER) {
            Toast.makeText(getActivity(), R.string.SaisirPointAmeliorer, Toast.LENGTH_SHORT).show();
        }
    }
    /*enregistre dans la BD le point afficher dans le spinner cmbParties*/
    private void enregistrerPoint() {
        Timestamp temps = new Timestamp(Calendar.getInstance().getTime().getTime());
        Partie partie = new Partie(temps, pointAmeliorer);
        MainActivity activity = (MainActivity) getActivity();
        activity.ajouterPartie(partie);
    }
    /*permet de faire disparaitre le menu dropdown de son spinner
    * lorsque l'utilisateur change de page en le laissant déroulé*/
    public void dismiss(){
        cmbRaison.dismiss();
    }

    public String getPointAmeliorer() {
        return pointAmeliorer;
    }
    public int getPoints() {
        return points;
    }
}