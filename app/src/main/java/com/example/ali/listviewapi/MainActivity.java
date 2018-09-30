package com.example.ali.listviewapi;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Klasse som skal hente data fra API
    fetchData process;

    //Variabler settes som static for å bli brukt av fetchdata klassen
    static ItemAdapter adapter;
    static ListView listView;
    static TextView loadText;
    static Button fetchBtn, topBtn, loadBtn;
    static int pageCounter = 0;

    String APIUrl = "https://data.brreg.no/enhetsregisteret/enhet.json?page=0";

    //Henter index fra APIUrl for å finne ut hvor '=' ligger. Laster inn neste side ved å erstatte text etter '=' med pagecounter
    int pageIndex = APIUrl.indexOf('=');

    //Variabler som blir brukt av funksjonen loadNextPage()
    String toReplace;
    String APIUrlTest;
    static ArrayList<listObject> listToShow = new ArrayList();

    //Searchview og Menuitem brukes for å lage en søkefelt i actionbar
    SearchView searchView;
    MenuItem searchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialiserer elementer i activity
        loadText = (TextView) findViewById(R.id.loadText);
        listView = (ListView) findViewById(R.id.myListView);
        loadText = (TextView) findViewById(R.id.loadText);
        fetchBtn = (Button) findViewById(R.id.buttonFetch);

        //Knapp som henter data fra API hvis man startet programmet uten internett tilkobling
        fetchBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                process = new fetchData(getApplicationContext(), APIUrl, null);
                process.execute();

            }
        });

        //Knapp som laster inn flere data fra API. Går til neste side i API og stopper når den når 100 ettersom det er maksgrensen.
        loadBtn = (Button) findViewById(R.id.loadBtn);
        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if ((pageCounter + 1) > 100) {
                    Toast.makeText(getApplicationContext(), "Kan ikke laste fler en 100 sider fra API", Toast.LENGTH_LONG).show();
                } else {
                    pageCounter++;

                    //Metode som laster inn neste side fra API
                    loadNextPage();
                }
            }
        });

        //Knapp som får listen til å peke på første(toppen)
        topBtn = (Button) findViewById(R.id.topBtn);
        topBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setSelectionAfterHeaderView();
            }
        });

        //Henter data fra variabel savedInstanceState når man roterer skjermen. Hvis det ikke er noe der(første oppstart) hentes data fra API klassen
        if (savedInstanceState != null)
        {
            if (savedInstanceState.getParcelableArrayList("listToShow") != null)
            {

                listToShow = savedInstanceState.getParcelableArrayList("listToShow");
                adapter = new ItemAdapter(getApplicationContext(), listToShow);
                listView.setAdapter(adapter);
                fetchBtn.setVisibility(View.GONE);
                loadBtn.setVisibility(View.VISIBLE);
                topBtn.setVisibility(View.VISIBLE);
            } else
            {
                process = new fetchData(getApplicationContext(), APIUrl, listToShow);
                process.execute();
            }
        }

        else
        {
            process = new fetchData(getApplicationContext(), APIUrl, listToShow);
            process.execute();
        }

        //Åpner nytt vindu som viser opplysninger om selskapet
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent i = new Intent(getApplicationContext(), showSelskap.class);
                i.putExtra("orgNavn", adapter.getItem(position).getOrgName());
                i.putExtra("orgNummer", adapter.getItem(position).getOrgNumber());
                i.putExtra("orgAdresse", adapter.getItem(position).getAdress());
                i.putExtra("orgHjemmeside", adapter.getItem(position).getHjemmeSide());
                i.putExtra("orgPostSted", adapter.getItem(position).getPostSted());
                i.putExtra("orgPostNr", adapter.getItem(position).getPostNr());
                startActivity(i);
            }
        });
    }

    //Funksjon som laster inn neste side av API ved å bruke en counter variabel og parse det inn i URL til API
    public void loadNextPage() {
        toReplace = APIUrl.substring(pageIndex + 1, APIUrl.length());
        APIUrlTest = APIUrl.replace(toReplace, String.valueOf(pageCounter));
        process = new fetchData(getApplicationContext(), APIUrlTest, listToShow);
        process.execute();
        Toast.makeText(getApplicationContext(), "Laster mer data...",Toast.LENGTH_SHORT).show();
    }

    //Metode som definerer søkefeltet. Søker på organisasjonsnummer hvis input er bare tall. Ellers søker den etter firmanavn.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        searchItem = menu.findItem(R.id.item_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<listObject> templist = new ArrayList<>();
                boolean isNumber = true;
                for (Character c : newText.toCharArray()) {
                    if (!Character.isDigit(c)) {
                        isNumber = false;
                        break;
                    }
                }
                for (listObject obj : listToShow) {
                    if (isNumber) {
                        if (obj.getOrgNumber().startsWith(newText)) {
                            templist.add(obj);
                        }
                    } else {
                        if (obj.getOrgName().toLowerCase().startsWith(newText.toLowerCase())) {
                            templist.add(obj);
                        }
                    }
                }
                adapter = new ItemAdapter(getApplicationContext(), templist);
                listView.setAdapter(adapter);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    //Lagrer listen ved orientasjon skifte.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (listToShow.size() != 0) {
            outState.putParcelableArrayList("listToShow", listToShow);
        }
    }
}