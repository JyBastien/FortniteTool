package fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fortnitetool.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import activity.MainActivity;
import modele.Entree;
import modele.Partie;
import modele.Stats;

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
    private MainActivity mainActivity;
    private BarChart chart;
    private TabLayout tabLayout;
    private Button btnClearData;
    private TextView txtChartDescription;


    public StatsFragment() {
        // Required empty public constructor
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        setWidgets(view);
        setListeners(view);
        remplirStatsParties();
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setWidgets(View view) {
        txtChartDescription = view.findViewById(R.id.txtChartDescription);
        //associer l'activity main a la propriété activity
        this.mainActivity = (MainActivity) this.getActivity();
        btnClearData = view.findViewById(R.id.btnClear);
        //monter la liste des parties enregistrées
        listeParties = view.findViewById(R.id.lstParties);
        listeParties.setNestedScrollingEnabled(true);
        ArrayAdapter<String> adapteur;

        //attacher la liste de joueur
        //cmbJoueurs = view.findViewById(R.id.cmbJoueurs);
       // ArrayList<String> listeJoueurs = Persistable.toArrayString((ArrayList<Persistable>) (Object) mainActivity.getJoueurs());
      //  adapteur = new ArrayAdapter<String>(mainActivity, R.layout.support_simple_spinner_dropdown_item, listeJoueurs);

        //     cmbJoueurs.setAdapter(adapteur);
        //     String nomJoueur = cmbJoueurs.getSelectedItem().toString();

        //monter la liste des scores enregistrées selon le nom de joueur
        //listeScores = view.findViewById(R.id.lstScores);
        //   remplirStatsJoueur(nomJoueur);

        chart = view.findViewById(R.id.chart);
        ArrayList<Partie> parties = mainActivity.getParties();
        if (parties.size() > 0){
            showBArChartTout(parties);
        }else {
            btnClearData.setEnabled(false);
            Toast.makeText(mainActivity, R.string.aucuneDonnee, Toast.LENGTH_SHORT).show();
        }
        tabLayout = view.findViewById(R.id.tabLayoutGraphs);
    }


    private void remplirObjetsGraphique(ArrayList<Entree> entrees, ArrayList<BarEntry> barEntries, ArrayList<String> points, int qteTotal) {
        String texte;
        Entree entree;
        for (int i = 0; i < entrees.size() && i < 6; i++) {
            entree = entrees.get(i);
            float ratio = (float) entree.getQte() / qteTotal * 100;
            barEntries.add(new BarEntry(i, ratio));
            texte = entree.getTexte().length() > 10 ? entree.getTexte().substring(0, 10) : entree.getTexte();
            points.add(texte);
        }
    }

    private ArrayList<Entree> listEntreeBuilderFromStatsBuilder(HashMap<String, Integer> statsBuilder) {
        ArrayList<Entree> entrees = new ArrayList<>(statsBuilder.size());
        for (Map.Entry<String, Integer> stats : statsBuilder.entrySet()) {
            entrees.add(new Entree(stats.getKey(), stats.getValue()));
        }
        return entrees;
    }

    private HashMap<String, Integer> statsBuilderDeParties(ArrayList<Partie> parties) {
        HashMap<String, Integer> statsBuilder = new HashMap<>();
        String pointAmeliorer;
        int qte;
        for (Partie partie : parties) {
            pointAmeliorer = partie.getPointAmeliorer();
            if (statsBuilder.containsKey(pointAmeliorer)) {
                qte = statsBuilder.get(pointAmeliorer) + 1;
            } else {
                qte = 1;
            }
            statsBuilder.put(pointAmeliorer, qte);
        }
        return statsBuilder;
    }
    private void showBArChartTout(ArrayList<Partie> parties) {
        HashMap<String, Integer> statsBuilder = statsBuilderDeParties(parties);
        ArrayList<Entree> entrees = listEntreeBuilderFromStatsBuilder(statsBuilder);
        Collections.sort(entrees);
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> points = new ArrayList<>();
        remplirObjetsGraphique(entrees, barEntries, points, parties.size());
        BarDataSet barDataSet = new BarDataSet(barEntries, "%");
        barDataSet.setValueTextSize(12f);
        setCharXAxis(points);
        chart.setData(new BarData(barDataSet));
        setCharDescription(getString(R.string.occurencesDes) + " " + getString(R.string.points_am_liorer));
        setChartAttributes();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showStatsInBarChart(String description, ArrayList<Stats> stats) {

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> points = new ArrayList<>();
        String nomPoint;
        for (int i = 0; i < stats.size(); i++) {
            barEntries.add(new BarEntry(i, (float) stats.get(i).getRatio() * 100));
            nomPoint = stats.get(i).getNomPoint();
            nomPoint = nomPoint.length() > 6 ? nomPoint.substring(0, 6) : nomPoint;
            points.add(nomPoint);
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, "%");
        barDataSet.setValueTextSize(12f);
        setCharXAxis(points);
        BarData theData = new BarData(barDataSet);
        setCharDescription(description);
        chart.setData(theData);
        setChartAttributes();
    }

    private void setCharXAxis(ArrayList<String> points) {
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(points));
        chart.getXAxis().setLabelCount(points.size(),points.size() == 1);
        chart.getXAxis().setCenterAxisLabels(points.size() == 1);
    }

    private void setChartAttributes() {
        int color = Color.WHITE;
        chart.getLegend().setTextColor(color);
        chart.getXAxis().setTextColor(color);
        chart.getAxisLeft().setTextColor(color);
        chart.getBarData().setValueTextColor(color);
        chart.getLegend().setTextColor(color);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.animateY(1500);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.invalidate();
    }

    private void setCharDescription(String texte) {
    txtChartDescription.setText(texte);
    chart.getDescription().setText("");
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void remplirStatsParties() {
        ArrayList<Partie> parties = mainActivity.getParties();
        btnClearData.setEnabled(parties.size() > 0);
        ArrayAdapter<String> adapteur = null;
        ArrayList<String> listeStringFormatParties = null;

            //          if (parties.size() < 5) {
            listeStringFormatParties = partiesToStringEnLigne(parties);
            //        } else //if (parties.size() < 10)
            //       {
            //           listeStringFormatParties = partiesToStringJour();
            //       }
            adapteur = new ArrayAdapter<String>(this.getActivity(), R.layout.session_liste, listeStringFormatParties);
            listeParties.setAdapter(adapteur);

    }
    private void setListeners(View view) {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int i = tab.getPosition();
                afficherStatsSelonSelectedTab(i);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        btnClearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogueEffacerConfirmation();
            }
        });
