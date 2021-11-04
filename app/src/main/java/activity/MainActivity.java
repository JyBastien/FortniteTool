package activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import data.DbAdapter;
import fragment.ConfigFragment;
import fragment.ElementConfigFragment;
import fragment.PartieFragment;

import com.example.fortnitetool.R;

import fragment.StatsFragment;
import modele.Partie;
import modele.Point;
import modele.Stats;

import com.google.android.gms.ads.AdView;
import com.google.android.material.tabs.TabLayout;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public static final String[] POINTS_INITIAUX = {"Mal évalué la complexité","Retard d'un fournisseur","Imprévu"};
    public static final String[] NOMS_DATASET_INITIAUX = {"DataSetA","DataSetB","DataSetC","DataSetD"};

    private DbAdapter dbAdapter;
    private TabLayout tabLayout;
    private FragmentContainerView fragmentContainerView;
    private Fragment fragment = null;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ArrayList<Partie> parties;
    private ArrayList<Point> points;
    private ArrayList<String> nomsDataSets;
    private int couleurGraphique;
    private int dataSetActuel;
    private AdView pub;

    //todo clean code
    //todo ajouter login firebase
    //todo ajouter data firebase
    //todo ajouter check avant dajouter un nouveau point , le pk va faire plantert si on entre 2 fois pareil
    //test ajouter un point avec apostrphes ou caracteres illégaux
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setWidgets();

        dbAdapter.ouvrirBd();
        if(dbAdapter.firstRun()){
            alertDialogueMessageBienvenu(getString(R.string.bienvenu_message));
            dbAdapter.updateFirstRun();
            tabLayout.getTabAt(2).select();
        }else{
            remplacerFragment(new PartieFragment());
        }
        dbAdapter.fermerBd();
    }
    private void setWidgets() {
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        fragmentContainerView = (FragmentContainerView) findViewById(R.id.fragmentContainerViewMainActivity);
        setTabLayoutListener();
        dbAdapter = new DbAdapter(MainActivity.this);
        dbAdapter.ouvrirBd();
        initialiserCouleur();
        initialiserDataSet();
        initialiserPoints();
        initialiserNomDataSets();
        dbAdapter.fermerBd();
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.black)));
    }

    private void initialiserNomDataSets() {
        this.nomsDataSets = dbAdapter.fetchNomsDataSets();
    }

    private void initialiserDataSet() {
        dataSetActuel = dbAdapter.fetchDataSetActuelId();
    }

    private void statsBuilder() {
        dbAdapter.ouvrirBd();
        Partie partie;
        Random random = new Random();
        Timestamp timestamp;
        LocalDate localDate;
        Date date;
        for (int i = 0; i < 250; i++ ){
            localDate = LocalDate.of(2021, random.nextInt(12) + 1,random.nextInt(27) + 1);
            date = Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
            partie = new Partie(new Timestamp(date.getTime()),this.points.get(random.nextInt(this.points.size())).getNom());
            dbAdapter.insertPartie(partie,dataSetActuel);
        }
        dbAdapter.fermerBd();
    }

    private void alertDialogueMessageBienvenu(String message) {

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

        // Set up the buttons
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
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


    private void initialiserCouleur() {
        this.couleurGraphique = dbAdapter.fetchCouleurGraphique();
    }

    private void initialiserPoints() {

        //refreshScores();
        //this.joueurs = (ArrayList<Joueur>) (Object) dbAdapter.fetchAllPersistable(new Joueur());
        this.points = dbAdapter.fetchAllPoints(dataSetActuel);
        //statsBuilder();

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
                        fragment = new ElementConfigFragment(getResources().getString(R.string.points_am_liorer));
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
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerViewMainActivity, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    public void ajouterPartie(Partie partie) {
        dbAdapter.ouvrirBd();
        dbAdapter.insertPartie(partie,dataSetActuel);
        parties = dbAdapter.fetchAllPartieParDate(this.dataSetActuel);
        dbAdapter.fermerBd();
    }

    public void ajouterPoint(Point point) {
        points.add(point);
        dbAdapter.ouvrirBd();
        dbAdapter.insertPoint(point,dataSetActuel);
        dbAdapter.fermerBd();
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public ArrayList<Partie> getParties() {
        if (this.parties == null){
            dbAdapter.ouvrirBd();
            parties = dbAdapter.fetchAllPartieParDate(this.dataSetActuel);
            dbAdapter.fermerBd();
        }
        return parties;
    }


    public Fragment getFragment() {
        return fragment;
    }

    public void modifierPoint(Point ancienPoint,String nouveauNom){
        Point nouveauPoint = new Point(nouveauNom);
        for (Point point : this.points){
            if (point.getNom().equals(ancienPoint.getNom())){
                point.setNom(nouveauNom);
            }
        }
        dbAdapter.ouvrirBd();
        dbAdapter.updatePoint(ancienPoint,nouveauPoint,dataSetActuel);
        dbAdapter.updateParties(ancienPoint, nouveauPoint,dataSetActuel);
        refreshParties();
        dbAdapter.fermerBd();

        remplacerFragment(new ElementConfigFragment(getResources().getString(R.string.points_am_liorer)));
    }


    public void effacerPoint(String nomElement) {
        Point point;
        Boolean trouve = false;
        for(int i = 0;i < this.points.size() && !trouve ;i++){
            point = this.points.get(i);
            if(point.getNom().equals(nomElement)){
                points.remove(i);
                trouve = true;
            }
        }
        Point pointAEffacer = new Point(nomElement);
        dbAdapter.ouvrirBd();
        dbAdapter.deletePoint(pointAEffacer,this.dataSetActuel);
        dbAdapter.deletePartiesPoint(pointAEffacer,this.dataSetActuel);
        refreshParties();
        dbAdapter.fermerBd();
        remplacerFragment(new ElementConfigFragment(getResources().getString(R.string.points_am_liorer)));
    }

    private void refreshParties() {
        this.parties = dbAdapter.fetchAllPartieParDate(this.dataSetActuel);
    }

    public void clearData() {
        dbAdapter.ouvrirBd();
        dbAdapter.clearData(this.dataSetActuel);
        refreshParties();
        dbAdapter.fermerBd();
    }

    public void effacerPartie(Partie partie) {
        this.parties.remove(partie);
        dbAdapter.ouvrirBd();
        dbAdapter.deletePartie(partie,this.dataSetActuel);
        refreshParties();
        dbAdapter.fermerBd();
    }

    public ArrayList<Stats> getStatsGrouperJour() {
        dbAdapter.ouvrirBd();
        ArrayList<Partie> partiesOrderedParJour = dbAdapter.fetchAllPartieParDate(dataSetActuel);
        dbAdapter.fermerBd();
        ArrayList<Stats> stats = null;
        if (partiesOrderedParJour.size() > 0) {
            stats = Stats.getStatsParJour(partiesOrderedParJour);
        }
        return stats;
    }


    public ArrayList<Stats> getStatsGrouperSemaine() {
        dbAdapter.ouvrirBd();
        ArrayList<Partie> partiesOrderedParJour = dbAdapter.fetchAllPartieParDate(this.dataSetActuel);
        dbAdapter.fermerBd();
        ArrayList<Stats> stats = null;
        if (partiesOrderedParJour.size() > 0) {
        stats = Stats.getStatsParSemaine(partiesOrderedParJour);
        }
        return stats;
    }

    public int getCouleurGraphique() {
        return this.couleurGraphique;
    }

    public void setCouleurGraphique(int couleur) {
        this.couleurGraphique = couleur;
        dbAdapter.ouvrirBd();
        dbAdapter.updateCouleurGraphique(couleur);
        dbAdapter.fermerBd();
    }

    public ArrayList<String> getNomsDataSets() {
        return nomsDataSets;
    }

    public void setDataSet(int i) {
        dbAdapter.ouvrirBd();
        dbAdapter.updateDataSetActuel(i);
        this.parties = dbAdapter.fetchAllPartieParDate(i);
        this.points = dbAdapter.fetchAllPoints(i);
        dbAdapter.fermerBd();
        this.dataSetActuel = i;
    }

    public int getDataSetActuel() {
        return dataSetActuel;
    }

    public void modifierNomDataSet(String nouveauNom) {
        dbAdapter.ouvrirBd();
        dbAdapter.updateNomDataSet(nouveauNom,dataSetActuel);
        initialiserNomDataSets();
        dbAdapter.fermerBd();
        remplacerFragment(new ElementConfigFragment(getResources().getString(R.string.points_am_liorer)));
    }
}