package com.example.ali.listviewapi;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;

public class showSelskap extends AppCompatActivity {

    private TextView orgNavn,orgNummer,orgAdresse,orgPostNr, orgHjemmeside, orgInfo;
    private Button backBtn, googleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_selskap);

        orgInfo = (TextView) findViewById(R.id.headerInfo);
        orgNavn = (TextView) findViewById(R.id.orgNavn);
        orgNummer = (TextView) findViewById(R.id.orgNr);
        orgAdresse = (TextView) findViewById(R.id.orgAdresse);
        orgPostNr = (TextView) findViewById(R.id.orgPostNr);
        orgHjemmeside = (TextView) findViewById(R.id.orgHjemmeside);

        Intent info = getIntent();

        orgNavn.setText(info.getStringExtra("orgNavn"));
        orgInfo.setText(orgInfo.getText().toString() + orgNavn.getText().toString());
        orgNummer.setText(info.getStringExtra("orgNummer"));
        orgAdresse.setText(info.getStringExtra("orgAdresse"));
        orgPostNr.setText(info.getStringExtra("orgPostNr") + " " + info.getStringExtra("orgPostSted"));

        orgHjemmeside.setText(info.getStringExtra("orgHjemmeside"));
        orgHjemmeside.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(orgHjemmeside.getText().toString().equalsIgnoreCase("Ingen hjemmeside"))
                {
                    Toast.makeText(getApplicationContext(),"Ingen gyldig internettside funnet", Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(!orgHjemmeside.getText().toString().startsWith("http://"))
                    {
                        Intent visitPage = new Intent(Intent.ACTION_VIEW, Uri.parse("http://" + orgHjemmeside.getText().toString()));
                        startActivity(visitPage);
                    }
                    else
                    {
                        Intent visitPage = new Intent(Intent.ACTION_VIEW, Uri.parse(orgHjemmeside.getText().toString()));
                        startActivity(visitPage);
                    }
                }
            }
        });

        orgAdresse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(orgAdresse.getText().toString().equalsIgnoreCase("Ingen Adress"))
                {
                    Toast.makeText(getApplicationContext(),"Ingen gyldig adresse funnet", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("geo:0,0?q=%s",
                            URLEncoder.encode(orgAdresse.getText().toString()))));
                    startActivity(i);
                }
            }
        });

        backBtn = (Button) findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        googleBtn = (Button) findViewById(R.id.googleBtn);
        googleBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/#q=" + orgNavn.getText().toString())));
            }
        });


    }
}
