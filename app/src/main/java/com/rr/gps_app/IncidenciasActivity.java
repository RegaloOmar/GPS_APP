package com.rr.gps_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class IncidenciasActivity extends AppCompatActivity {


    private Button btnIncidencia;
    SessionManager sessionManager;
    String talon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidencias);

        getSupportActionBar().setTitle("Incidencias");

        sessionManager = new SessionManager(IncidenciasActivity.this);
        sessionManager.checkLogin();

        talon = getIntent().getStringExtra("talon");

        btnIncidencia = findViewById(R.id.btnMandar);

        Toast.makeText(getApplicationContext(),talon,Toast.LENGTH_LONG).show();

        btnIncidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(IncidenciasActivity.this, SemaforoActivity.class);
                startActivity(i);
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
                sessionManager.logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        Intent i = new Intent(IncidenciasActivity.this, LoginActivity.class);
        startActivity(i);
    }
}
