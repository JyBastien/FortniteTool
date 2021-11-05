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
import android.widget.Spinner;
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
import utils.AbstractOnItemListener;

public class ElementConfigFragment extends Fragment {

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


    public ElementConfigFragment(String titre) {
        this.titre = titre;
    }

    public ElementConfigFragment() {
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

    private void initialiserCmbDataSet(View view) {
        this.cmbDataSet = view.findViewById(R.id.cmbDataSet);
        ArrayList<String> nomsDataSets = activity.getNomsDataSets();
        ArrayAdapter<String> adapteur = new ArrayAdapter<>(view.getContext(), R.layout.spinner, nomsDataSets);
        adapteur.setDropDownViewResource(R.layout.spinner_drop_down);
        cmbDataSet.setItems(nomsDataSets);
        cmbDataSet.selectItemByIndex(activity.getDataSetActuel());
    }

    private void initialiserCmbCouleur(View view) {
        this.cmbCouleur = view.findViewById(R.id.cmbCouleur);
        ArrayAdapter<String> adapteur = new ArrayAdapter<String>(view.getContext(), R.layout.spinner, arrayCouleurs);
        adapteur.setDropDownViewResource(R.layout.spinner_drop_down);
        cmbCouleur.setItems(arrayCouleurs);
        cmbCouleur.selectItemByIndex(COULEURS.indexOf(activity.getCouleurGraphique()));
    }

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

    private void setBtnListeners() {
        btnAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertNouveauElement(view);
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

    private void setListListeners() {
        this.listeElementView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String nomElement = listeElementView.getItemAtPosition(i).toString();
                activity.remplacerFragment(new ModifierElementFragment(ElementConfigFragment.this.titre, nomElement));
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



//        cmbDataSet.setOnItemSelectedListener(new AbstractOnItemListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                activity.setDataSet(i);
//                initialiserListePoints();
//            }
//        });
    }


    private void alertNouveauElement(View view) {
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
                    insertNouveauElement(reponse);
                }
            }
        });

        builder.show();
    }

    private void insertNouveauElement(String reponse) {
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

    public void dismiss(){
        this.cmbDataSet.dismiss();
        this.cmbCouleur.dismiss();
    }


}


