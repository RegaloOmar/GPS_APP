package com.rr.gps_app;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;

public class SemaforoActivity extends AppCompatActivity {

    private Button btn_Evidencia,btn_Incidencias,btn_Estatus;
    private Switch gSwitch,rSwitch,bSwitch,ySwitch;
    String state ;
    String talon;
    SessionManager sessionManager;
    private ProgressDialog pDialog;
    private FusedLocationProviderClient fusedLocationClient;
    String URL = "https://rrdevsolutions.com/cdm/master/request/insertStatus.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semaforo);

        getSupportActionBar().setTitle("Semaforo");
        sessionManager = new SessionManager(SemaforoActivity.this);
        sessionManager.checkLogin();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        talon = getIntent().getStringExtra("talon");

        btn_Estatus = findViewById(R.id.btnEstatus);
        btn_Evidencia = findViewById(R.id.btnEvidencia);
        btn_Incidencias = findViewById(R.id.btnIncidencias);
        gSwitch = findViewById(R.id.switchGreen);
        rSwitch = findViewById(R.id.switchRed);
        ySwitch = findViewById(R.id.switchYellow);
        bSwitch = findViewById(R.id.switchBlue);

        upLocation();


        gSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    state = "1";
                }else
                {
                    state = "0";
                }
            }
        });


        ySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    state = "2";
                }else
                {
                    state = "0";
                }
            }
        });

        rSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    state = "3";
                }else
                {
                    state = "0";
                }
            }
        });

        bSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    state = "4";
                }else
                {
                    state = "0";
                }
            }
        });


        btn_Estatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                semaforo(URL);
               // switchReturn();
            }
        });

        btn_Evidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                i = new Intent(SemaforoActivity.this, CamaraActivity.class);
                i.putExtra("talon",talon);
                startActivity(i);

            }
        });


        btn_Incidencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(SemaforoActivity.this, IncidenciasActivity.class);
                intent.putExtra("talon",talon);
                startActivity(intent);
            }
        });

    }

    private void semaforo(String URL)
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                Toast.makeText(getApplicationContext(),"Se han insertado datos",Toast.LENGTH_LONG).show();
                switchReturn();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getApplicationContext(),"Elegir un Status",Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("talon",talon);
                params.put("state",state);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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

    public void switchReturn()
    {

        if (state.equals("1"))
        {
            gSwitch.toggle();
        }else if (state.equals("2"))
        {
            ySwitch.toggle();
        }else if(state.equals("3"))
        {
            rSwitch.toggle();
        }else if(state.equals("4"))
        {
            bSwitch.toggle();
        }else{
            Toast.makeText(getApplicationContext(),"Elegir un Status",Toast.LENGTH_LONG).show();
        }
    }

    private void upLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    Toast.makeText(getApplicationContext(), "Latitud: " + location.getLatitude() + "Longitud: "
                            + location.getLongitude(), Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    //Metodo para crear dialogos al momento de hacer cambios
    public void dialog()
    {
        pDialog = new ProgressDialog(SemaforoActivity.this);
        pDialog.setMessage("Guardando Estatus...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }
}
