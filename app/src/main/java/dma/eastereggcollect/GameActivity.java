package dma.eastereggcollect;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.Random;


public class GameActivity extends Activity implements View.OnClickListener,Runnable{

    private int runde;
    private int punkte;
    private int anzahlEier;
    private int gesammelteEier;
    private int zeit;
    private float massstab;
    private Random zufallsGenerator = new Random();
    private ViewGroup spielbereich;
    private final static long MAXALTER_EI = 2;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        massstab = getResources().getDisplayMetrics().density;

        spielbereich = (FrameLayout) findViewById(R.id.spielbereich);
        spielStarten();
    }

    private void spielStarten() {
        runde = 0;
        punkte = 0;
        starteRunde();
    }

    private void starteRunde() {
        runde++;
        anzahlEier = runde * 10;
        gesammelteEier = 0;
        zeit = 60;
        bildschirmAktualisieren();
        handler.postDelayed(this, 1000);
    }

    private void bildschirmAktualisieren() {
        TextView tvPunkte = (TextView) findViewById(R.id.points); tvPunkte.setText(Integer.toString(punkte));
        TextView tvRunde = (TextView) findViewById(R.id.round); tvRunde.setText(Integer.toString(runde));
        TextView tvTreffer = (TextView) findViewById(R.id.hits); tvTreffer.setText(Integer.toString(gesammelteEier));
        TextView tvZeit = (TextView) findViewById(R.id.time); tvZeit.setText(Integer.toString(zeit));
        FrameLayout flTreffer = (FrameLayout) findViewById(R.id.bar_hits); FrameLayout flZeit = (FrameLayout) findViewById(R.id.bar_time);
        ViewGroup.LayoutParams lpTreffer = flTreffer.getLayoutParams(); lpTreffer.width = Math.round(massstab * 300
                * Math.min(gesammelteEier, anzahlEier) / anzahlEier);
        ViewGroup.LayoutParams lpZeit = flZeit.getLayoutParams();
        lpZeit.width = Math.round(massstab * 300 * zeit / 60);
    }

    public void zeitHerunterzaehlen() {
        zeit = zeit - 1;
        float zufallszahl = zufallsGenerator.nextFloat();
        double wahrscheinlichkeit = anzahlEier * 1.5 /60;
        if(wahrscheinlichkeit > 1)
        {
            einEiAnzeigen();
            if((wahrscheinlichkeit -1) > zufallszahl)
            {
                einEiAnzeigen();
            }
        } else {
            if (wahrscheinlichkeit > zufallszahl)
            {
                einEiAnzeigen();
            }
        }
        eierVerschwinden();
        bildschirmAktualisieren();
        if(!pruefeSpielende())
        {
            if (!pruefeRundenende())
            {
                handler.postDelayed(this, 1000);
            }
        }

    }

    private boolean pruefeSpielende() {
        if (zeit == 0 && (gesammelteEier < anzahlEier))
        {
            gameOver();
            return true;
        }
        return false;
    }

    private void gameOver() {
        Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.game_over);
        dialog.show();
    }

    private boolean pruefeRundenende() {
        return true;
    }

    private void einEiAnzeigen() {
        int breite = spielbereich.getWidth();
        int hoehe = spielbereich.getHeight();
        // Groesse des Ei's
        int ei_breite = (int) Math.round(massstab * 34); int ei_hoehe = (int) Math.round(massstab * 40); // Positionierung der View auf dem Spielfeld
        int links = zufallsGenerator.nextInt(breite - ei_breite); int oben = zufallsGenerator.nextInt(hoehe - ei_hoehe);
        // Erzeugen der ImageView zur Anzeige eines Eis
        ImageView ei = new ImageView(this);
        // Auswahl des anzuzeigenden Eis und
        // Zuweisen der passenden Image Ressource an den erzeugten // View ei mit Hilfe des Zufallsgenerators
        //.............. bitte ergänzen ........
        // Als Listener für das Ei die aktuelle Aktivity anmelden
        ei.setOnClickListener(this);
        // Wo soll der ImageView erscheinen?
        // Wie groß soll der ImageView zur Aufnahme des Drawable sein?
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
        ei_breite, ei_hoehe);
        // Setzen der Position des Imageview
        params.leftMargin = links;
        params.topMargin = oben;
        params.gravity = Gravity.TOP + Gravity.LEFT;
        spielbereich.addView(ei, params);
        //ei.setTag(R.id.geburtsdatum, new Date());



    }

    private void eierVerschwinden() {}

    @Override
    public void onClick(View v) {

    }

    @Override
    public void run() {
        zeitHerunterzaehlen();
    }












    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
