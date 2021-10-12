package fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fortnitetool.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InsertNouveauElement#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InsertNouveauElement extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String nomElement;

    public InsertNouveauElement() {
    }

    public InsertNouveauElement(String nomElement) {
        this.nomElement = nomElement;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InsertNouveauElement.
     */
    // TODO: Rename and change types and number of parameters
    public static InsertNouveauElement newInstance(String param1, String param2) {
        InsertNouveauElement fragment = new InsertNouveauElement("Pour commencer, vous devez entrez un premier point à améliorer, vous pourrez en ajouter, modifier ou supprimer plusieurs dans l'oglet Config");
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_insert_nouveau_element, container, false);
    }
}