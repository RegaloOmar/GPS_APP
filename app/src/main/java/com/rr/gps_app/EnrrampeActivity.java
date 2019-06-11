package com.rr.gps_app;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class EnrrampeActivity extends AppCompatActivity {

    private Button btn_FechaEnrrampe;
    private EditText txt_FechaEnrrampe;
    private int dia, mes, año;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrrampe);

        txt_FechaEnrrampe = (EditText) findViewById(R.id.txtFechaEnrrampe);

        btn_FechaEnrrampe = (Button) findViewById(R.id.btnFechaEnrrampe);

        btn_FechaEnrrampe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                dia = c.get(Calendar.DAY_OF_MONTH);
                mes = c.get(Calendar.MONTH);
                año = c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(EnrrampeActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        txt_FechaEnrrampe.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },año,mes,dia);
                datePickerDialog.show();
            }
        });
    }
}