//        cmbJoueurs.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String nomJoueur = cmbJoueurs.getSelectedItem().toString();
//                remplirStatsJoueur(nomJoueur);
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
        listeParties.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayList<Partie> parties = mainActivity.getParties();
                dialogueEffacerConfirmation(parties.get(i));
                return false;
            }
        });
//        listeScores.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                ArrayList<Score> scores = mainActivity.getScores();
//                dialogueConfirmation(scores.get(i));
//                return false;
//            }
//        });
    }

    private void afficherStatsSelonSelectedTab(int i) {
        switch (i) {
            case 0: {
                ArrayList<Partie> parties = mainActivity.getParties();
                if (parties.size() > 0){
                    showBArChartTout(parties);
                }else {
                    Toast.makeText(mainActivity, R.string.aucuneDonnee, Toast.LENGTH_SHORT).show();
                    chart.clear();
                }

                break;
            }
            case 1: {
                ArrayList<Stats> stats = mainActivity.getStatsGrouperJour();
                if (stats != null && stats.size() > 0) {
                    showStatsInBarChart(getString(R.string.ratioDerniersJours), stats);
                }else {
                    Toast.makeText(mainActivity, R.string.aucuneDonnee, Toast.LENGTH_SHORT).show();
                    chart.clear();
                }
                break;
            }
            case 2: {
                ArrayList<Stats> stats = mainActivity.getStatsGrouperSemaine();
                if (stats != null && stats.size() > 0) {
                    showStatsInBarChart(getString(R.string.rationPointsSemaine),stats);
                }else {
                    Toast.makeText(mainActivity, R.string.aucuneDonnee, Toast.LENGTH_SHORT).show();
                    chart.clear();
                }

                break;
            }
        }
    }


    private void dialogueEffacerConfirmation(Partie partie) {
        AlertDialog.Builder confirmDeleteDialog = new AlertDialog.Builder(this.getContext(),R.style.confirmDialogueTheme);
        confirmDeleteDialog.setTitle(R.string.effacer);
        confirmDeleteDialog.setMessage(getString(R.string.voulezEffacerPartie) + " " + partie.getTemps().toString().substring(0, partie.getTemps().toString().length() - 7) + "?");
        confirmDeleteDialog.setPositiveButton(R.string.effacer, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mainActivity.effacerPartie(partie);
                remplirStatsParties();
                chart.clear();
                int tabIndex = tabLayout.getSelectedTabPosition();
                afficherStatsSelonSelectedTab(tabIndex);
                dialogInterface.dismiss();
            }
        });
        confirmDeleteDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        confirmDeleteDialog.show();
    }
    private void dialogueEffacerConfirmation() {
        AlertDialog.Builder confirmDeleteDialog = new AlertDialog.Builder(this.getContext(),R.style.confirmDialogueTheme);
        confirmDeleteDialog.setTitle(R.string.effacer);
        confirmDeleteDialog.setMessage(R.string.effacerStatistiques);
        confirmDeleteDialog.setPositiveButton(R.string.effacer, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mainActivity.clearData();
                remplirStatsParties();
                chart.clear();
                dialogInterface.dismiss();
            }
        });
        confirmDeleteDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        confirmDeleteDialog.show();
    }

