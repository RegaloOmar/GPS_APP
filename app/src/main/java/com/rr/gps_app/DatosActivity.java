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
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DatosActivity extends AppCompatActivity {

    private Button btn_Semforo,btn_talon;
    private TextView edtTalon,edtPlacas,edtSello,edtTransp,edtNet,edtFecha,edtConfir;
    String user, canal, talon;
    SessionManager sessionManager;
    private RequestQueue requestQueue;
    private static String URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        user = getIntent().getStringExtra("datosUsuario");
        canal = getIntent().getStringExtra("datosCanal");

        getSupportActionBar().setTitle("Datos del Talon");

        URL = "https://rrdevsolutions.com/cdm/master/request/request.php?usuario="+user;

        searchInfo(URL);

        edtTalon = findViewById(R.id.textTalon);
        edtPlacas = findViewById(R.id.textPlacas);
        edtSello = findViewById(R.id.textSello);
        edtTransp = findViewById(R.id.textTransp);
        edtNet = findViewById(R.id.textNet);
        edtFecha = findViewById(R.id.textFecha);
        edtConfir = findViewById(R.id.textConfim);
        btn_Semforo = findViewById(R.id.btnSemaforo);
        btn_talon = findViewById(R.id.btnTalon);


        btn_Semforo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                talon = edtTalon.getText().toString();
                Intent i;
                i = new Intent(DatosActivity.this, SemaforoActivity.class);
                i.putExtra("talon",talon);
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

    private void searchInfo(String URL)
    {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        edtTalon.setText(jsonObject.getString("Talon_Localidad"));
                        edtPlacas.setText(jsonObject.getString("Placas"));
                        edtSello.setText(jsonObject.getString("Sello"));
                        edtTransp.setText(jsonObject.getString("Transportista"));
                        edtNet.setText(jsonObject.getString("Net"));
                        edtFecha.setText(jsonObject.getString("Fecha_Cita"));
                        edtConfir.setText(jsonObject.getString("Confirmacion"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error de Conexion",Toast.LENGTH_LONG).show();
            }
        }
        );

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
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
        Intent i = new Intent(DatosActivity.this, LoginActivity.class);
        startActivity(i);
    }
}
