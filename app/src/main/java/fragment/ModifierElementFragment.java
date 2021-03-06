package fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fortnitetool.R;

import activity.MainActivity;
import modele.Point;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModifierElementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
/*Permet à l'utlisateur de modifier un nom d'élément fournit en paramètre*/
public class ModifierElementFragment extends Fragment {

    public static final String DATASET = "DataSet";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private String type;
    private String nomElement;
    private TextView txtNomOriginal;
    private EditText txtNouveauNom;
    private Button btnEnregistrer;
    private Button btnEffacer;
    private Button btnRetour;
    private MainActivity activity;

    public ModifierElementFragment() {
        // Required empty public constructor
    }
    public ModifierElementFragment(String type, String nomElement) {
        this.type = type;
        this.nomElement = nomElement;
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ModifierElementFragment.
     */
    public static ModifierElementFragment newInstance(String param1, String param2) {
        ModifierElementFragment fragment = new ModifierElementFragment();
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
        View view = inflater.inflate(R.layout.fragment_modifier_element, container, false);
        setWidgets(view);
        setValues();
        setListeners();
        return view;
    }
    /*associe les attributes de la classe aux objets graphiques du layout*/
    private void setWidgets(View view) {
        this.txtNomOriginal = view.findViewById(R.id.txtNomElementOriginal);
        this.txtNouveauNom = view.findViewById(R.id.txtNouveauNomElement);
        this.btnEnregistrer = view.findViewById(R.id.btnEnregistrerMods);
        this.btnEffacer = view.findViewById(R.id.btnEffacerMods);
        if (this.type.equals(DATASET)) {
            this.btnEffacer.setVisibility(View.GONE);
        }
        this.btnRetour = view.findViewById(R.id.btnRetour);
        this.activity = (MainActivity) this.getActivity();
    }
    /*attache les lsitenenrts aux boutons du layout*/
    private void setListeners() {
        btnEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nouveauNom = txtNouveauNom.getText().toString().trim();
                if (!nouveauNom.equals("")) {
                    if (ModifierElementFragment.this.type.equals(DATASET)){
                        activity.modifierNomDataSet(nouveauNom);
                    }else {
                        Point ancienPoint = new Point(nomElement);
                        activity.modifierPoint(ancienPoint, nouveauNom);
                    }
                }
            }
        });
        btnEffacer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogueConfirmation();
            }
        });
        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.remplacerFragment(new ConfigFragment(getResources().getString(R.string.points_am_liorer)));
            }
        });
    }
    /*affiche une demande confirmation à l'utilisateur avant d'effacer le point sélectionné*/
    private void dialogueConfirmation() {
        AlertDialog.Builder confirmDeleteDialog = new AlertDialog.Builder(this.getContext());
        confirmDeleteDialog.setTitle(R.string.effacer);
        confirmDeleteDialog.setMessage(getString(R.string.vraimentEffacer) + nomElement + getString(R.string.effacerAssocieEgalement));
        confirmDeleteDialog.setPositiveButton(R.string.effacer, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (activity.getPoints().size() > 1) {
                    activity.effacerPoint(nomElement);
                } else {
                    alertDialogueMessage(getString(R.string.impossibleEffacerDernier) + getResources().getString(R.string.point_am_liorer));
                }
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
    /*affiche un message d'erreur fournit en paramètres à l'utilisateur*/
    private void alertDialogueMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(R.string.erreur);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
    /*initialise les text des textviews*/
    private void setValues() {
        this.txtNomOriginal.setText(R.string.renommer);
        txtNouveauNom.setText(this.nomElement);
    }
}