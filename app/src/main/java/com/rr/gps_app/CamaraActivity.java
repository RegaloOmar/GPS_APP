 package com.rr.gps_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

 public class CamaraActivity extends AppCompatActivity {


     private static final int COD_SELECCIONA = 10;
     private static final int COD_FOTO = 20;
     Bitmap bmp, bmpFoto1;
     private Button btn_Camara, btn_CargarFotos, btn_Cancelar, btn_Enrrampe;
     private ImageView foto1;
     final static int cons = 0;
     public int contador = 0;
     SessionManager sessionManager;
     StringRequest stringRequest;
     RequestQueue request;
     ProgressDialog progreso;
     String talon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);

        talon = getIntent().getStringExtra("talon");

        btn_Camara =  findViewById(R.id.btnCamara);
        btn_CargarFotos =  findViewById(R.id.btnCargarFotos);
        btn_Cancelar =  findViewById(R.id.btnCancelar);
        btn_Enrrampe =  findViewById(R.id.btnEnrrampe);

        foto1 =  findViewById(R.id.image1);
        request = Volley.newRequestQueue(CamaraActivity.this);

        getSupportActionBar().setTitle("Camara");

        btn_Camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mostrarDialogoOpciones();
                initCamara();
            }
        });

        btn_CargarFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarWebService();
            }
        });

        btn_Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresarHome();
            }
        });

        btn_Enrrampe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Enrrampe();
            }
        });
        

    }

     private void regresarHome() {

        Intent i = new Intent(CamaraActivity.this, MainActivity.class);
        startActivity(i);
     }

     private void mostrarDialogoOpciones(){
        final CharSequence[] opciones = {"Tomar Foto", "Elegir de la Galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Elige una Opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (opciones[i].equals("Tomar Foto")){
                    //aqui se activa la camara ahora
                    initCamara();

                }else{
                    if(opciones[i].equals("Elegir de la Galeria")){
                        contador++;
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/");
                        startActivityForResult(intent.createChooser(intent, "Seleccione"),COD_SELECCIONA);
                    }else {
                        dialog.dismiss();
                    }
                }
            }
        });
        builder.show();
    }

     private void initCamara() {

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        contador++;
        startActivityForResult(i, cons);
    }


     @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         /*switch (requestCode){
             case COD_SELECCIONA:
                 Uri miPath=data.getData();
                 if(contador==1){
                     foto1.setImageURI(miPath);
                     try {
                         bmpFoto1=MediaStore.Images.Media.getBitmap(CamaraActivity.this.getContentResolver(), miPath);
                         foto1.setImageBitmap(bmpFoto1);
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 }

                 break;
             case COD_FOTO:
                 //Iniciar la camara cuando se abra el activity
                 initCamara();
                 break;
         }*/
         if(resultCode == Activity.RESULT_OK ){
            if(contador == 1){
                Bundle ext = data.getExtras();
                bmp = (Bitmap)ext.get("data");
                bmpFoto1 = bmp;
                foto1.setImageBitmap(bmp);
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

        progreso=new ProgressDialog(CamaraActivity.this);
        progreso.setMessage("Cargando...");
        progreso.show();

         String url = "https://rrdevsolutions.com/cdm/master/request/requestPhoto.php";

         stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {

                 progreso.hide();

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
                 Long consecutivo = System.currentTimeMillis()/1000;

                 String nombre = "Foto1_"+consecutivo.toString();
                 String imagen = convertirImagen1(bmpFoto1);


                 Map<String, String> parametros = new HashMap<>();
                 parametros.put("nombre", nombre);
                 parametros.put("imagen", imagen);
                 parametros.put("talon", talon);
                 return parametros;
             }
         };
         request.add(stringRequest);
     }

     private String convertirImagen1(Bitmap bmpFoto1) {
         ByteArrayOutputStream array = new ByteArrayOutputStream();
         bmpFoto1.compress(Bitmap.CompressFormat.JPEG,100,array);
         byte[] imagenByte = array.toByteArray();
         String imagenString = Base64.encodeToString(imagenByte, Base64.DEFAULT);
         return imagenString;
     }

     private void Enrrampe(){
        Intent i = new Intent(CamaraActivity.this, EnrrampeActivity.class);
        i.putExtra("talon",talon);
        startActivity(i);
     }


 }
