package com.rr.gps_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rr.gps_app.Adapter.DatosAdapter;
import com.rr.gps_app.Class.Datos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatosActivity extends AppCompatActivity {

    String user, canal;
    SessionManager sessionManager;
    private static String URL = "";
    private Date date;
    //a list to store all the products
    List<Datos> datosList;
    //the recyclerview
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        getSupportActionBar().setTitle("Datos del Talon");

        user = getIntent().getStringExtra("datosUsuario");
        canal = getIntent().getStringExtra("datosCanal");

        //Fecha Actual
        date = new Date();
        final String fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(date);


        URL = "https://rrdevsolutions.com/cdmBueno/master/request/requestRecycler.php?usuario="+user+"&dateusuario="+fechaActual;
        //URL = "https://rrdevsolutions.com/cdm/master/request/requestRecycler.php?usuario="+user;

        //getting the recyclerview from xml
        recyclerView = (RecyclerView) findViewById(R.id.recyclerDatos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);



        //initializing the productlist
        datosList = new ArrayList<>();


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
                                JSONObject datos = array.getJSONObject(i);

                                //adding the product to product list
                                datosList.add(new Datos(
                                        datos.getString("Talon_Localidad"),
                                        datos.getString("Placas"),
                                        datos.getString("Sello"),
                                        datos.getString("Transportista"),
                                        datos.getString("Net"),
                                        datos.getString("Fecha_Cita"),
                                        datos.getString("Confirmacion"),
                                        datos.getString("Fecha_Cita_Hora")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            DatosAdapter adapter = new DatosAdapter(DatosActivity.this, datosList);
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
