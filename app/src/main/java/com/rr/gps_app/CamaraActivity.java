 package com.rr.gps_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
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
import com.android.volley.toolbox.Volley;
import com.rr.gps_app.Adapter.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

 public class CamaraActivity extends AppCompatActivity {


     private static final int COD_SELECCIONA = 10;
     private static final int COD_FOTO = 20;
     Bitmap bmp;
     private Button btn_Camara, btn_CargarFotos, btn_Cancelar, btn_Enrrampe, btn_Galeria;
     private ImageView foto;
     final static int cons = 0;
     public int contador = 0;
     SessionManager sessionManager;
     StringRequest stringRequest;
     RequestQueue request;
     private Date date;
     ProgressDialog progreso;
     String talon, mCurrentPhotoPath;

     private final String CARPETA_PRINCIPAL= "misImagenesAPP/";//directorio principal
     private final String CARPETA_IMAGEN = "imagenes"; //Donde se guardan las fotos
     private final String DIRECTORIO_IMAGEN= CARPETA_PRINCIPAL + CARPETA_IMAGEN; //ruta de la carpeta de directorios
     private String path; //almacena la ruta de la imagen
     File fileImagen;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);

        talon = getIntent().getStringExtra("talon");

        btn_Camara =  findViewById(R.id.btnCamara);
        btn_CargarFotos =  findViewById(R.id.btnCargarFotos);
        btn_Cancelar =  findViewById(R.id.btnCancelar);
        btn_Enrrampe =  findViewById(R.id.btnEnrrampe);
        btn_Galeria = findViewById(R.id.btnGaleria);

        foto =  findViewById(R.id.imageFoto);
        request = Volley.newRequestQueue(CamaraActivity.this);

        getSupportActionBar().setTitle("Camara");

        //Fecha Actual
        date = new Date();

        btn_Camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contador++;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //intent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
                //intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                startActivityForResult(intent, COD_FOTO);
            }
        });

        btn_Galeria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contador++;
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/");
                startActivityForResult(intent.createChooser(intent, "Seleccione"),COD_SELECCIONA);
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

     private void initCamara() {

        File miFile = new File( Environment.getExternalStorageDirectory() , DIRECTORIO_IMAGEN);
        boolean isCreada = miFile.exists();

        if (isCreada ==false){
            isCreada=miFile.mkdirs();
        }else {
            Long consecutivo = System.currentTimeMillis()/1000;
            String nombre = consecutivo.toString() + ".jpg";

            //Ruta de almacenamiento
            path = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString()+"/"+nombre;

            //Contruir el archivo


            fileImagen = new File(path);
            Uri photoURI = FileProvider.getUriForFile(CamaraActivity.this, this.getApplicationContext().getPackageName()
                    + ".my.package.name.provider", fileImagen);


            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            startActivityForResult(intent, COD_FOTO);
        }



    }


     @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);

         switch (requestCode){
             case COD_SELECCIONA:
                 Uri miPath=data.getData();
                 if(contador==1){
                     foto.setImageURI(miPath);
                     try {
                         bmp=null;
                         bmp=MediaStore.Images.Media.getBitmap(CamaraActivity.this.getContentResolver(), miPath);
                         foto.setImageBitmap(bmp);
                         contador=0;
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 }

                 break;
             case COD_FOTO:

                 /*MediaScannerConnection.scanFile(CamaraActivity.this, new String[]{path}, null,
                         new MediaScannerConnection.OnScanCompletedListener() {
                             @Override
                             public void onScanCompleted(String path, Uri uri) {
                                 Log.i("Path", ""+path);
                             }
                         });

                 bmp = BitmapFactory.decodeFile(path);
                 foto.setImageBitmap(bmp);*/



                     if (contador == 1) {
                         bmp=null;
                         Bundle ext = data.getExtras();
                         bmp = (Bitmap) ext.get("data");
                         foto.setImageBitmap(bmp);
                         contador = 0;
                     }


                 break;
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

         final String fechaActual = new SimpleDateFormat("yyyy-MM-dd").format(date);
         String url = "https://rrdevsolutions.com/cdmBueno/master/request/requestPhoto.php";

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

                 String nombre = "Foto_"+consecutivo.toString();
                 String imagen = convertirImagen1(bmp);


                 Map<String, String> parametros = new HashMap<>();
                 parametros.put("nombre", nombre);
                 parametros.put("imagen", imagen);
                 parametros.put("talon", talon);
                 parametros.put("fecha", fechaActual);
                 return parametros;
             }
         };
         request.add(stringRequest);
     }

     private String convertirImagen1(Bitmap bmpFoto) {
         ByteArrayOutputStream array = new ByteArrayOutputStream();
         bmpFoto.compress(Bitmap.CompressFormat.PNG,100,array);
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