//    private void dialogueConfirmation(Score score) {
//        AlertDialog.Builder confirmDeleteDialog = new AlertDialog.Builder(this.getContext());
//        confirmDeleteDialog.setTitle(R.string.effacer);
//        confirmDeleteDialog.setMessage("Voulez-vous vraiment Effacer le score de " + score.getNomJoueur() + " inscrit le " + score.getTemps().toString().substring(0, score.getTemps().toString().length() - 7) + "?");
//        confirmDeleteDialog.setPositiveButton("Effacer", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                mainActivity.effacerScore(score);
//                String nomJoueur = cmbJoueurs.getSelectedItem().toString();
//                remplirStatsJoueur(nomJoueur);
//                dialogInterface.dismiss();
//            }
//        });
//        confirmDeleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
//            }
//        });
//        confirmDeleteDialog.show();
//    }

//    private void remplirStatsJoueur(String nomJoueur) {
//
//        ArrayAdapter<String> adapteur;
//        ArrayList<String> listeStringFormatScores = scoresArrayStringFactory(nomJoueur);
//        adapteur = new ArrayAdapter<String>(this.getActivity(), R.layout.support_simple_spinner_dropdown_item, listeStringFormatScores);
//        listeScores.setAdapter(adapteur);
//    }

//    private ArrayList<String> scoresArrayStringFactory(String nomJoueur) {
//        ArrayList<String> scoresString = new ArrayList(0);
//        ArrayList<Score> scores = mainActivity.getScores();
//        for (Score score : scores) {
//            if (score.getNomJoueur().equals(nomJoueur)) {
//                scoresString.add(score.getNomJoueur() + " " + new Date(score.getTemps().getTime()).toString().substring(4, 19) + " " + getString(R.string.score) + ": " + score.getScore());
//            }
//        }
//
//        return scoresString;
//    }

    private ArrayList<String> partiesToStringEnLigne(ArrayList<Partie> parties) {
        ArrayList<String> partiesString = new ArrayList(0);
        for (Partie partie : parties) {
            partiesString.add(new Date(partie.getTemps().getTime()).toString().substring(4, 19) + " " + getString(R.string.point) + ": " + partie.getPointAmeliorer());
        }
        return partiesString;
    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private ArrayList<String> partiesToStringJour() {
//        ArrayList<String> partiesString = new ArrayList(0);
//        ArrayList<Stats> listeStats = mainActivity.getStatsGrouperJour();
//        int i = 1;
//        for (Stats stats : listeStats) {
//            partiesString.add("Jour " + i++ + " " + stats.getNomPoint() + " :" + stats.getRatio() * 100 + "%");
//        }
//        return partiesString;
//    }


}