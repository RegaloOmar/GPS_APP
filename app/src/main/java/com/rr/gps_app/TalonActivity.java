package com.rr.gps_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class TalonActivity extends AppCompatActivity {

    private EditText TalonText;
    private Button btn_CargarDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talon);

        TalonText = (EditText) findViewById(R.id.txtTalon);

        btn_CargarDatos = (Button)findViewById(R.id.btnCargarDatos);



    }
}
