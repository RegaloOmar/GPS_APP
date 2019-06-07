package com.rr.gps_app;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
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
    final private int REQUEST_CODE_ASK_PERMISSION = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        solicitarPermisos();

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
                sessionManager.logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void solicitarPermisos() {
        int permisoStroage = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permisosCamara = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA);
        int permisosUbicacion = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permisosLocate = ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permisoStroage != PackageManager.PERMISSION_GRANTED || permisosCamara != PackageManager.PERMISSION_GRANTED || permisosUbicacion != PackageManager.PERMISSION_GRANTED || permisosLocate != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_ASK_PERMISSION);
            }
        }
    }

    private void logout() {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
    }
}
