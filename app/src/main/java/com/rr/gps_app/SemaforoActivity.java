package com.rr.gps_app;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SemaforoActivity extends AppCompatActivity {

    private Button btn_Camara;
    private Button btn_Incidencias;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semaforo);

        getSupportActionBar().setTitle("Semaforo");

        btn_Camara = (Button) findViewById(R.id.btnCamara);


        btn_Incidencias = (Button) findViewById(R.id.btnIncidencias);




        btn_Camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivity(i);

            }
        });


        btn_Incidencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(SemaforoActivity.this, IncidenciasActivity.class);
                startActivity(intent);
            }
        });

    }
}
