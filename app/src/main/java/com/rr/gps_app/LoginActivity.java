package com.rr.gps_app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.rr.gps_app.MainActivity;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button btn_InicoSesion;
    private EditText email_text;
    private EditText pass_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_text = (EditText) findViewById(R.id.emailText);

        pass_text = (EditText) findViewById(R.id.passText) ;

        btn_InicoSesion = (Button) findViewById(R.id.buttonInicioSesion);

        btn_InicoSesion.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {

                                                   ejecutarServicio("http://192.168.64.2:8080/Developer/Insertar_usuario.php");

                                                   //Intent i;
                                                   //i =  new Intent(LoginActivity.this, MainActivity.class);
                                                   // startActivity(i);
                                               }
                                           }

        );

    }

    //Conexion al servidor XAMPP
    private void ejecutarServicio(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "Operación Exitosa", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Algo salio mal", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new HashMap<String, String>();
                //Datos a insertar primero va el nombre declarado en PHP
                //despues el campo del formulario donde sacaremos la info ("PHP", formulario)
                parametros.put("usuario", email_text.getText().toString());
                parametros.put("pasword", pass_text.getText().toString());
                return super.getParams();
            }
        };
        //Procesar las peticiones hechas por la app para que la libreria Volley las ejecute
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
