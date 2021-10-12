package fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        setBtnInsertListener();
    }

    private void setBtnInsertListener() {
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
        //final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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
    }

    private void setWidgets(View view) {
        this.txtTitre = view.findViewById(R.id.txtTitreConfig);
        txtTitre.setText(this.titre);
        btnAjouter = view.findViewById(R.id.btnAjouter);
        configurerListe(view);

    }

    private void configurerListe(View view) {
        this.listeElementView = view.findViewById(R.id.lstElementConfig);
        ArrayList<Persistable> listeElements;
        if (this.titre.equals(activity.getResources().getString(R.string.points_am_liorer))){
            listeElements = (ArrayList<Persistable>) (Object) activity.getPoints();
        }else {
            listeElements = (ArrayList<Persistable>) (Object)activity.getJoueurs();
        }

        ArrayList<String> stringArray = Persistable.toArrayString(listeElements);
        ArrayAdapter<String> adapteur = new ArrayAdapter<String>(this.getActivity(), R.layout.support_simple_spinner_dropdown_item, stringArray);
        listeElementView.setAdapter(adapteur);
    }

}


