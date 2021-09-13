package fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.fortnitetool.R;

import java.util.ArrayList;
import java.util.Date;

import modele.Partie;
import modele.Score;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ListView listeParties;
    private ListView listeScores;
    private Spinner cmbJoueurs;
    private String mParam1;
    private String mParam2;
    private String[] nomJoueurs;
    private ArrayList<Partie> parties;
    private ArrayList<Score> scores;

    public StatsFragment() {
        // Required empty public constructor
    }

    public StatsFragment(ArrayList<Partie> parties, ArrayList<Score> scores, String[] nomJoueurs) {
        this.parties = parties;
        this.scores = scores;
        this.nomJoueurs = nomJoueurs;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StatsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StatsFragment newInstance(String param1, String param2) {
        StatsFragment fragment = new StatsFragment();
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

    private void setWidgets(View view) {

        //monter la liste des parties enregistrées
        listeParties = view.findViewById(R.id.lstParties);
        ArrayList<String> listeStringFormatParties = partiesArrayStringFactory();
        ArrayAdapter<String> adapteurParties = new ArrayAdapter<String>(this.getActivity(),R.layout.support_simple_spinner_dropdown_item, listeStringFormatParties);
        listeParties.setAdapter(adapteurParties);

        //monter la liste des scores enregistrées selon le nom de joueur
        listeScores = view.findViewById(R.id.lstScores);
        ArrayList<String> listeStringFormatScores = scoresArrayStringFactory();
        ArrayAdapter<String> adapteurScores = new ArrayAdapter<String>(this.getActivity(),R.layout.support_simple_spinner_dropdown_item, listeStringFormatScores);
        listeScores.setAdapter(adapteurScores);
    }

    private ArrayList<String> scoresArrayStringFactory() {
        ArrayList<String> scoresString = new ArrayList(0);

        for (Score score : scores){
            scoresString.add(score.getJoueur() + " " + new Date(score.getTemps().getTime()).toString().substring(4,19) + " Score: " + score.getScore());
        }

        return scoresString;
    }

    private ArrayList<String> partiesArrayStringFactory() {

        ArrayList<String> partiesString = new ArrayList(0);

        for (Partie partie : parties){
            partiesString.add(new Date(partie.getTemps().getTime()).toString().substring(4,19) + " Point: " + partie.getPointAmeliorer());
        }
        return partiesString;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container,false);
        setWidgets(view);

        // Inflate the layout for this fragment
        return view;
    }
}