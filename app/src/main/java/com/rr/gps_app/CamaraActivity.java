 package com.rr.gps_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

 public class CamaraActivity extends AppCompatActivity {

    private Button btn_Camara;
    private ImageView foto;
    final static int cons = 0;
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);

        btn_Camara = (Button) findViewById(R.id.btnCamara);

        foto = (ImageView) findViewById(R.id.image1);

        getSupportActionBar().setTitle("Camara");
        
        //Iniciar la camara cuando se abra el activity
        initCamara();
    }

    private void initCamara() {


        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, cons);
        /*btn_Camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id;
                id = v.getId();
                switch(id){
                    case R.id.btnCamara:
                        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(i, cons);
                        break;
                }
            }
        });*/
    }


     @Override
     protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
         super.onActivityResult(requestCode, resultCode, data);
         if(resultCode == Activity.RESULT_OK ){
             Bundle ext = data.getExtras();
             bmp = (Bitmap)ext.get("data");
             foto.setImageBitmap(bmp );
         }
     }
 }
