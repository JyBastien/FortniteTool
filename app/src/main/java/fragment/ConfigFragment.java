package fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fortnitetool.R;
import com.google.android.material.tabs.TabLayout;

import activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ConfigFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConfigFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TabLayout tabLayout;
    private MainActivity mainActivity;
    private FragmentContainerView fragmentContainerView;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;


    public ConfigFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConfigFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ConfigFragment newInstance(String param1, String param2) {
        ConfigFragment fragment = new ConfigFragment();
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
        View view = inflater.inflate(R.layout.fragment_config, container, false);
        setWidgets(view);
        setListeners();
        return view;
    }

    private void setWidgets(View view) {
        //todo refactor with main method
        this.mainActivity = (MainActivity) this.getActivity();
        this.fragment = new ElementConfigFragment(mainActivity.getResources().getString(R.string.points_am_liorer));
        remplacerFragment(this.fragment);
        tabLayout = view.findViewById(R.id.tabLayoutConfig);
        fragmentContainerView = view.findViewById(R.id.fragmentContainerViewElementConfig);
    }

    private void setListeners() {
        setTablLayoutListener();
    }



    private void setTablLayoutListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fragment = null;

                switch (tab.getPosition()) {
                    case 0: {
                        fragment = new ElementConfigFragment(mainActivity.getResources().getString(R.string.points_am_liorer));
                        break;
                    }
                    case 1: {
                        fragment = new ElementConfigFragment(mainActivity.getResources().getString(R.string.joueurs));
                        break;
                    }
                }
                remplacerFragment(fragment);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void remplacerFragment(Fragment fragment) {
        FragmentManager fm = this.getChildFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerViewElementConfig, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }


}