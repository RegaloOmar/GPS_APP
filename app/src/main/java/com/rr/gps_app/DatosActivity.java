package com.rr.gps_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DatosActivity extends AppCompatActivity {

    private Button btn_Semforo,btn_talon;
    private TextView edtUser, edtCanal;
    String user, canal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);

        user = getIntent().getStringExtra("datosUsuario");
        canal = getIntent().getStringExtra("datosCanal");

        getSupportActionBar().setTitle("Datos del Talon");

        edtUser = findViewById(R.id.textTalon);
        edtCanal = findViewById(R.id.textPlacas);
        btn_Semforo = findViewById(R.id.btnSemaforo);
        btn_talon = findViewById(R.id.btnTalon);

        edtUser.setText(user);
        edtCanal.setText(canal);


        btn_Semforo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(DatosActivity.this, SemaforoActivity.class);
                startActivity(i);
            }
        });

        btn_talon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it;
                it = new Intent(DatosActivity.this, TalonActivity.class);
                startActivity(it);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        Intent i = new Intent(DatosActivity.this, LoginActivity.class);
        startActivity(i);
    }
}
