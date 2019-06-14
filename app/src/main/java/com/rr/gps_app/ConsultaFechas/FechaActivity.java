package com.rr.gps_app.ConsultaFechas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.rr.gps_app.R;
import com.rr.gps_app.SessionManager;

public class FechaActivity extends AppCompatActivity {

    SessionManager sessionManager;
    String user, canal, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fecha);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        getSupportActionBar().setTitle("Datos por Fecha");

        user = getIntent().getStringExtra("mUser");
        canal = getIntent().getStringExtra("mCanal");
        date = getIntent().getStringExtra("mFecha");
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
}
