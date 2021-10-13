package activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import data.DbAdapter;
import fragment.ConfigFragment;
import fragment.ElementConfigFragment;
import fragment.PartieFragment;

import com.example.fortnitetool.R;

import fragment.StatsFragment;
import modele.Joueur;
import modele.Partie;
import modele.Point;
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
    private ArrayList<Joueur> joueurs;
    private ArrayList<Point> points;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setWidgets();
        dbAdapter = new DbAdapter(MainActivity.this);
        dbAdapter.ouvrirBd();
        this.parties = (ArrayList<Partie>) (Object) dbAdapter.fetchAllPersistable(new Partie());
        this.scores = (ArrayList<Score>) (Object) dbAdapter.fetchAllPersistable(new Score());
        this.joueurs = (ArrayList<Joueur>) (Object) dbAdapter.fetchAllPersistable(new Joueur());
        this.points = (ArrayList<Point>) (Object) dbAdapter.fetchAllPersistable(new Point());
        dbAdapter.fermerBd();

        boolean firstRun = false;
        if(joueurs.size()==0){
            Joueur joueur = new Joueur(this.getResources().getString(R.string.joueur));
            joueurs.add(joueur);
            dbAdapter.ouvrirBd();
            dbAdapter.insertPersistable(joueur);
            dbAdapter.fermerBd();
            firstRun = true;
        }

        if(points.size() == 0){
            Point point = new Point(this.getResources().getString(R.string.point));
            points.add(point);
            dbAdapter.ouvrirBd();
            dbAdapter.insertPersistable(point);
            dbAdapter.fermerBd();
            firstRun = true;
        }

        if(firstRun){
            alertDialogue(getString(R.string.bienvenu_message));
            tabLayout.getTabAt(2).select();
        }else{
            remplacerFragment(new PartieFragment());
        }


    }

    private void alertDialogue(String message) {

        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.message_bienvenu,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        TextView titre = new TextView(this);
        titre.setText(R.string.ramp_up);
        titre.setPadding(20,20,20,20);
        titre.setGravity(Gravity.CENTER);
        titre.setTextSize(40);

        builder.setCustomTitle(titre);




        // Set up the input
        //final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        //input.setInputType(InputType.TYPE_CLASS_TEXT);
        TextView txtMessage = dialogLayout.findViewById(R.id.txtMessage);
        txtMessage.setText(message);
        txtMessage.setPadding(20,20,20,20);
        txtMessage.setGravity(Gravity.CENTER);
        txtMessage.setTextSize(20);
        builder.setView(dialogLayout);

        ;


        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void dialogue(String message){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.message_bienvenu);
        TextView txtMessage = (TextView) dialog.findViewById(R.id.txtMessage);
        txtMessage.setText(message);
        dialog.show();

    }


    public ArrayList<Joueur> getjoueurs() {
        return joueurs;
    }

    private void setWidgets() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        fragmentContainerView = (FragmentContainerView) findViewById(R.id.fragmentContainerViewMainActivity);
        setTabLayoutListener();

    }

    private void setTabLayoutListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                fragment = null;

                switch (tab.getPosition()) {
                    case 0: {
                        fragment = new PartieFragment();
                        break;
                    }
                    case 1: {
                        fragment = new StatsFragment();
                        break;
                    }
                    case 2: {
                        fragment = new ConfigFragment();
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

    private void remplacerFragment(Fragment fragment) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerViewMainActivity, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    public void ajouterPartie(Partie partie) {
        parties.add(partie);
        dbAdapter.ouvrirBd();
        dbAdapter.insertPersistable(partie);
        dbAdapter.fermerBd();
    }

    public void ajouterScore(Score score) {
        scores.add(score);
        dbAdapter.ouvrirBd();
        dbAdapter.insertPersistable(score);
        dbAdapter.fermerBd();
    }
    public void ajouterPoint(Point point) {
        points.add(point);
        dbAdapter.ouvrirBd();
        dbAdapter.insertPersistable(point);
        dbAdapter.fermerBd();
    }
    public void ajouterJoueur(Joueur joueur) {
        joueurs.add(joueur);
        dbAdapter.ouvrirBd();
        dbAdapter.insertPersistable(joueur);
        dbAdapter.fermerBd();
    }


    //String[] reponse = {"BadStorm", "Unity", "OutSkilled","Casual", "BadDrop", "Position", "HotDrop", "Greed", "BadLoot","Bonjour mon ami comment a été ta fin de semaine"};


    public ArrayList<Joueur> getJoueurs() {
        return joueurs;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public ArrayList<Partie> getParties() {
        return parties;
    }

    public ArrayList<Score> getScores() {
        return scores;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public FragmentTransaction getFragmentTransaction() {
        return fragmentTransaction;
    }

    public void modifierPoint(Point ancienPoint,String nouveauNom){
        for (Point point : this.points){
            if (point.getNom().equals(ancienPoint.getNom())){
                point.setNom(nouveauNom);
            }
        }
        dbAdapter.ouvrirBd();
        dbAdapter.updatePoint(ancienPoint,new Point(nouveauNom));
        dbAdapter.fermerBd();
        retourElementConfig(new ElementConfigFragment(getResources().getString(R.string.points_am_liorer)));
    }

    public void modifierJoueur(Joueur ancienJoueur,String nouveauNom){
        for (Joueur joueur : this.joueurs){
            if (joueur.getNom().equals(ancienJoueur.getNom())){
                joueur.setNom(nouveauNom);
            }
        }
        dbAdapter.ouvrirBd();
        dbAdapter.updateJoueur(ancienJoueur,new Joueur(nouveauNom));
        dbAdapter.fermerBd();
        retourElementConfig(new ElementConfigFragment(getResources().getString(R.string.joueurs)));
    }

    private void retourElementConfig(Fragment fragment) {
        ConfigFragment configFragment = (ConfigFragment) this.fragment;
        configFragment.remplacerFragment(fragment);
    }
}