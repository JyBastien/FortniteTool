package com.example.fortnitetool;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
        setWidgets(view);


        // Inflate the layout for this fragment
        return view;
    }



    private void setWidgets(View view) {
        Spinner raison;
        Spinner joueur;

        raison = (Spinner) view.findViewById(R.id.cmbRaisons);
        joueur = (Spinner) view.findViewById(R.id.cmbJoueur);

        //1. Data
        String[] dataRaison = {"BadStorm", "Unity", "OutSkilled","Casual", "BadDrop","2v1", "Position"};
        String[] dataJoueur = {"CptSemiColon", "SpecktR"};

        //2. Adapter
        ArrayAdapter<String> adapteurRaison = new ArrayAdapter<String>(this.getActivity(),R.layout.support_simple_spinner_dropdown_item, dataRaison);
        ArrayAdapter<String> adapteurJoueur = new ArrayAdapter<String>(this.getActivity(),R.layout.support_simple_spinner_dropdown_item, dataJoueur);



        //3. Ler l'adapter avec lsiting

        raison.setAdapter(adapteurRaison);
        joueur.setAdapter(adapteurJoueur);
    }
}