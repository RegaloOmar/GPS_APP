package com.rr.gps_app;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;

public class EnrrampeActivity extends AppCompatActivity {

    String talon;
    private ImageButton imgDatePicker;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText txtFechaNac;
    private static final String TAG = "EnrrampeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrrampe);

        talon = getIntent().getStringExtra("talon");
        txtFechaNac = findViewById(R.id.fechaNacimiento);
        imgDatePicker = findViewById(R.id.imageButton1);

        //Mediante eventos mandamos a llamar a la vista del calendario
        imgDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EnrrampeActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        //Mediante esta variable obtenemos la fecha del calendario del datepicker
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day)
            {
                month = month + 1;
                Log.d(TAG,"onDateSet: mm/dd/yyyy; " + month + "/" + day + "/" + year);

                String date = month + "/" + day + "/" + year;
                txtFechaNac.setText(date);
            }
        };


    }
}
