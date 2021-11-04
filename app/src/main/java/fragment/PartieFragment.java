package fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fortnitetool.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import activity.MainActivity;
import modele.Partie;
import modele.Point;
import utils.AbstractOnItemListener;
import utils.Validateur;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PartieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PartieFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final int PAS_SCORE = -1;
    private Spinner cmbRaison;
    private Spinner cmbJoueur;
    private Button btnEnregistrer;
    private TextView txtScore;
    private String pointAmeliorer;
    private String nomjoueur;
    private int points = PAS_SCORE;
    private ArrayList<String> nomJoueurs;
    private ArrayList<String> pointsAmeliorer;
    private AdView pub;



    // TODO: Rename and change types of parameters
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
       // nomJoueurs = Persistable.toArrayString((ArrayList<Persistable>) (Object)activity.getJoueurs());
        pointsAmeliorer = Point.toArrayString(activity.getPoints());
        setWidgets(view);
        setListeners();
        initialiserPub(view);

        // Inflate the layout for this fragment
        return view;
    }
    private void setListeners() {
        btnEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enregistrerFormulaire();
            }
        });
        cmbRaison.setOnItemSelectedListener(new AbstractOnItemListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pointAmeliorer = cmbRaison.getSelectedItem().toString();
            }});
    }
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
    private void enregistrerFormulaire() {
        int reponse = Validateur.verifierFormulaire(this);
        if (reponse == Validateur.SUCCES) {
            enregistrerPoint();
            //enregistrerScore();
            Toast.makeText(getActivity(), R.string.PartieEnregistree, Toast.LENGTH_SHORT).show();
        } else if (reponse == Validateur.PAS_POINT_AMELIORER) {
            Toast.makeText(getActivity(), R.string.SaisirPointAmeliorer, Toast.LENGTH_SHORT).show();
        } else if (reponse == Validateur.PAS_DE_SCORE) {
            Toast.makeText(getActivity(), R.string.SaisirScore, Toast.LENGTH_SHORT).show();
        } else if (reponse == Validateur.PAS_DE_JOUEUR) {
            Toast.makeText(getActivity(), R.string.SaisirJoueur, Toast.LENGTH_SHORT).show();
        }
    }
    private void enregistrerPoint() {
        Timestamp temps = new Timestamp(Calendar.getInstance().getTime().getTime());
        Partie partie = new Partie(temps, pointAmeliorer);
        MainActivity activity = (MainActivity) getActivity();
        activity.ajouterPartie(partie);
    }

    private void setWidgets(View view) {
        cmbRaison = (Spinner) view.findViewById(R.id.cmbRaisons);
        btnEnregistrer = view.findViewById(R.id.btnEnregistrer);

        //2. Adapter
        ArrayAdapter<String> adapteurRaison = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner, pointsAmeliorer);
        adapteurRaison.setDropDownViewResource(R.layout.spinner_drop_down);

        //3. Ler l'adapter avec lsiting
        cmbRaison.setAdapter(adapteurRaison);
    }

    public String getPointAmeliorer() {
        return pointAmeliorer;
    }
    public String getNomjoueur() {
        return nomjoueur;
    }
    public int getPoints() {
        return points;
    }
}