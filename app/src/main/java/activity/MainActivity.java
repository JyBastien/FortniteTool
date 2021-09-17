package activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import data.DbAdapter;
import fragment.PartieFragment;
import com.example.fortnitetool.R;
import fragment.StatsFragment;
import modele.Partie;
import modele.Score;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private DbAdapter dbAdapter;
    private TabLayout tabLayout;
    private FragmentContainerView fragmentContainerView;
    private Fragment fragment = null;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ArrayList<Partie> parties;
    private ArrayList<Score> scores;
    private String[] nomJoueurs;

    //rendu a customiser les item des spinners comme le prof avait faoit pour son list view


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setWidgets();
        dbAdapter = new DbAdapter(MainActivity.this);
        dbAdapter.ouvrirBd();
        this.parties = dbAdapter.fetchParties();
        this.scores = dbAdapter.fetchScores();
        dbAdapter.fermerBd();

        this.nomJoueurs = fetchNomJoueurs();
    }

    private String[] fetchNomJoueurs() {
        nomJoueurs = new String[]{"CptSemiColon", "SpecktR"};
        return nomJoueurs;
    }


    public String[] getNomJoueurs() {
        return nomJoueurs;
    }

    private void setWidgets() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        fragmentContainerView = (FragmentContainerView) findViewById(R.id.fragmentContainerView);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fragment = null;

                switch (tab.getPosition()) {
                    case 0:{
                        fragment = new PartieFragment();
                        break;
                    }
                    case 1: {
                        fragment = new StatsFragment(parties,scores,nomJoueurs);
                        break;
                    }
                }

                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerView,fragment);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void ajouterPartie(Partie partie){
        parties.add(partie);
        dbAdapter.ouvrirBd();
        dbAdapter.ajouterPartie(partie);
        dbAdapter.fermerBd();
    }
    public void ajouterScore(Score score){
        scores.add(score);
        dbAdapter.ouvrirBd();
        dbAdapter.ajouterScore(score);
        dbAdapter.fermerBd();
    }


    public String[] getPointsAmeliorer() {
        String[] reponse = {"BadStorm", "Unity", "OutSkilled","Casual", "BadDrop", "Position", "HotDrop", "Greed", "BadLoot"};
        return reponse;
    }
}