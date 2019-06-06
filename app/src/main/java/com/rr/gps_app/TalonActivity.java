package com.rr.gps_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TalonActivity extends AppCompatActivity {

    private EditText TalonText;
    private Button btn_CargarDatos;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talon);

        getSupportActionBar().setTitle("Talon");

        sessionManager = new SessionManager(TalonActivity.this);
        sessionManager.checkLogin();

        TalonText = (EditText) findViewById(R.id.txtTalon);

        btn_CargarDatos = (Button)findViewById(R.id.btnCargarDatos);

        btn_CargarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(TalonActivity.this, DatosActivity.class);
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
        Intent i = new Intent(TalonActivity.this, LoginActivity.class);
        startActivity(i);
    }
}
