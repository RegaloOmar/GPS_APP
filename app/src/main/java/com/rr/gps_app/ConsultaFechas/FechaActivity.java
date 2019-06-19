package com.rr.gps_app.ConsultaFechas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rr.gps_app.Adapter.FechaAdapter;
import com.rr.gps_app.Class.Fechas;
import com.rr.gps_app.R;
import com.rr.gps_app.Adapter.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FechaActivity extends AppCompatActivity {

    SessionManager sessionManager;
    String user, canal, datechoose;
    private static String URL = "";;
    //a list to store all the products
    List<Fechas> fechasList;
    //the recyclerview
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fecha);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        getSupportActionBar().setTitle("Datos por Fecha");

        user = getIntent().getStringExtra("mUser");
        canal = getIntent().getStringExtra("mCanal");
        datechoose = getIntent().getStringExtra("mFecha");

        Toast.makeText(getApplicationContext(),datechoose,Toast.LENGTH_LONG).show();


        URL = "https://rrdevsolutions.com/cdmBueno/master/request/requestRecyclerDate.php?usuario="+user+"&datechoose="+datechoose;
        //URL = "https://rrdevsolutions.com/cdm/master/request/requestRecycler.php?usuario="+user+"&datechoose="+datechoose;


        //getting the recyclerview from xml
        recyclerView =  findViewById(R.id.recyclerFechas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);



        //initializing the productlist
        fechasList = new ArrayList<>();


        //this method will fetch and parse json
        //to display it in recyclerview
        loadDatos();

    }

    private void loadDatos() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject fechas = array.getJSONObject(i);

                                //adding the product to product list
                                fechasList.add(new Fechas(
                                        fechas.getString("Talon_Localidad"),
                                        fechas.getString("Placas"),
                                        fechas.getString("Sello"),
                                        fechas.getString("Transportista"),
                                        fechas.getString("Net"),
                                        fechas.getString("Fecha_Cita"),
                                        fechas.getString("Confirmacion"),
                                        fechas.getString("Fecha_Cita_Hora")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            FechaAdapter adapter = new FechaAdapter(FechaActivity.this, fechasList);
                            recyclerView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(this).add(stringRequest);
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
