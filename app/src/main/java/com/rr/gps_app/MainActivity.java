package com.rr.gps_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btn_Seleccionar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Cedis");

        btn_Seleccionar = (Button) findViewById(R.id.btnSeleccionar);


        btn_Seleccionar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i;
                i =  new Intent(MainActivity.this, TalonActivity.class);
                startActivity(i);
            }
        });
    }
}
