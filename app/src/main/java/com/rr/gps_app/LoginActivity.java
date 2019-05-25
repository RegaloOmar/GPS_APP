package com.rr.gps_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.rr.gps_app.MainActivity;

public class LoginActivity extends AppCompatActivity {

    private Button btn_InicoSesion;
    private EditText email_text;
    private EditText pass_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email_text = (EditText) findViewById(R.id.emailText);

        pass_text = (EditText) findViewById(R.id.passText) ;

        btn_InicoSesion = (Button) findViewById(R.id.buttonInicioSesion);

        btn_InicoSesion.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {


                                               }
                                           }

        );

    }
}
