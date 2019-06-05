package com.rr.gps_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DatosActivity extends AppCompatActivity {

    private Button btn_Semforo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);

        getSupportActionBar().setTitle("Datos del Talon");

        btn_Semforo = (Button)findViewById(R.id.btnSemaforo);
        btn_Semforo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(DatosActivity.this, SemaforoActivity.class);
                startActivity(i);
            }
        });

    }
}
