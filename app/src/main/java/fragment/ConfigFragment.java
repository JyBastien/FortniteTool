package fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.fortnitetool.R;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.ArrayList;
import java.util.Arrays;

import activity.MainActivity;
import modele.Point;

/*Fragment utilisé pour modifier les préférences de l'applications*/
public class ConfigFragment extends Fragment {
    private String titre;
    private TextView txtTitre;
    private MainActivity activity;
    private View view;
    private ListView listeElementView;
    private Button btnAjouter;
    private Button btnRenommer;
    private Button btnHelp;
    private PowerSpinnerView cmbCouleur;
    private PowerSpinnerView cmbDataSet;
    private ArrayList<String> arrayCouleurs;
    private ArrayList<Integer> COULEURS = new ArrayList<>(Arrays.asList(Color.CYAN, Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW));
    public ConfigFragment(String titre) {
        this.titre = titre;
    }
    public ConfigFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_element_config, container, false);
        this.activity = (MainActivity) getActivity();
        setWidgets(view);
        setListeners();
        // Inflate the layout for this fragment
        return view;
    }
    private void setWidgets(View view) {
        arrayCouleurs = new ArrayList<String>(Arrays.asList(getString(R.string.cyan), getString(R.string.rouge), getString(R.string.bleu), getString(R.string.vert), getString(R.string.jaune)));

        this.txtTitre = view.findViewById(R.id.txtTitreConfig);
        txtTitre.setText(this.titre);
        btnAjouter = view.findViewById(R.id.btnAjouter);
        btnRenommer = view.findViewById(R.id.btnRenommer);
        btnHelp = view.findViewById(R.id.btnHelp);
        initialiserListePoints();
        initialiserCmbCouleur(view);
        initialiserCmbDataSet(view);
    }
    /*initialise le spinner qui contient les noms des dataset
    * avec les nom et les styles du dropdown menu*/
    private void initialiserCmbDataSet(View view) {
        this.cmbDataSet = view.findViewById(R.id.cmbDataSet);
        ArrayList<String> nomsDataSets = activity.getNomsDataSets();
        ArrayAdapter<String> adapteur = new ArrayAdapter<>(view.getContext(), R.layout.spinner, nomsDataSets);
        adapteur.setDropDownViewResource(R.layout.spinner_drop_down);
        cmbDataSet.setItems(nomsDataSets);
        cmbDataSet.selectItemByIndex(activity.getDataSetActuel());
    }
    /*initialise le spinner qui contient les noms des couleur graphique
     * avec les nom et les styles du dropdown menu*/
    private void initialiserCmbCouleur(View view) {
        this.cmbCouleur = view.findViewById(R.id.cmbCouleur);
        ArrayAdapter<String> adapteur = new ArrayAdapter<String>(view.getContext(), R.layout.spinner, arrayCouleurs);
        adapteur.setDropDownViewResource(R.layout.spinner_drop_down);
        cmbCouleur.setItems(arrayCouleurs);
        cmbCouleur.selectItemByIndex(COULEURS.indexOf(activity.getCouleurGraphique()));
    }
    /*initilialise la listeview de touts les points correspondant au dataset du main
    * activity*/
    public void initialiserListePoints() {
        this.listeElementView = view.findViewById(R.id.lstElementConfig);
        ArrayList<Point> listeElements;
        listeElements = activity.getPoints();
        ArrayList<String> stringArray = Point.toArrayString(listeElements);
        ArrayAdapter<String> adapteur = new ArrayAdapter<String>(this.getActivity(), R.layout.config_liste, stringArray);
        listeElementView.setAdapter(adapteur);
    }
    private void setListeners() {
        setBtnListeners();
        setListListeners();
    }
    /*ajoute les listenenr onclick des boutons de la page*/
    private void setBtnListeners() {
        btnAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogNouveauElement(view);
            }
        });
        btnRenommer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomElement = activity.getNomsDataSets().get(cmbDataSet.getSelectedIndex());
                activity.remplacerFragment(new ModifierElementFragment(ModifierElementFragment.DATASET, nomElement));
            }
        });
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.alertDialogueMessageBienvenu(view.getContext().getResources().getString(R.string.bienvenu_message));
            }
        });
    }
    /*ajoute les listeners des spinner et des listes du fragment*/
    private void setListListeners() {
        this.listeElementView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String nomElement = listeElementView.getItemAtPosition(i).toString();
                activity.remplacerFragment(new ModifierElementFragment(ConfigFragment.this.titre, nomElement));
            }
        });
        cmbCouleur.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            @Override
            public void onItemSelected(int i, @Nullable String s, int newIndex, String t1) {
                activity.setCouleurGraphique(COULEURS.get(newIndex));
            }
        });
        cmbDataSet.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            @Override
            public void onItemSelected(int i, @Nullable String s, int newIndex, String t1) {
                activity.setDataSet(newIndex);
                initialiserListePoints();
            }
        });
    }
    /*affiche un dialog pour permettre a l'utilisateur d'ajouter un points a sa liste*/
    private void dialogNouveauElement(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext(), R.style.confirmDialogueTheme);
        String titreSingulier = this.titre.equals(getResources().getString(R.string.points_am_liorer)) ? getResources().getString(R.string.point_am_liorer) : getResources().getString(R.string.joueur);
        builder.setTitle(getString(R.string.ajouterNouveau) + titreSingulier);
        // Set up the input
        EditText txtReponse = new EditText(view.getContext());
        txtReponse.setTextColor(Color.WHITE);
        builder.setView(txtReponse);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String reponse = txtReponse.getText().toString();
                if (!reponse.trim().equals("")) {
                    insertNouveauPoint(reponse);
                }
            }
        });
        builder.show();
    }
    /*inserre un nouveau point a la liste du main activity et réinitialise la liste*/
    private void insertNouveauPoint(String reponse) {
        boolean trouve = false;
        for (Point point : activity.getPoints()){
            if (point.getNom().equals(reponse)) {
                trouve = true;
            }
        }
        if (!trouve) {
            activity.ajouterPoint(new Point(reponse));
            initialiserListePoints();
        }
        else {
            Toast.makeText(activity, activity.getResources().getString(R.string.dejaDansListe), Toast.LENGTH_SHORT).show();
        }
    }

    /*permet de retirer les dropdown menu dans le cas ou l'utilisateur
    change de page avec des les avoir refermés*/
    public void dismiss(){
        this.cmbDataSet.dismiss();
        this.cmbCouleur.dismiss();
    }
}


