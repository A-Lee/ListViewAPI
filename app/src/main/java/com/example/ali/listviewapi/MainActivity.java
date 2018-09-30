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
import java.util.Arrays;
import java.util.Collections;
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


    TextView sortFirma, sortOrgNum;
    boolean sortFirmaAsc = false;
    boolean sortOrgNumAsc = false;
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
        topBtn = (Button) findViewById(R.id.topBtn);
        loadBtn = (Button) findViewById(R.id.loadBtn);

        sortFirma = (TextView) findViewById(R.id.sortFirma);
        sortOrgNum = (TextView) findViewById(R.id.sortOrgNum);


        //Henter data fra variabel savedInstanceState når man roterer skjermen. Hvis det ikke er noe der(første oppstart) hentes data fra API klassen
        if (savedInstanceState != null)
        {
            if (savedInstanceState.getParcelableArrayList("listToShow") != null)
            {

                listToShow = savedInstanceState.getParcelableArrayList("listToShow");
                adapter = new ItemAdapter(getApplicationContext(), listToShow);
                listView.setAdapter(adapter);
            } else
            {
                process = new fetchData(getApplicationContext(), APIUrl, listToShow);
                process.execute();
            }
            fetchBtn.setVisibility(View.GONE);
            loadBtn.setVisibility(View.VISIBLE);
            topBtn.setVisibility(View.VISIBLE);
            loadText.setText(savedInstanceState.getString("loadText"));
            sortFirmaAsc = savedInstanceState.getBoolean("sortFirmaAsc");
            sortOrgNumAsc = savedInstanceState.getBoolean("sortOrgNumAsc");
        }

        else
        {
            process = new fetchData(getApplicationContext(), APIUrl, listToShow);
            process.execute();
            fetchBtn.setVisibility(View.GONE);
            loadBtn.setVisibility(View.VISIBLE);
            topBtn.setVisibility(View.VISIBLE);
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
        loadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if ((pageCounter + 1) > 100) {
                    Toast.makeText(getApplicationContext(), "Kan ikke laste fler en 100 sider fra API", Toast.LENGTH_LONG).show();
                } else {
                    //Metode som laster inn neste side fra API
                    loadNextPage();
                }
            }
        });

        //Knapp som får listen til å peke på første(toppen)
        topBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setSelectionAfterHeaderView();
            }
        });

        sortFirma.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(sortFirmaAsc == false)
                {
                    Collections.sort(listToShow, listObject.firmaNavnComparableAsc);
                    sortFirmaAsc = true;
                    sortOrgNumAsc = false;
                }
                else
                {
                    Collections.sort(listToShow, listObject.firmaNavnComparableDesc);
                    sortFirmaAsc = false;
                    sortFirmaAsc = false;
                }
                adapter = new ItemAdapter(getApplicationContext(), listToShow);
                listView.setAdapter(adapter);
            }
        });

        sortOrgNum.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(sortOrgNumAsc == false)
                {
                    Collections.sort(listToShow,listObject.orgNummberComparableAsc);
                    sortOrgNumAsc = true;
                    sortFirmaAsc = false;
                }
                else
                {
                    Collections.sort(listToShow,listObject.orgNummberComparableDesc);
                    sortOrgNumAsc = false;
                    sortFirmaAsc = false;
                }
                adapter = new ItemAdapter(getApplicationContext(), listToShow);
                listView.setAdapter(adapter);
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
            outState.putString("loadText", loadText.getText().toString());
            outState.putBoolean("sortOrgNumAsc", sortOrgNumAsc);
            outState.putBoolean("sortFirmaAsc", sortFirmaAsc);
        }
    }
}