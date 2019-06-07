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
import java.util.HashMap;
import java.util.Map;

 public class CamaraActivity extends AppCompatActivity {


     private static final int COD_SELECCIONA = 10;
     private static final int COD_FOTO = 20;
     private static final String CARPETA_PRINCIPAL = "misImagenesApp/";
     private static final String CARPETA_IMAGEN = "imagenes";
     private static final String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL + CARPETA_IMAGEN;
     private String path;
     File fileImagen;
     Bitmap bmp, bmpFoto1;


     private Button btn_Camara, btn_CargarFotos;
     private ImageView foto1, foto2, foto3, foto4;
     final static int cons = 0;
     public int contador = 0;
     SessionManager sessionManager;
     StringRequest stringRequest;
     RequestQueue request;
     ProgressDialog progreso;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);

        //Iniciar la camara cuando se abra el activity
        //initCamara();

        btn_Camara = (Button) findViewById(R.id.btnCamara);
        btn_CargarFotos = (Button) findViewById(R.id.btnCargarFotos);

        foto1 = (ImageView) findViewById(R.id.image1);
        foto2 = (ImageView) findViewById(R.id.image2);
        foto3 = (ImageView) findViewById(R.id.image3);
        foto4 = (ImageView) findViewById(R.id.image4);
        request = Volley.newRequestQueue(this);

        getSupportActionBar().setTitle("Camara");

        btn_Camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoOpciones();
            }
        });

        btn_CargarFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarWebService();
            }
        });
        

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

                    abrirCamara();
                    /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    contador++;
                    startActivityForResult(intent, cons);*/




                }else{
                    if(opciones[i].equals("Elegir de la Galeria")){

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

     private void abrirCamara() {
        File miFile = new File(Environment.getExternalStorageDirectory(),DIRECTORIO_IMAGEN);
        boolean isCreada=miFile.exists();

        if(isCreada==false){
            isCreada=miFile.mkdirs();
        }


        if (isCreada == true){
            //Consecutivo captura fecha y hora para ponersela de nombre
            Long consecutivo = System.currentTimeMillis()/1000;
            //Le añade la extencion .jpg al nombre
            String nombre = consecutivo.toString()+".jpg";
            //Ruta de almacenamiento para guardar la imagen
            path = Environment.getExternalStorageDirectory()+File.separator+DIRECTORIO_IMAGEN
                    +File.separator+nombre; // Ruta de almacenamiento apra la foto

            fileImagen = new File(path);
            //Abrir camara
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(fileImagen));
            contador++;
            startActivityForResult(intent, COD_FOTO);
        }

     }

     private void initCamara() {

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        contador++;
        startActivityForResult(i, cons);
    }


     @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);


         switch (requestCode){
             case COD_SELECCIONA:
                 Uri miPath=data.getData();
                 foto1.setImageURI(miPath);
                 break;
             case COD_FOTO:
                 MediaScannerConnection.scanFile(CamaraActivity.this, new String[]{path}, null,
                         new MediaScannerConnection.OnScanCompletedListener() {
                             @Override
                             public void onScanCompleted(String path, Uri uri) {
                                 Log.i("Path",""+path);
                             }
                         });

                 bmp = BitmapFactory.decodeFile(path);
                 foto1.setImageBitmap(bmp);
                 break;
         }
         /*if(resultCode == Activity.RESULT_OK ){
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
         }*/
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

        progreso=new ProgressDialog(this);
        progreso.setMessage("Cargando...");
        progreso.show();

         String url = "https://rrdevsolutions.com/ScriptsPHP/pruebaFoto.php";

         stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {

                 progreso.hide();

                 if(response.equalsIgnoreCase("registra")) {
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

                 String nombre = "Foto1";
                 String imagen1 = convertirImagen1(bmpFoto1);


                 Map<String, String> parametros = new HashMap<>();
                 parametros.put("nombre", nombre);
                 parametros.put("imagen", imagen1);
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


 }
