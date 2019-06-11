package com.rr.gps_app;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;

public class EnrrampeActivity extends AppCompatActivity {

    String talon;
    private ImageButton imgDatePicker,imgTimePicker;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText txtDate,txtHour;
    private static final String TAG = "EnrrampeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrrampe);

        talon = getIntent().getStringExtra("talon");
        txtDate = findViewById(R.id.fechaNacimiento);
        txtHour = findViewById(R.id.textTime);
        imgDatePicker = findViewById(R.id.imageButton1);
        imgTimePicker = findViewById(R.id.imageButton2);

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
                txtDate.setText(date);
            }
        };

        //Mediante el evento convocamos al time picker
        imgTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(EnrrampeActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mTimeSetListener,hour,minute,android.text.format.DateFormat.is24HourFormat(getApplicationContext()));

                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();

            }
        });

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute)
            {
                txtHour.setText(hourOfDay + ":" + minute);
            }
        };
    }


}
