package com.example.ali.listviewapi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class showSelskap extends AppCompatActivity {

    TextView orgNavn,orgNummer,orgAdresse,orgPostNr,orgHjemmeside;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_selskap);

        orgNavn = (TextView) findViewById(R.id.orgNavn);
        orgNummer = (TextView) findViewById(R.id.orgNr);
        orgAdresse = (TextView) findViewById(R.id.orgAdresse);
        orgPostNr = (TextView) findViewById(R.id.orgPostNr);
        orgHjemmeside = (TextView) findViewById(R.id.orgHjemmeside);

        Intent info = getIntent();
        orgNavn.setText(info.getStringExtra("orgNavn"));
        orgNummer.setText(info.getStringExtra("orgNummer"));
        orgAdresse.setText(info.getStringExtra("orgAdresse"));
        orgPostNr.setText(info.getStringExtra("orgPostNr"));
        orgHjemmeside.setText(info.getStringExtra("orgHjemmeside"));

    }
}
