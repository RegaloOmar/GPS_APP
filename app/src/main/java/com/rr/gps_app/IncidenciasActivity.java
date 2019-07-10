package com.rr.gps_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rr.gps_app.Adapter.SessionManager;
import com.rr.gps_app.Login.LoginActivity;
import com.rr.gps_app.Semaforo.SemaforoActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class IncidenciasActivity extends AppCompatActivity {


    private Button btnIncidencia;
    private EditText edtIncidencia;
    SessionManager sessionManager;
    private ProgressDialog pDialog;
    String talon,resIncidencia,userSend;
    private Date date;
    final private int REQUEST_CODE_ASK_PERMISSION = 111;
    private Spinner incidencias;
    String URL = "https://rrdevsolutions.com/cdm/master/request/insertIncidencia.php";
    //String URL = "https://rrdevsolutions.com/cdmBueno/master/request/insertIncidencia.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidencias);

        getSupportActionBar().setTitle("Incidencias");

        sessionManager = new SessionManager(IncidenciasActivity.this);
        sessionManager.checkLogin();

        talon = getIntent().getStringExtra("talon");
        userSend = getIntent().getStringExtra("datosUsuario");

        incidencias = findViewById(R.id.spIncidencias);
        edtIncidencia = findViewById(R.id.txtIncidencias);
        btnIncidencia = findViewById(R.id.btnMandar);

        HashMap<String,String> user = sessionManager.getUSerDetail();
        final String mUSer = user.get(sessionManager.USER);
        final String mCanal = user.get(sessionManager.IDCANAL);

        //Fecha Actual
        date = new Date();


        btnIncidencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                spinIncidencia();
                incidencia(URL);
                dialog();
                Intent i = new Intent(IncidenciasActivity.this, SemaforoActivity.class);
                i.putExtra("talon",talon);
                startActivity(i);
            }
        });

        String [] opciones = {"LIBERADO AL 100%","LIBERADO CON DEVOLUCION","PROCESO DE DESCARGA","AUSENCIA","LIBERADO CON FALTANTE"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,opciones);
        incidencias.setAdapter(adapter);

    }

    private void incidencia(String URL)
    {
        final String fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(date);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                Toast.makeText(getApplicationContext(),"Se han insertado datos",Toast.LENGTH_LONG).show();
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
                params.put("tipoIncidencia",resIncidencia);
                params.put("user",userSend);
                params.put("fechaActual",fechaActual);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void spinIncidencia()
    {
        String seleccion = incidencias.getSelectedItem().toString();

        if (seleccion.equals("LIBERADO AL 100%"))
        {
            resIncidencia = "1";
        }
        else if (seleccion.equals("LIBERADO CON DEVOLUCION"))
        {
            resIncidencia = "2";
        }else if (seleccion.equals("PROCESO DE DESCARGA"))
        {
            resIncidencia = "3";
        }else if (seleccion.equals("AUSENCIA"))
        {
            resIncidencia = "4";
        }else if (seleccion.equals("LIBERADO CON FALTANTE"))
        {
            resIncidencia = "5";
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
