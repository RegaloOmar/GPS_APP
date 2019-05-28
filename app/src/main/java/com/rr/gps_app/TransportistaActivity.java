package com.rr.gps_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TransportistaActivity extends AppCompatActivity {

    private Button btn_Siguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportista);

        btn_Siguiente = (Button) findViewById(R.id.buttonSiguiente);

        btn_Siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(TransportistaActivity.this, SemaforoActivity.class);
                startActivity(i);
            }
        });
    }
}
