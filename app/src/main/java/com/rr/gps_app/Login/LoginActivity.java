package com.rr.gps_app.Login;

import android.app.ProgressDialog;
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
import com.rr.gps_app.R;
import com.rr.gps_app.Adapter.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button btn_InicoSesion;
    private EditText edtUser, edtPass;
    //private static String url = "https://rrdevsolutions.com/cdmBueno/master/request/login.php";
    private static String url = "https://rrdevsolutions.com/cdmBueno/master/request/login.php";
    private ProgressDialog pDialog;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        edtUser = findViewById(R.id.txtUsuario);
        edtPass = findViewById(R.id.txtPass) ;
        btn_InicoSesion = findViewById(R.id.btnInicioSesion);


        btn_InicoSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final String mUser = edtUser.getText().toString().trim();
                final String mPass = edtPass.getText().toString().trim();

                if (!mUser.isEmpty() || !mPass.isEmpty())
                {
                    login(mUser,mPass);
                }else{
                    edtUser.setError("Insertar Usuario valido.");
                    edtPass.setError("Insertar Contrasena valida.");
                }

            }
        });

    }

    //Conexion al servidor XAMPP
    private void login(final String user, final String pass)
    {
        dialog();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("login");


                    if (success.equals("1"))
                    {

                        for (int i=0; i < jsonArray.length(); i++)
                        {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String user = object.getString("Usuario").trim();
                            String idCanal = object.getString("IdCanal").trim();
                            String destino = object.getString("Destino").trim();

                            sessionManager.createSession(user,idCanal,destino);

                            Intent iw = new Intent(LoginActivity.this, MainActivity.class);
                            iw.putExtra("Usuario",user);
                            iw.putExtra("IdCanal",idCanal);
                            iw.putExtra("Destino",destino);
                            startActivity(iw);
                            finish();

                            dialog();
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    dialog();
                    btn_InicoSesion.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"Datos incorrectos.",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                dialog();
                btn_InicoSesion.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),"Error en la conexion.",Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String,String> params = new HashMap<>();
                params.put("Usuario",user);
                params.put("Password",pass);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    //Metodo para crear dialogos al momento de hacer cambios
    public void dialog()
    {
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Iniciando Sesion...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
    }
}
