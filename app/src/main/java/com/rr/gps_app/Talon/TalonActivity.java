package com.rr.gps_app.Talon;

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
import com.rr.gps_app.Adapter.SessionManager;
import com.rr.gps_app.R;
import com.rr.gps_app.Semaforo.SemaforoActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TalonActivity extends AppCompatActivity {

    private EditText txt_Talon;
    private TextView txt_Placas,txt_Sello, txt_Transportista, txt_Net, txt_FechaCita, txt_Confirmacion;
    private Button btn_BuscarTalon, btn_Semaforo;
    SessionManager sessionManager;
    private RequestQueue requestQueue;
    private static String URL = "";
    String talon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talon);

        getSupportActionBar().setTitle("Talon");

        sessionManager = new SessionManager(TalonActivity.this);
        sessionManager.checkLogin();

        txt_Talon = (EditText) findViewById(R.id.txtTalon);
        txt_Placas = (TextView) findViewById(R.id.txtPlacasTalon);
        txt_Sello = (TextView) findViewById(R.id.txtSelloTalon);
        txt_Transportista = (TextView) findViewById(R.id.txtTranspTalon);
        txt_Net = (TextView) findViewById(R.id.txtNetTalon);
        txt_FechaCita = (TextView) findViewById(R.id.txtFechaTalon);
        txt_Confirmacion = (TextView) findViewById(R.id.txtConfimTalon);
        btn_BuscarTalon = (Button)findViewById(R.id.btnBuscarTalon);
        btn_Semaforo = (Button) findViewById(R.id.btnSemaforoTalon);

        btn_BuscarTalon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                talon = txt_Talon.getText().toString();
                //URL = "https://rrdevsolutions.com/cdmBueno/master/request/requestTalon.php?talon="+talon;
                URL = "https://grupopromociones.com/cdm/master/request/requestTalon.php?talon="+talon;
                searchInfo(URL);
            }
        });

        btn_Semaforo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TalonActivity.this, SemaforoActivity.class);
                i.putExtra("talon",talon);
                startActivity(i);
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
                        txt_Talon.setText(jsonObject.getString("Talon_Localidad"));
                        txt_Placas.setText(jsonObject.getString("Placas"));
                        txt_Sello.setText(jsonObject.getString("Sello"));
                        txt_Transportista.setText(jsonObject.getString("Transportista"));
                        txt_Net.setText(jsonObject.getString("Net"));
                        txt_FechaCita.setText(jsonObject.getString("Fecha_Cita"));
                        txt_Confirmacion.setText(jsonObject.getString("Confirmacion"));
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
}
