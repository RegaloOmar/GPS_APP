package com.rr.gps_app;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.rr.gps_app.Adapter.SessionManager;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EnrrampeActivity extends AppCompatActivity {

    String talon;
    private ImageButton imgDatePicker,imgTimePicker;
    private Button btnUpdate;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private EditText txtDate,txtHour;
    SessionManager sessionManager;
    private static final String TAG = "EnrrampeActivity";
    private RequestQueue requestQueue;
    private static String URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enrrampe);

        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        talon = getIntent().getStringExtra("talon");
        txtDate = findViewById(R.id.fechaNacimiento);
        txtHour = findViewById(R.id.textTime);
        imgDatePicker = findViewById(R.id.imageButton1);
        imgTimePicker = findViewById(R.id.imageButton2);
        btnUpdate = findViewById(R.id.btnActualizarEnrrampe);

        URL = "https://rrdevsolutions.com/cdm/master/request/updateRequest.php";

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
                Log.d(TAG,"onDateSet: dd/mm/yyyy; " + day + "/" + month + "/" + year);

                String date = day + "/" + month + "/" + year;
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

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                enrrampe( URL);
                Intent i = new Intent(EnrrampeActivity.this, CamaraActivity.class);
                i.putExtra("talon",talon);
                startActivity(i);
            }
        });
    }

    private void enrrampe(String URL)
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {

                Toast.makeText(getApplicationContext(),"Se han actualizado fechas.",Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getApplicationContext(),"Elegir horario",Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("talon",talon);
                params.put("fecha",txtDate.getText().toString());
                params.put("hora",txtHour.getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
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
