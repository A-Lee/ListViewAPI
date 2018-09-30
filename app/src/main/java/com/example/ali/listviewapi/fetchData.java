package com.example.ali.listviewapi;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//Klasse som henter data fra API. Arver fra AsyncTask for å kjøre på bakgrunnen.
public class fetchData extends AsyncTask<Void,Void,Void>
{

    //Leser inn all input fra API og lagrer det som JSONObject i linje 63.
    String output = "";

    //Sjekker hvis man har internett-tilkobling.
    boolean internettConnection = true;

    Context context;
    String APIPage;
    ArrayList listOfObjects;

    public fetchData(Context c, String APIPage, ArrayList<listObject> listObjects)
    {
        context = c;
        this.APIPage = APIPage;

        //Hvis man henter flere data fra API(data fra neste side) så legger man på ny data i eksisterende liste. Ellers så henter man data for første gang og initialiserer listOfObjects,
        if(listObjects == null)
        {
            this.listOfObjects = new ArrayList();
        }
        else
        {
            this.listOfObjects = listObjects;
        }
    }

    @Override
    protected Void doInBackground(Void... voids)
    {
        MainActivity.loadText.setText("Loading data from Api....");
        try
        {
            //Åpner opp API link som ble tilsendt fra MainActivity.java og leser data linje for linje. Hver linje blir lagret i variabel Output.
            URL url3 = new URL(APIPage);
            HttpURLConnection httpURLConnection  = (HttpURLConnection) url3.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while(line != null)
            {
                line = bufferedReader.readLine();
                output += line;
            }

            //Innholdet i output variabel blir overført og omgjort til en JSONObject
            JSONObject JsonObject = new JSONObject(output);

            //Henter de mest relevante dataene fra "data" listen i JSON objektet.
            JSONArray data = JsonObject.getJSONArray("data");
            for(int i = 0 ; i < data.length(); i++)
            {

                JSONObject firstData = (JSONObject) data.get(i);
                JSONObject firstForretning = (JSONObject) firstData.get("forretningsadresse");
                String firmaNavn = !firstData.get("navn").equals(new JSONObject()) ? firstData.get("navn").toString() : "Ingen navn";
                String organisasjonsNummer = !firstData.get("organisasjonsnummer").equals(new JSONObject())? firstData.get("organisasjonsnummer").toString()
                        : "No Organisation number";
                //String kommune = firstForretning.has("kommune") ? firstForretning.get("kommune").toString() : "Ingen kommune";
                String postSted =  firstForretning.has("poststed") ? firstForretning.get("poststed").toString() : "Ingen Poststed";
                String adresse = firstForretning.has("adresse") ? firstForretning.get("adresse").toString() : "Ingen Adress";
                String postNr = firstForretning.has("postnummer") ? firstForretning.get("postnummer").toString() : "Ingen Postnummr";
                String hjemmeside = firstData.has("hjemmeside") ? firstData.get("hjemmeside").toString() : "Ingen hjemmeside";
                listOfObjects.add(new listObject(firmaNavn,organisasjonsNummer, hjemmeside,adresse, postSted,postNr));
                internettConnection = true;
            }
        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {

            internettConnection = false;
            MainActivity.loadText.setText("No internett connection...");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        MainActivity.listToShow = listOfObjects;
    }

    //Setter innhold til listview og gjemmer/synligjør knapper avhengig av resultatet.
    @Override
    protected void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);
        if(internettConnection)
        {
            MainActivity.adapter = new ItemAdapter(context, MainActivity.listToShow);
            MainActivity.listView.setAdapter(MainActivity.adapter);
            MainActivity.loadText.setVisibility(View.GONE);
            MainActivity.fetchBtn.setVisibility(View.GONE);
            MainActivity.loadBtn.setVisibility(View.VISIBLE);
            MainActivity.topBtn.setVisibility(View.VISIBLE);;
        }
    }
    public ArrayList<listObject> getList()
    {
        return listOfObjects;
    }
}

