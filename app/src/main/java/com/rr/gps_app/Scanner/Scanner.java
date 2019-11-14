package com.rr.gps_app.Scanner;


import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import com.rr.gps_app.Adapter.SessionManager;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.rr.gps_app.Camara.CamaraActivity;
import com.rr.gps_app.MainActivity;
import com.rr.gps_app.R;
import com.rr.gps_app.Semaforo.SemaforoActivity;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;

public class Scanner extends AppCompatActivity {

    ImageView imageView;
    String currentPhotoPath, filenamePDF, nombreArchivo, talon, mUSer;
    Button btn_PickImage, btn_PDF;
    Bitmap bitmap;
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;
    public int contador = 0;
    ProgressDialog progreso;
    private Date date;
    File root, file;
    String URL_SUBIRPDF = "https://rrdevsolutions.com/cdm/Scanner/subirScanner.php";
    StringRequest stringRequest;
    RequestQueue request;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);



        talon = getIntent().getStringExtra("talon");

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String,String> user = sessionManager.getUSerDetail();
        mUSer = user.get(sessionManager.USER);
        imageView = findViewById(R.id.imagenScan);
        btn_PickImage = findViewById(R.id.btn_elegir);
        btn_PDF = findViewById(R.id.btnPDF);
        getSupportActionBar().setTitle("Scanner");

        //Fecha Actual
        date = new Date();


        request = Volley.newRequestQueue(Scanner.this);


        btn_PickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contador++;
                dispatchTakePictureIntent();
            }
        });

        btn_PDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearPDF();
                subirPDF();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case COD_SELECCIONA:
                Uri miPath=data.getData();
                if(contador==1){
                    imageView.setImageURI(miPath);
                    try {
                        bitmap=null;
                        bitmap= MediaStore.Images.Media.getBitmap(Scanner.this.getContentResolver(), miPath);
                        imageView.setImageBitmap(bitmap);
                        contador=0;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case COD_FOTO:

                if (contador == 1) {
                    agregarFotoGaleria();
                    setPic();
                    contador = 0;
                }


                break;
        }
    }


    private void crearPDF(){

        //Crear el PDF que se subira al server como archivo escaneado
        PdfDocument pdfDocument = new PdfDocument();
        //Creamos la variable Page info que es la que permitira mostrar la imagen en el PDF
        PdfDocument.PageInfo pi;
        pi = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(),1).create();


        PdfDocument.Page page = pdfDocument.startPage(pi);

        //Se dibuja lo que contiene el pdf
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#FFFFFF"));
        canvas.drawPaint(paint);
        bitmap = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight(), true);
        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap,0,0,null);
        //Se finaliza la pagina
        pdfDocument.finishPage(page);
        //Guardar el Imagen
        root = new File(Environment.getExternalStorageDirectory(), "Scanneos_GPS_APP");
        if(!root.exists()){
            root.mkdir();
        }

        nombreArchivo = "Scan_"+ date.toString() +".pdf";

        file = new File(root, nombreArchivo);

        try{
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            pdfDocument.writeTo(fileOutputStream);
            Toast.makeText(Scanner.this, "Si se pudo", Toast.LENGTH_SHORT).show();
        }catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.close();



    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void agregarFotoGaleria() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        Scanner.this.sendBroadcast(mediaScanIntent);
    }

    private void setPic() {
        // Obtener tamaño del IMageView
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        //Obtener tamaño del bitmap original
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        //Determinar cuanto es la escala de la imagen
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decodigficarla y acomodarla en el iMageview pra que se vea bien vergas
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        imageView.setImageBitmap(bitmap);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Asegurarse de que hay un activity para recibir el intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Crear el File
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error
                ex.printStackTrace();

            }
            // solo continua si la creacion de imagen fue exitosa
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.rr.imagenapdf.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, COD_FOTO);
            }
        }
    }

    private void subirPDF(){


        try {
            filenamePDF = nombreArchivo;
            String uploadId = UUID.randomUUID().toString();
            new MultipartUploadRequest(Scanner.this, uploadId, URL_SUBIRPDF)
                    .addFileToUpload(file.getPath(), "documento")
                    .addParameter("filename", filenamePDF)
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(UploadInfo uploadInfo) {

                        }

                        @Override
                        public void onError(UploadInfo uploadInfo, Exception exception) {

                        }

                        @Override
                        public void onCompleted(UploadInfo uploadInfo, ServerResponse serverResponse) {


                            Toast.makeText(Scanner.this, "Imagen subida exitosamente.", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(UploadInfo uploadInfo){

                        }
                    }).startUpload();
        }
        catch (Exception exc) {
            System.out.println(exc.getMessage() + " " + exc.getLocalizedMessage());
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_logout:
                sessionManager.logout();
                return true;
            case android.R.id.home:
                regresandoTalon();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void regresandoTalon() {
        Intent id;
        id =  new Intent(Scanner.this, SemaforoActivity.class);
        id.putExtra("datosUsuario",mUSer);
        id.putExtra("talon",talon);
        startActivity(id);
    }
}
