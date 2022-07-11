package activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import data.DbAdapter;
import fragment.ConfigFragment;
import fragment.PartieFragment;

import com.example.fortnitetool.R;

import fragment.StatsFragment;
import modele.Partie;
import modele.Point;
import modele.Stats;
import utils.AbstractOnTabSelectedListener;

import com.google.android.material.tabs.TabLayout;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
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

    //todo clean code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setWidgets();
        dbAdapter.ouvrirBd();
        if(dbAdapter.firstRun()){
            //on affiche le message d'explication de l'application lors de la première exécution
            alertDialogueMessageBienvenu(getString(R.string.bienvenu_message));
            dbAdapter.updateFirstRun();
        }
        dbAdapter.fermerBd();
        //on place le un fragment partie dans le conteneur
        remplacerFragment(new PartieFragment());
    }
    //on instancie les objets qui seront utilisé par le MainActivity
    private void setWidgets() {
        //tableau de navigation
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        //conteneur de fragment single page
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
    /*récupère les noms des datasets dans la bd et initialise l'attribut nomsData*/
    private void initialiserNomDataSets() {
        this.nomsDataSets = dbAdapter.fetchNomsDataSets();
    }
    private void initialiserDataSet() {
        dataSetActuel = dbAdapter.fetchDataSetActuel();
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
    /*affiche le message fournit en paramètre à l'utlisateur*/
    public void alertDialogueMessageBienvenu(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.message_bienvenu,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView titre = new TextView(this);
        titre.setText(R.string.ramp_up);
        titre.setPadding(20,20,20,20);
        titre.setGravity(Gravity.CENTER);
        titre.setTextSize(40);
        builder.setCustomTitle(titre);
        TextView txtMessage = dialogLayout.findViewById(R.id.txtMessage);
        txtMessage.setText(message);
        txtMessage.setPadding(20,20,20,20);
        txtMessage.setGravity(Gravity.CENTER);
        txtMessage.setTextSize(20);
        txtMessage.setMovementMethod(new ScrollingMovementMethod());
        builder.setView(dialogLayout);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }
    /*On va lire la couleur personnalisée de l'utilisateur dans la BD
    * et l'affecté à l'attribut de MainActivity*/
    private void initialiserCouleur() {
        this.couleurGraphique = dbAdapter.fetchCouleurGraphique();
    }
    /*On va lire tous les points du dataset de l'utilisateur dans la BD
     * et l'affecté à l'attribut de MainActivity */
    private void initialiserPoints() {
        this.points = dbAdapter.fetchAllPoints(dataSetActuel);
        //statsBuilder();
    }
    /*On va ajouter un listener sur le tableau pour afficher le fragment correspondant au choix de
    * l'utilisateur soit Partie Stats Ou Config fragment*/
    private void setTabLayoutListener() {
        tabLayout.addOnTabSelectedListener(new AbstractOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment nouveauFragment = null;
                switch (tab.getPosition()) {
                    case 0: {
                        nouveauFragment = new PartieFragment();
                        break;
                    }
                    case 1: {
                        nouveauFragment = new StatsFragment();
                        break;
                    }
                    case 2: {
                        nouveauFragment = new ConfigFragment(getResources().getString(R.string.points_am_liorer));
                        break;
                    }
                }
                remplacerFragment(nouveauFragment);
            }
        });
    }
    /*affiche le fragment fournit en paramètre dans le conteneur de MainActivity*/
    public void remplacerFragment(Fragment fragment) {
        if (this.fragment != null){
            if (this.fragment instanceof PartieFragment){
                PartieFragment partieFragment = (PartieFragment) this.fragment;
                partieFragment.dismiss();
            }else if (this.fragment instanceof ConfigFragment){
                ConfigFragment elementConfigFragment = (ConfigFragment) this.fragment;
                elementConfigFragment.dismiss();
            }
        }
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerViewMainActivity, fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
        this.fragment = fragment;
    }
    /*Ajoute la partie fournie en paramètre dans la BD
    * Actualise ensuite la liste de parties du MainActivity
    * Pour garder une liste synchronisée et ordonnée avec la BD*/
    public void ajouterPartie(Partie partie) {
        dbAdapter.ouvrirBd();
        dbAdapter.insertPartie(partie,dataSetActuel);
        parties = dbAdapter.fetchAllPartieParDate(this.dataSetActuel);
        dbAdapter.fermerBd();
    }
    /*Ajoute le point fourni en paramètre dans la BD
     * Actualise ensuite la liste de points du MainActivity*/
    public void ajouterPoint(Point point) {
        points.add(point);
        dbAdapter.ouvrirBd();
        dbAdapter.insertPoint(point,dataSetActuel);
        dbAdapter.fermerBd();
    }
    public ArrayList<Point> getPoints() {
        return points;
    }
    /*Retournera la listes de toutes les parties du dataset
    * Si elle est vide, on va la lire dabord*/
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
    /*Prends en paramètre un ancien point ainsi que le nouveau Nom choisi
    * par l'utilisateur. Recherche dans la liste de points de l'activity
    * un point avec le meme nom et, après avoir changer celui ci, va updater
    * le point dans la BD et tous les parties qui ont été enregistrée à ce nom aussi */
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

        remplacerFragment(new ConfigFragment(getResources().getString(R.string.points_am_liorer)));
    }
    /*Trouve le point assicué au nom fournit en paramètre dans la liste de
    * points de l'activité et le retire de la liste, delete ensuite dans la bd
    * le point portant le meme nom, et toutes les parties enregistrées qui font
    * références à ce nom*/
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
        remplacerFragment(new ConfigFragment(getResources().getString(R.string.points_am_liorer)));
    }
    /*synchronise les parties de l'Activité avec celles de la BD*/
    private void refreshParties() {
        this.parties = dbAdapter.fetchAllPartieParDate(this.dataSetActuel);
    }
    /*Efface toutes les parties associées au DataSet présenté à l'utilisateur*/
    public void clearData() {
        dbAdapter.ouvrirBd();
        dbAdapter.clearData(this.dataSetActuel);
        refreshParties();
        dbAdapter.fermerBd();
    }
    /*efface de la liste de l'Activité et de la PD la partie fournie en paramètre*/
    public void effacerPartie(Partie partie) {
        this.parties.remove(partie);
        dbAdapter.ouvrirBd();
        dbAdapter.deletePartie(partie,this.dataSetActuel);
        refreshParties();
        dbAdapter.fermerBd();
    }
    /*Délègue à la classe statistique le calcul du point le plus récurrent de chaque jour des 6 derniers jours*/
    public ArrayList<Stats> getStatsGrouperJour() {
        dbAdapter.ouvrirBd();
        ArrayList<Partie> partiesOrderedParJour = dbAdapter.fetchAllPartieParDate(dataSetActuel);
        dbAdapter.fermerBd();
        ArrayList<Stats> stats = null;
        if (partiesOrderedParJour.size() > 0) {
            stats = Stats.getStatsParIntervalJour(partiesOrderedParJour);
        }
        return stats;
    }
    /*Délègue à la classe statistique le calcul du point le plus récurrent de chaque semaine des 6 derniers semaines*/
    public ArrayList<Stats> getStatsGrouperSemaine() {
        dbAdapter.ouvrirBd();
        ArrayList<Partie> partiesOrderedParJour = dbAdapter.fetchAllPartieParDate(this.dataSetActuel);
        dbAdapter.fermerBd();
        ArrayList<Stats> stats = null;
        if (partiesOrderedParJour.size() > 0) {
        stats = Stats.getStatsParIntervalSemaine(partiesOrderedParJour);
        }
        return stats;
    }
    public int getCouleurGraphique() {
        return this.couleurGraphique;
    }
    /*Mets à jour la couleur graphique, fournie en paramètre, de l'activité
    * et dans la BD */
    public void setCouleurGraphique(int couleur) {
        this.couleurGraphique = couleur;
        dbAdapter.ouvrirBd();
        dbAdapter.updateCouleurGraphique(couleur);
        dbAdapter.fermerBd();
    }
    public ArrayList<String> getNomsDataSets() {
        return nomsDataSets;
    }
    /*Prends en paramètre le numéro (id) du dataset à utiliser pour les données
    * et actualise l'attribut dataSetActuel de l'activity ainsi que dans la BD
    * synchronise la l'attribut points ainsi que parties de l'Activity avec les données
    * du nouveau dataSet*/
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
    /*Change le nom d'affichage du dataSetActuel par celui fourni en paramètre et
    * enregistre le nom sélectionné dans la BD*/
    public void modifierNomDataSet(String nouveauNom) {
        dbAdapter.ouvrirBd();
        dbAdapter.updateNomDataSet(nouveauNom,dataSetActuel);
        initialiserNomDataSets();
        dbAdapter.fermerBd();
        remplacerFragment(new ConfigFragment(getResources().getString(R.string.points_am_liorer)));
    }
}