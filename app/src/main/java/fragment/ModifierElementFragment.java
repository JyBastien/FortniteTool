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
import android.widget.Toast;

import com.example.fortnitetool.R;

import activity.MainActivity;
import modele.Joueur;
import modele.Point;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ModifierElementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModifierElementFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String type;
    private String nomElement;
    private TextView txtNomOriginal;
    private EditText txtNouveauNom;
    private Button btnEnregistrer;
    private Button btnEffacer;
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
    // TODO: Rename and change types and number of parameters
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

    private void setListeners() {
        btnEnregistrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nouveauNom = txtNouveauNom.getText().toString().trim();
                if (!nouveauNom.equals("")) {
                    if (type.equals(getResources().getString(R.string.points_am_liorer))) {
                        Point ancienPoint = new Point(nomElement);
                        activity.modifierPoint(ancienPoint, nouveauNom);
                    } else {
                        Joueur ancienJoueur = new Joueur(nomElement);
                        activity.modifierJoueur(ancienJoueur, nouveauNom);
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
    }

    private void dialogueConfirmation() {
        AlertDialog.Builder confirmDeleteDialog = new AlertDialog.Builder(this.getContext());
        confirmDeleteDialog.setTitle("Effacer");
        confirmDeleteDialog.setMessage("Voulez-vous vraiment effacer " + nomElement + "?\nVous effacerez toutes les données associés également");
        confirmDeleteDialog.setPositiveButton("Effacer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (type.equals(getResources().getString(R.string.points_am_liorer))) {
                    if(activity.getPoints().size()>1) {
                        activity.effacerPoint(nomElement);
                    }else{
                        alertDialogueMessage("Impossible d'effacer le dernier " + getResources().getString(R.string.point_am_liorer));
                    }
                } else {
                    if(activity.getJoueurs().size()>1) {
                        activity.effacerJoueur(nomElement);
                    }else{
                        alertDialogueMessage("Impossible d'effacer le dernier " + getResources().getString(R.string.joueur));
                    }
                }
                dialogInterface.dismiss();
            }
        });
        confirmDeleteDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        confirmDeleteDialog.show();
    }

    private void alertDialogueMessage(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Erreur");
        builder.setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void setValues() {
        this.txtNomOriginal.setText(this.nomElement);
        txtNouveauNom.setText(this.nomElement);
    }

    private void setWidgets(View view) {
        this.txtNomOriginal = view.findViewById(R.id.txtNomElementOriginal);
        this.txtNouveauNom = view.findViewById(R.id.txtNouveauNomElement);
        this.btnEnregistrer = view.findViewById(R.id.btnEnregistrerMods);
        this.btnEffacer = view.findViewById(R.id.btnEffacerMods);
        this.activity = (MainActivity) this.getActivity();
    }
}