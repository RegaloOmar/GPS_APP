package com.rr.gps_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class IncidenciasActivity extends AppCompatActivity {


    private Button btnIncidencia;
    private EditText edtIncidencia;
    SessionManager sessionManager;
    private ProgressDialog pDialog;
    String talon;
    String URL = "https://rrdevsolutions.com/cdm/master/request/insertIncidencia.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidencias);

        getSupportActionBar().setTitle("Incidencias");

        sessionManager = new SessionManager(IncidenciasActivity.this);
        sessionManager.checkLogin();

        talon = getIntent().getStringExtra("talon");

        edtIncidencia = findViewById(R.id.txtIncidencias);
        btnIncidencia = findViewById(R.id.btnMandar);

        Toast.makeText(getApplicationContext(),talon,Toast.LENGTH_LONG).show();

        btnIncidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                incidencia(URL);
                dialog();
                Intent i = new Intent(IncidenciasActivity.this, SemaforoActivity.class);
                startActivity(i);
            }
        });



    }

    private void incidencia(String URL)
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                //Toast.makeText(getApplicationContext(),"Se han insertado datos",Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("talon",talon);
                params.put("incidencia",edtIncidencia.getText().toString());
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

    //Metodo para crear dialogos al momento de hacer cambios
    public void dialog()
    {
        pDialog = new ProgressDialog(IncidenciasActivity.this);
        pDialog.setMessage("Mandando Incidencia...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }

    private void logout() {
        Intent i = new Intent(IncidenciasActivity.this, LoginActivity.class);
        startActivity(i);
    }
}
