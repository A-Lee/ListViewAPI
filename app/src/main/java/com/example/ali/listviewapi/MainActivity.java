package com.example.ali.listviewapi;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    static List<listObject> listToShow = new ArrayList();
    fetchData process = new fetchData();
    ItemAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        process.execute();
        setContentView(R.layout.activity_main);
        onPostCreate(null,null);
        listView = (ListView) findViewById(R.id.myListView);
        listToShow = process.getList();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = listView.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),"" + text, Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), showSelskap.class);
                i.putExtra("orgNavn",adapter.getItem(position).getOrgName());
                i.putExtra("orgNummer",adapter.getItem(position).getOrgNumber());
                i.putExtra("orgAdresse",adapter.getItem(position).getAdress());
                i.putExtra("orgHjemmeside",adapter.getItem(position).getHjemmeSide());
                i.putExtra("orgPostNr",adapter.getItem(position).getPostNr());
                startActivity(i);

            }
        });
        System.out.println(listToShow.size());
        adapter = new ItemAdapter(getApplicationContext(), listToShow);
        listView.setAdapter(adapter);

        Button knapp = (Button) findViewById(R.id.buttonFetch);
        knapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter = new ItemAdapter(getApplicationContext(), listToShow);
                listView.setAdapter(adapter);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<listObject> templist = new ArrayList<>();

                for(listObject obj : listToShow)
                {
                    if(obj.getOrgName().toLowerCase().contains(newText.toLowerCase()))
                    {
                        templist.add(obj);
                    }
                }
                adapter = new ItemAdapter(getApplicationContext(), templist);
                listView.setAdapter(adapter);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
