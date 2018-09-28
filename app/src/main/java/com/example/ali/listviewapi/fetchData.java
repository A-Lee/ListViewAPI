package com.example.ali.listviewapi;

import android.os.AsyncTask;

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

public class fetchData extends AsyncTask<Void,Void,Void>
{

    String output = "";
    String JsonOutput = "";
    List listOfObjects = new ArrayList();
    @Override
    protected Void doInBackground(Void... voids)
    {
        try
        {
            URL url3 = new URL("https://data.brreg.no/enhetsregisteret/enhet.json?page=0");
            HttpURLConnection httpURLConnection  = (HttpURLConnection) url3.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while(line != null)
            {
                line = bufferedReader.readLine();
                output += line;
            }
            JSONObject JsonObject = new JSONObject(output);
            JSONArray links = JsonObject.getJSONArray("links");
            JSONArray data = JsonObject.getJSONArray("data");
            //JSONArray JA = new JSONArray(output);

            for(int i = 0 ; i < data.length(); i++)
            {

                JSONObject firstData = (JSONObject) data.get(i);
                JSONObject firstForretning = (JSONObject) firstData.get("forretningsadresse");
                String firmaNavn = !firstData.get("navn").equals(new JSONObject()) ? firstData.get("navn").toString() : "No Name";
                String organisasjonsNummer = !firstData.get("organisasjonsnummer").equals(new JSONObject())? firstData.get("organisasjonsnummer").toString()
                        : "No Organisation number";
                String kommune = firstForretning.has("kommune") ? firstForretning.get("kommune").toString() : "No Kommune";
                String land =  firstForretning.has("land") ? firstForretning.get("land").toString() : "No Country";
                String adresse = firstForretning.has("adresse") ? firstForretning.get("adresse").toString() : "No Adress";
                String postNr = firstForretning.has("postnummer") ? firstForretning.get("postnummer").toString() : "No PostNr";
                String hjemmeside = firstData.has("hjemmeside") ? firstData.get("hjemmeside").toString() : "No hjemmeside";
                Object firmaNavnOBJ = firstData.get("navn");
                JsonOutput += "Name: " + firmaNavn + "\nGender: " + organisasjonsNummer + "\nEye color: " +
                        kommune + "\nFirst friend: " + "\n" + land + "\n\n";
                listOfObjects.add(new listObject(firmaNavn,organisasjonsNummer, hjemmeside,adresse, postNr));
            }
        }
        catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid)
    {
        super.onPostExecute(aVoid);

        MainActivity.listToShow = listOfObjects;
    }
    public List<listObject> getList()
    {
        return listOfObjects;
    }
}

