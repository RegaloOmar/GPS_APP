package com.rr.gps_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Button btn_Seleccionar;
    private TextView txtUser, txtCanal;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        txtUser = findViewById(R.id.txtUsuario);
        txtCanal = findViewById(R.id.txtCanal);
        btn_Seleccionar = findViewById(R.id.btnSeleccionar);

        HashMap<String,String> user = sessionManager.getUSerDetail();
        final String mUSer = user.get(sessionManager.USER);
        final String mCanal = user.get(sessionManager.IDCANAL);
        String mLocalidad = user.get(sessionManager.LOCALIDAD);

        txtUser.setText(mUSer);
        txtCanal.setText(mLocalidad);



        btn_Seleccionar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent id;
                id =  new Intent(MainActivity.this, DatosActivity.class);
                id.putExtra("datosUsuario",mUSer);
                id.putExtra("datosCanal",mCanal);
                startActivity(id);
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
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }
}
