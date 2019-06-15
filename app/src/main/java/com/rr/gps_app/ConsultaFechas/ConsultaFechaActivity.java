package com.rr.gps_app.ConsultaFechas;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.rr.gps_app.R;
import com.rr.gps_app.Adapter.SessionManager;

import java.util.Calendar;

public class ConsultaFechaActivity extends AppCompatActivity {

    SessionManager sessionManager;
    String user, canal, date;
    private ImageView imgDatePicker;
    private Button btnConsultar;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TextView txtDate;
    private static final String TAG = "ConsultaFechaActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_fecha);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        getSupportActionBar().setTitle("Talones por Fecha");

        user = getIntent().getStringExtra("datosUsuario");
        canal = getIntent().getStringExtra("datosCanal");

        txtDate = findViewById(R.id.dateCalendar);
        imgDatePicker = findViewById(R.id.calendarView);
        btnConsultar = findViewById(R.id.btnConsultarFecha);


        //Mediante eventos mandamos a llamar a la vista del calendario
        imgDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ConsultaFechaActivity.this,
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
                Log.d(TAG,"onDateSet: dd/mm/yyyy; " + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
                txtDate.setText(date);
            }
        };

        date = txtDate.getText().toString();

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent i = new Intent(ConsultaFechaActivity.this, FechaActivity.class);
                i.putExtra("mUser",user);
                i.putExtra("mCanal",canal);
                i.putExtra("mFecha",date);
                startActivity(i);
            }
        });

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
}
