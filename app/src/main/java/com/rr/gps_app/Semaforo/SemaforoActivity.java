package com.rr.gps_app.Semaforo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.GenericTransitionOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.rr.gps_app.Adapter.SessionManager;
import com.rr.gps_app.CamaraActivity;
import com.rr.gps_app.IncidenciasActivity;
import com.rr.gps_app.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SemaforoActivity extends AppCompatActivity {

    private Button btn_Evidencia,btn_Incidencias,btn_Estatus;
    private Switch gSwitch,rSwitch,bSwitch,ySwitch;
    private TextView direccion, lat,lon;
    String state,talon,userSend,mUSer ;
    SessionManager sessionManager;
    private ProgressDialog pDialog;
    private Date date;
    private FusedLocationProviderClient fusedLocationClient;
    String URL = "https://rrdevsolutions.com/cdm/master/request/insertStatus.php";
    //String URL = "https://rrdevsolutions.com/cdmBueno/master/request/insertStatus.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semaforo);

        getSupportActionBar().setTitle("Semaforo");
        sessionManager = new SessionManager(SemaforoActivity.this);
        sessionManager.checkLogin();

        HashMap<String,String> user = sessionManager.getUSerDetail();
         mUSer = user.get(sessionManager.USER);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        talon = getIntent().getStringExtra("talon");

        direccion = findViewById(R.id.txtDireccion);
        lat = findViewById(R.id.txtLat);
        lon = findViewById(R.id.txtLon);
        btn_Estatus = findViewById(R.id.btnEstatus);
        btn_Evidencia = findViewById(R.id.btnEvidencia);
        btn_Incidencias = findViewById(R.id.btnIncidencias);
        gSwitch = findViewById(R.id.switchGreen);
        rSwitch = findViewById(R.id.switchRed);
        ySwitch = findViewById(R.id.switchYellow);
        bSwitch = findViewById(R.id.switchBlue);

        locationStart();

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

        //Fecha Actual
        date = new Date();


        btn_Estatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                semaforo(URL);

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
                Intent inc;
                inc = new Intent(SemaforoActivity.this, IncidenciasActivity.class);
                inc.putExtra("talon",talon);
                inc.putExtra("datosUsuario",mUSer);
                startActivity(inc);
            }
        });

    }

    private void semaforo(String URL)
    {

        userSend = mUSer;
        final String fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(date);


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
                params.put("us",userSend);
                params.put("dire", direccion.getText().toString().trim());
                params.put("lat",lat.getText().toString().trim());
                params.put("lon",lon.getText().toString().trim());
                params.put("fechaActual",fechaActual);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setSemaforoActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
        lat.setText("Localización agregada");
        direccion.setText("");
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationStart();
                return;
            }
        }
    }

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    direccion.setText(DirCalle.getAddressLine(0));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements LocationListener {
        SemaforoActivity semaforoActivity;
        public SemaforoActivity getMainActivity() {
            return semaforoActivity;
        }
        public void setSemaforoActivity(SemaforoActivity semaforoActivity) {
            this.semaforoActivity = semaforoActivity;
        }
        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion
            loc.getLatitude();
            loc.getLongitude();
            String Text1 = String.valueOf(loc.getLatitude());
            String Text2 = String.valueOf(loc.getLongitude());
            lat.setText(Text1);
            lon.setText(Text2);
            this.semaforoActivity.setLocation(loc);
        }
        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            lat.setText("GPS Desactivado");
        }
        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            lat.setText("GPS Activado");
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
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
