 package com.rr.gps_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

 public class CamaraActivity extends AppCompatActivity {

    private Button btn_Camara;
    private ImageView foto1, foto2, foto3, foto4;
    final static int cons = 0;
    public int contador = 0;
    Bitmap bmp;
     SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);

        //Iniciar la camara cuando se abra el activity
        initCamara();

        btn_Camara = (Button) findViewById(R.id.btnCamara);

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

     private void logout() {
         Intent i = new Intent(CamaraActivity.this, LoginActivity.class);
         startActivity(i);
     }
 }
