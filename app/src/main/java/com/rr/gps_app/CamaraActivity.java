 package com.rr.gps_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

 public class CamaraActivity extends AppCompatActivity {

    private Button btn_Camara, btn_CargarFotos;
    private ImageView foto1, foto2, foto3, foto4;
    final static int cons = 0;
    public int contador = 0;
    Bitmap bmp, bmpFoto1;
    SessionManager sessionManager;
    StringRequest stringRequest;
    RequestQueue request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);

        //Iniciar la camara cuando se abra el activity
        initCamara();

        btn_Camara = (Button) findViewById(R.id.btnCamara);
        btn_CargarFotos = (Button) findViewById(R.id.btnCargarFotos);

        foto1 = (ImageView) findViewById(R.id.image1);
        foto2 = (ImageView) findViewById(R.id.image2);
        foto3 = (ImageView) findViewById(R.id.image3);
        foto4 = (ImageView) findViewById(R.id.image4);

        getSupportActionBar().setTitle("Camara");

        btn_Camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                contador++;
                startActivityForResult(i, cons);
            }
        });

        btn_CargarFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarWebService();
            }
        });
        

    }

    private void initCamara() {

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        contador++;
        startActivityForResult(i, cons);
    }


     @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         if(resultCode == Activity.RESULT_OK ){
            if(contador == 1){
                Bundle ext = data.getExtras();
                bmp = (Bitmap)ext.get("data");
                bmpFoto1 = bmp;
                foto1.setImageBitmap(bmp);
            }else if(contador == 2){
                Bundle ext = data.getExtras();
                bmp = (Bitmap)ext.get("data");
                foto2.setImageBitmap(bmp);
            }else if(contador == 3){
                 Bundle ext = data.getExtras();
                 bmp = (Bitmap)ext.get("data");
                 foto3.setImageBitmap(bmp);
             }else if(contador == 4){
                Bundle ext = data.getExtras();
                bmp = (Bitmap)ext.get("data");
                foto4.setImageBitmap(bmp);
                contador = 0;
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

     private void cargarWebService(){

         String url = "https://rrdevsolutions.com/ScriptsPHP/pruebaFoto.php?";

         stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {

                 if(response.trim().equalsIgnoreCase("registra")) {
                     Toast.makeText(CamaraActivity.this, "Se registro con exito", Toast.LENGTH_SHORT).show();
                 } else {
                     Toast.makeText(CamaraActivity.this, "No se registro", Toast.LENGTH_SHORT).show();
                 }

             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 Toast.makeText(CamaraActivity.this, "A ocurrido un error", Toast.LENGTH_SHORT).show();
             }
         }){
             @Override
             protected Map<String, String> getParams() throws AuthFailureError {

                 String imagen1 = convertirImagen1(bmp);
                 String nombre = "Foto1";

                 Map<String, String> parametros= new HashMap<>();
                 parametros.put("nombre", nombre);
                 parametros.put("imagen", imagen1);
                 return super.getParams();
             }
         };
         request.add(stringRequest);
     }


     private String convertirImagen1(Bitmap bmp){

         ByteArrayOutputStream array = new ByteArrayOutputStream();
         bmp.compress(Bitmap.CompressFormat.JPEG,100,array);
         byte[] imagenByte = array.toByteArray();
         String imagenString = Base64.encodeToString(imagenByte, Base64.DEFAULT);
         return  imagenString;

     }


 }
