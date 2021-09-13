package fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
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

import java.sql.Timestamp;
import java.util.Calendar;

import activity.MainActivity;
import modele.Partie;
import modele.Score;


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
    private Spinner cmbRaison;
    private Spinner cmbJoueur;
    private Button btnRaison;
    private Button btnScore;
    private TextView txtScore;
    private String pointAmeliorer;
    private String nomjoueur;
    private int points = -1;
    private String[] nomJoueurs;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PartieFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PArtieFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        View view = inflater.inflate(R.layout.fragment_partie, container,false);
        MainActivity activity = (MainActivity) getActivity();
        nomJoueurs = activity.getNomJoueurs();
        setWidgets(view);
        setListeners();


        // Inflate the layout for this fragment
        return view;
    }

    private void setListeners() {
        btnRaison.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pointAmeliorer != null){
                    Timestamp temps = new Timestamp(Calendar.getInstance().getTime().getTime());
                    Partie partie = new Partie(temps, pointAmeliorer);
                    MainActivity activity = (MainActivity) getActivity();
                    activity.ajouterPartie(partie);
                }else {
                    //todo user ressource
                    Toast.makeText(getActivity(), "Veuillez sélectionner un point à améliorer", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nomjoueur != null){
                    if (points != -1){
                        Timestamp temps = new Timestamp(Calendar.getInstance().getTime().getTime());
                        Score score = new Score(temps,nomjoueur, points);
                        MainActivity activity = (MainActivity) getActivity();
                        activity.ajouterScore(score);
                        txtScore.setText("");
                    }else {
                        //todo user ressource
                        Toast.makeText(getActivity(), "Veuillez saisir un score", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    //todo user ressource
                    Toast.makeText(getActivity(), "Veuillez sélectionner un nom de joueur", Toast.LENGTH_SHORT).show();
                }
            }
        });


        cmbRaison.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pointAmeliorer = cmbRaison.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        cmbJoueur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nomjoueur = cmbJoueur.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //todo make an adapter
        txtScore.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    points = Integer.parseInt(txtScore.getText().toString());
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void setWidgets(View view) {

        cmbRaison = (Spinner) view.findViewById(R.id.cmbRaisons);
        cmbJoueur = (Spinner) view.findViewById(R.id.cmbJoueur);
        btnScore = view.findViewById(R.id.btnScore);
        btnRaison = view.findViewById(R.id.btnRaison);
        txtScore = view.findViewById(R.id.txtScore);

        //1. Data
        String[] dataRaison = {"BadStorm", "Unity", "OutSkilled","Casual", "BadDrop","2v1", "Position"};

        //2. Adapter
        ArrayAdapter<String> adapteurRaison = new ArrayAdapter<String>(this.getActivity(),R.layout.support_simple_spinner_dropdown_item, dataRaison);
        ArrayAdapter<String> adapteurJoueur = new ArrayAdapter<String>(this.getActivity(),R.layout.support_simple_spinner_dropdown_item, nomJoueurs);



        //3. Ler l'adapter avec lsiting

        cmbRaison.setAdapter(adapteurRaison);
        cmbJoueur.setAdapter(adapteurJoueur);
    }
}