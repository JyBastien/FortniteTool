package fragment;

import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.fortnitetool.R;

import java.util.ArrayList;

import activity.MainActivity;
import modele.Joueur;
import modele.Point;
import utils.Persistable;

public class ElementConfigFragment extends Fragment {

    private String titre;
    private TextView txtTitre;
    private MainActivity activity;
    private View view;
    private ListView listeElementView;
    private Button btnAjouter;



    public ElementConfigFragment(String titre) {
        this.titre = titre;
    }

    public ElementConfigFragment(){}



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

    private void setListeners() {
        setBtnListeners();
        setListViewListener();
    }

    private void setListViewListener() {
        this.listeElementView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                modifierElement(listeElementView.getItemAtPosition(i).toString());
            }
        });
    }

    private void modifierElement(String nomElement) {
        //ConfigFragment configFragment = (ConfigFragment) activity.getFragment();
        activity.remplacerFragment(new ModifierElementFragment(this.titre, nomElement));
    }

    private void setBtnListeners() {
        btnAjouter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertNouveauElement(view);
            }
        });

    }


    private void alertNouveauElement(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        String titreSingulier = this.titre.equals(getResources().getString(R.string.points_am_liorer))? getResources().getString(R.string.point_am_liorer) : getResources().getString(R.string.joueur);
        builder.setTitle(getString(R.string.ajouterNouveau) + titreSingulier);
        // Set up the input
        EditText txtReponse = new EditText(view.getContext());
        builder.setView(txtReponse);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String reponse = txtReponse.getText().toString();
                if (!reponse.trim().equals("")){
                    insertNouveauElement(reponse);
                }
            }
        });

        builder.show();
    }

    private void insertNouveauElement(String reponse) {
        if (this.titre.equals(getResources().getString(R.string.points_am_liorer))){
            activity.ajouterPoint(new Point(reponse));
        }else {
            activity.ajouterJoueur(new Joueur(reponse));
        }
        configurerListe();
    }

    private void setWidgets(View view) {
        this.txtTitre = view.findViewById(R.id.txtTitreConfig);
        txtTitre.setText(this.titre);
        btnAjouter = view.findViewById(R.id.btnAjouter);
        this.listeElementView = view.findViewById(R.id.lstElementConfig);
        configurerListe();
    }

    public void configurerListe() {
        ArrayList<Persistable> listeElements;
        if (this.titre.equals(activity.getResources().getString(R.string.points_am_liorer))){
            listeElements = (ArrayList<Persistable>) (Object) activity.getPoints();
        }else {
            listeElements = (ArrayList<Persistable>) (Object)activity.getJoueurs();
        }

        ArrayList<String> stringArray = Persistable.toArrayString(listeElements);
        ArrayAdapter<String> adapteur = new ArrayAdapter<String>(this.getActivity(), R.layout.config_liste, stringArray);
        listeElementView.setAdapter(adapteur);
    }

}


